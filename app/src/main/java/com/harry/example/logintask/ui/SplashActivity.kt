package com.harry.example.logintask.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.Observer
import com.harry.example.logintask.R
import com.harry.example.logintask.pojos.ApiResponse
import com.harry.example.logintask.utility.*
import com.harry.example.logintask.viewmodels.LoginViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    private var username: String? = null
    private var password: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        observeResponse()
        Thread {
            Thread.sleep(3000)
            retrieveUserCredentials()
        }.start()

    }

    private fun retrieveUserCredentials() {
        val sharedPreferences = getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
        if (sharedPreferences.contains(USERNAME) && sharedPreferences.contains(PASSWORD)) {
            username = sharedPreferences.getString(USERNAME, "")
            password = sharedPreferences.getString(PASSWORD, "")
            loginUser(username, password)
        } else {
            toLoginActivity()
        }
    }

    private fun loginUser(username: String?, password: String?) {
        if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
            loginViewModel.loginUser(username, password)
        }
    }

    private fun observeResponse() {
        loginViewModel.loginResponse.observe(this, Observer {
            if (it?.data != null) {
                Log.i("TAG", "observeResponse: ")
                toHomeActivity(it)
            } else {
                Toast.makeText(applicationContext, SOMETHING_WENT_WRONG, Toast.LENGTH_LONG).show()
                toLoginActivity()
            }
        })
    }

    private fun toHomeActivity(apiResponse: ApiResponse) {
        val intent = Intent(applicationContext, HomeActivity::class.java)
        intent.putExtra(EMPLOYEE_NAME, apiResponse.data.company_name)
        intent.putExtra(COMPANY_NAME, apiResponse.data.employee_name)
        intent.putExtra(
            NOTIFICATION_FILTER_ID,
            apiResponse.data.notificationFilter[0].notification_filter_id
        )
        startActivity(intent)
    }

    private fun toLoginActivity() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        startActivity(intent)
    }
}