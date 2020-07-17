package com.harry.example.logintask.api

import com.google.gson.JsonObject
import com.harry.example.logintask.pojos.ApiResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface Client {

    @POST("api/v1/login")
    @FormUrlEncoded
    suspend fun loginUser(
        @Field("username") username: String,
        @Field("password") password: String
    ): Response<ApiResponse>
}