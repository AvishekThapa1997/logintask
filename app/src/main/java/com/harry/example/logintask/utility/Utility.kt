package com.harry.example.logintask.utility

import android.content.Context
import android.widget.Toast

object Utility {
    fun showMessage(context: Context, connected: Boolean?) {
        connected.let {
            if(connected == false) {
                Toast.makeText(context, NO_INTERNET_CONNECTION, Toast.LENGTH_LONG).show()
            }

        }
    }
}