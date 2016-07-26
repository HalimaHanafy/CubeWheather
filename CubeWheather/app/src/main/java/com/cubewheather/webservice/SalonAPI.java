package com.cubewheather.webservice;

import com.cubewheather.models.forcast16.WeatherForecastResponseModel;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by halimahanafy on 25/07/16.
 */

public interface SalonAPI {


//cnt=7 this is days num you need
    @GET("/data/2.5/forecast/daily?units=metric&APPID=4f087bf7b1cdc161443d65c7be0feccd&cnt=7")
    Call<WeatherForecastResponseModel> getWeatherForecast(@Query("q") String q);
}
