package com.example.syennamani.meowfest;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by syennamani on 1/29/2018.
 */

public interface WebService {
    /*@GET("data/2.5/weather")
    Single<WeatherResponse> queryWeatherByCityName(@Query("q") String query,
                                                   @Query("APPID") String appId);*/


    @GET("cats?page={offset}")
    Observable<List<Cats>> getCatsData(@Path("offset") String offset);



}
