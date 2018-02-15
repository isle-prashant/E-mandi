package com.twosquares.e_mandi.utils

import android.content.Context
import android.net.ConnectivityManager

/**
* Created by Prashant Kumar on 12/19/2017.
*/
class NetworkConnectivityStatus private constructor(){

    companion object {
        private val instance = NetworkConnectivityStatus()
        private var context: Context? = null
        fun getInstance(context: Context): NetworkConnectivityStatus {
            this.context = context.applicationContext
            return instance
        }
    }


     fun isNetworkAvailable(): Boolean {
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}