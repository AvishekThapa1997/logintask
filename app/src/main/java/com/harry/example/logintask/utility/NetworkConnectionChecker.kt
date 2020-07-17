package com.harry.example.logintask.utility

import android.annotation.TargetApi
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData

class NetworkConnectionChecker(private val context: Context) : LiveData<Boolean>() {
    var connectivityManager: ConnectivityManager
    var networkCallback: ConnectivityManager.NetworkCallback

    init {
        connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        networkCallback = getNetworkCallBacks()
    }

    override fun onActive() {
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> {
                registerNetworkForAboveLolipop(networkCallback)
            }
            else -> {
                val networkRequest = getNetworkRequest()
                connectivityManager.registerNetworkCallback(networkRequest, networkCallback)
            }
        }
    }

    override fun onInactive() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun getNetworkRequest(): NetworkRequest {
        val networkRequestBuilder: NetworkRequest.Builder = NetworkRequest.Builder()
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        networkRequestBuilder.addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
        return networkRequestBuilder.build()
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun registerNetworkForAboveLolipop(networkCallback: ConnectivityManager.NetworkCallback) {
        connectivityManager.registerDefaultNetworkCallback(networkCallback)
    }

    private fun getNetworkCallBacks(): ConnectivityManager.NetworkCallback {
        val networkCallback = object :
            ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                postValue(true)
            }

            override fun onLost(network: Network) {
                postValue(false)
            }

            override fun onUnavailable() {
                postValue(false)
            }
        }
        return networkCallback
    }
}