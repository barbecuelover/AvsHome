package com.zw.avshome.home;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zw.avshome.R;
import com.zw.avshome.home.base.ParentFragment;
import com.zw.avshome.home.bean.Weather;
import com.zw.avshome.home.bean.WeatherRequestInterface;

import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.core.app.ActivityCompat;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class HomeFragment extends ParentFragment {

    private final String TAG = "HomeFragment";

    private final String weatherIconAddress = "https://images-na.ssl-images-amazon.com/images/G/01/alexa/avs/gui/large/currentWeatherIcon/";

    /** Beijing City */
    private TextView mCityName;
    /** Sunny */
    private TextView mWeatherState;
    /** 22℃ */
    private TextView mTemperatureValue;
    private TextView mAlexaTips;
    private ImageView mWeatherIcon;

    private RxPermissions permissions;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        view = inflater.inflate(R.layout.fragment_home, container, false);
        initView();
        initData();
        initEvent();
        return view;
    }

    @Override
    public void initView() {
        mCityName = view.findViewById(R.id.home_city_name);
        mWeatherState = view.findViewById(R.id.home_weather_state);
        mTemperatureValue = view.findViewById(R.id.home_temperature_value);
        mWeatherIcon = view.findViewById(R.id.home_weather_icon);
        mAlexaTips = view.findViewById(R.id.home_news_tips);
    }

    @Override
    public void initData() {
        permissions  = new RxPermissions(this);
    
        setWeather();
        setAlexaTips();
    }

    @Override
    public void initEvent() {

    }


    private void setWeather() {

        final String city = "Beijing";
        //创建 retrofit 对象
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.openweathermap.org/") //设置网络请求url
                .addConverterFactory(GsonConverterFactory.create()) // 设置使用Gson解析 记得加入依赖 'com.squareup.retrofit2:converter-gson:2.5.0'
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create()) //支持RxJava2
                .build();

        WeatherRequestInterface requestInterface = retrofit.create(WeatherRequestInterface.class);
        final Observable<Weather> weatherObservable = requestInterface.getWeather(city, "eb3ab565ef059bf15a893164c74710ff");

        Disposable weatherDisosable = Observable.interval(0, 10, TimeUnit.MINUTES) // 10 分钟查询一次天气
                .doOnNext(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Disposable disposable = weatherObservable.subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Weather>() {
                                    @Override
                                    public void accept(Weather weather) throws Exception {
                                        String weatherStr = weather.getWeather().get(0).getMain();
                                        String iconStr = weather.getWeather().get(0).getIcon();
                                        String iconUrl = getWeatherUrl(iconStr);
                                        double temp = weather.getMain().getTemp();
                                        //int fTemperature = (int) Math.round((9 / 5) * (temp - 273.15) + 32);
                                        int cTemperature = (int)(temp - 273.15);
                                        String cityName = weather.getName();

                                        Log.i(TAG, "weatherStr :" + weatherStr);
                                        Log.i(TAG, "\nTemperature :" + cTemperature);
                                        Log.i(TAG, "\ncityName :" + cityName);

                                        mWeatherState.setText(weatherStr);
                                        mTemperatureValue.setText(cTemperature + "℃");
                                        mCityName.setText(cityName);
                                        Glide.with(context).load(iconUrl).into(mWeatherIcon);


                                    }
                                }, new Consumer<Throwable>() {
                                    @Override
                                    public void accept(Throwable throwable) throws Exception {

                                    }
                                });
                    }
                })
                .subscribe();

    }


    private Location getLocation() {
        String locationProvider = "";
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        List<String> providers = locationManager.getProviders(true);


        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {

            return null;
        }

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Disposable permissionDisposable =permissions.request(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean aBoolean) throws Exception {
                            if (aBoolean){

                            }
                        }
                    });

        }
        Location location = locationManager.getLastKnownLocation(locationProvider);
        return location;
    }


    private void setAlexaTips() {

        final String[] tips = getResources().getStringArray(R.array.footer_tips);

        Disposable tipsDisposable = Observable
                .interval(0, 6, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        mAlexaTips.setText(tips[(int) (aLong % (tips.length))]);
                    }
                });
    }



    private String getWeatherUrl(String iconStr) {
        String weatherIconUrl;
        switch (iconStr) {
            case "01d":
                weatherIconUrl = weatherIconAddress + "sunny.png";
                break;
            case "01n":
                weatherIconUrl = weatherIconAddress + "clear_night.png";
                break;
            case "03d":
            case "04d":
            case "03n":
            case "04n":
                weatherIconUrl = weatherIconAddress + "cloudy.png";
                break;
            case "02d":
                weatherIconUrl = weatherIconAddress + "partly_cloudy.png";
                break;
            case "02n":
                weatherIconUrl = weatherIconAddress + "partly_cloudy_night.png";
                break;
            case "09d":
            case "10d":
            case "09n":
            case "10n":
                weatherIconUrl = weatherIconAddress + "rainy.png";
                break;
            case "11d":
            case "11n":
                weatherIconUrl = weatherIconAddress + "lightning.png";
                break;
            case "13d":
            case "13n":
                weatherIconUrl = weatherIconAddress + "snow.png";
                break;
            case "50d":
            case "50n":
            default:
                weatherIconUrl = "http://openweathermap.org/img/w/50n.png";
                break;
        }
        return weatherIconUrl;
    }

}
