package com.example.weather.data.remote

import com.example.weather.data.remote.activity_entity.Activity
import retrofit2.http.GET

interface BoredApi {
    @GET("./activity/")
    suspend fun getActivity(): Activity
}