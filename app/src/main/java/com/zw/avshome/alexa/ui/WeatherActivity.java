package com.zw.avshome.alexa.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.zw.avshome.home.base.ParentActivity;
import com.zw.avshome.R;
import com.zw.avshome.alexa.util.DownloadImageTask;
import com.zw.avshome.alexa.interfaces.ClearTemplateListener;
import com.zw.avshome.alexa.AlexaService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;



public class WeatherActivity extends ParentActivity {

    private final String sTag = "WeatherActivity";

    private Activity mActivity;
    private  TextView mMainTitle;
    private  TextView mSubTitle;
    private  ImageView mCurrentWeatherIcon;
    private  TextView mCurrentWeather;
    private  TextView mHighTempCurrent;
    private  TextView mLowTempCurrent;


    private static View[] mForecasts = new View[5];
    private static JSONObject mJsonObject;


    public static Intent createIntent(Context context, JSONObject jsonObject) {
        mJsonObject = jsonObject;
        return new Intent(context, WeatherActivity.class);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_weather_template);
        mActivity = this;
        initView();
        initData();
        initEvent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        initData();
    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    public Activity getActivity() {
        return this;
    }

    @Override
    public void initView() {

        mMainTitle = (TextView) findViewById(R.id.mainTitle);
        mSubTitle = (TextView) findViewById(R.id.subTitle);
        mCurrentWeatherIcon = (ImageView) findViewById(R.id.currentWeatherIcon);
        mCurrentWeather = (TextView) findViewById(R.id.currentWeather);
        mHighTempCurrent = (TextView) findViewById(R.id.highTempCurrent);
        mLowTempCurrent = (TextView) findViewById(R.id.lowTempCurrent);
        mForecasts[0] = (View) findViewById(R.id.forecast0);
        mForecasts[1] = (View) findViewById(R.id.forecast1);
        mForecasts[2] = (View) findViewById(R.id.forecast2);
        mForecasts[3] = (View) findViewById(R.id.forecast3);
        mForecasts[4] = (View) findViewById(R.id.forecast4);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mActivity = null;
    }

    @Override
    public void initData() {
        configureWeatherTemplate(mJsonObject);
    }

    @Override
    public void initEvent() {
        setClearTemplateListener();
    }

    public void downloadImage(final ImageView imageView, final String url) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new DownloadImageTask(imageView).execute(url);
            }
        });
    }

    private void configureWeatherTemplate(JSONObject template) {
        try {
            if (template.has("title")) {
                JSONObject title = template.getJSONObject("title");
                if (title.has("mainTitle")) {
                    String mainTitle = title.getString("mainTitle");
                    mMainTitle.setText(mainTitle);
                    mMainTitle.setTextSize(18);
                }

                if (title.has("subTitle")) {
                    String subTitle = title.getString("subTitle");
                    mSubTitle.setText(subTitle);
                    mSubTitle.setTextSize(16);
                }
            }

            if (template.has("currentWeather")) {
                String currentWeather = template.getString("currentWeather");
                mCurrentWeather.setText(currentWeather);
                mCurrentWeather.setTextSize(90);
            }

            if (template.has("currentWeatherIcon")) {
                String currentWeatherIconURL =
                        getImageUrl(template.getJSONObject("currentWeatherIcon"));
                downloadImage(mCurrentWeatherIcon, currentWeatherIconURL);
            }

            if (template.has("highTemperature")) {
                String highTempValue =
                        template.getJSONObject("highTemperature").getString("value");
                mHighTempCurrent.setText(highTempValue);
                mHighTempCurrent.setTextSize(36);
            }

            if (template.has("lowTemperature")) {
                String lowTempValue =
                        template.getJSONObject("lowTemperature").getString("value");
                mLowTempCurrent.setText(lowTempValue);
                mLowTempCurrent.setTextSize(36);
            }



                if (template.has("weatherForecast")) {
                    JSONArray forecasts = template.getJSONArray("weatherForecast");
                    for (int j = 0; j < mForecasts.length; j++) {
                        // Get forecast
                        JSONObject next = forecasts.getJSONObject(j);
                        View forecastView = getForecast(j);

                        // Set icon
                        JSONObject image = next.getJSONObject("image");
                        String url = getImageUrl(image);
                        downloadImage((ImageView) forecastView.findViewById(R.id.forecastIcon), url);

                        // Set day
                        String day = next.has("day") ? next.getString("day") : "";
                        ((TextView) forecastView.findViewById(R.id.day)).setText(day);
                        ((TextView) forecastView.findViewById(R.id.day)).setTextSize(16);

                        // Set high temp
                        String high = next.has("highTemperature")
                                ? next.getString("highTemperature") : "";
                        ((TextView) forecastView.findViewById(R.id.highTemp)).setText(high);
                        ((TextView) forecastView.findViewById(R.id.highTemp)).setTextSize(16);

                        // Set low temp
                        String low = next.has("lowTemperature")
                                ? next.getString("lowTemperature") : "";
                        ((TextView) forecastView.findViewById(R.id.lowTemp)).setText(low);
                        ((TextView) forecastView.findViewById(R.id.lowTemp)).setTextSize(16);

                    }

            }

        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
    }

    private String getImageUrl(JSONObject image) {
        String url = null;

        try {
            JSONArray sources = image.getJSONArray("sources");
            HashMap<String, String> imageMap = new HashMap<>();

            for (int j = 0; j < sources.length(); j++) {
                JSONObject next = sources.getJSONObject(j);
                String size;
                if (next.has("size")) {
                    size = next.getString("size").toUpperCase();
                } else {
                    size = "DEFAULT";
                }
                imageMap.put(size, next.getString("url"));
            }

            if (imageMap.containsKey("DEFAULT")) {
                url = imageMap.get("DEFAULT");
            } else if (imageMap.containsKey("X-LARGE")) {
                url = imageMap.get("X-LARGE");
            } else if (imageMap.containsKey("LARGE")) {
                url = imageMap.get("LARGE");
            } else if (imageMap.containsKey("MEDIUM")) {
                url = imageMap.get("MEDIUM");
            } else if (imageMap.containsKey("SMALL")) {
                url = imageMap.get("SMALL");
            } else if (imageMap.containsKey("X-SMALL")) {
                url = imageMap.get("X-SMALL");
            }
        } catch (JSONException e) {
            Log.e(sTag, e.getMessage());
        }
        return url;
    }

    private View getForecast(int number) {
        return mForecasts[number];
    }

    public void setClearTemplateListener() {
        final AlexaService alexaService = AlexaService.getInstance();
        alexaService.clearTemplateInNewActivity();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                alexaService.setClearTemplateListener(new ClearTemplateListener() {
                    @Override
                    public void onClearTemplate() {
                        if (mActivity != null) {
                            finishActivity();
                        }
                    }
                });
            }
        },4000);
    }

}
