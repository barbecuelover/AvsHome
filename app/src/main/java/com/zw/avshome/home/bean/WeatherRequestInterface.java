package com.zw.avshome.home.bean;

import com.zw.avshome.home.bean.Weather;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface WeatherRequestInterface {
    //data/2.5/weather?q={city}&appid=eb3ab565ef059bf15a893164c74710ff
    @GET("data/2.5/weather")
    Observable<Weather> getWeather(@Query("q") String city, @Query("appid") String appid);
    Observable<Weather> getWeather(@QueryMap Map<String, String> map); // 也可用@QueryMap
}
