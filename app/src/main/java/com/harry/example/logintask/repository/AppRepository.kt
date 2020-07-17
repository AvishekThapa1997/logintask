package com.harry.example.logintask.repository

import android.util.Log
import com.harry.example.logintask.api.Client
import com.harry.example.logintask.pojos.ApiResponse
import com.harry.example.logintask.utility.NO_INTERNET_CONNECTION
import kotlinx.coroutines.delay
import java.net.UnknownHostException


class AppRepository(private val client: Client) {
    suspend fun loginUser(username: String?, password: String?): ApiResponse? {
        try {
            delay(5000)
            val response = client.loginUser(username!!, password!!)
            return response.body()
        } catch (exception: UnknownHostException) {
            return null
        }
    }
}