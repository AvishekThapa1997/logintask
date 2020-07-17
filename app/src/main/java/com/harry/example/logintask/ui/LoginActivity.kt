package com.harry.example.logintask.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.harry.example.logintask.R
import com.harry.example.logintask.databinding.LoginActivityBinding
import com.harry.example.logintask.pojos.ApiResponse
import com.harry.example.logintask.utility.*
import com.harry.example.logintask.viewmodels.LoginViewModel
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class LoginActivity : AppCompatActivity() {
    private val loginViewModel: LoginViewModel by viewModel()
    private val networkConnectionChecker: NetworkConnectionChecker by inject()
    private var isNetworkAvailable: Boolean = false
    private var isLoggingUser: Boolean = false
    private var credentialsStored = false
    private var rememberMeIsChecked: Boolean = false
    private var username: String? = null
    private var password: String? = null


    private lateinit var loginActivityBinding: LoginActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        loginActivityBinding.listener = this
        savedInstanceState.let {
            if (savedInstanceState?.getBoolean(LOGGING_USER) == true && loginViewModel.isLoggingIn.value == true) {
                showProgress()
            }
        }
        loginActivityBinding.inputLayoutUsername.username.setOnFocusChangeListener { v, hasFocus ->
            if (loginActivityBinding.inputLayoutUsername.isErrorEnabled)
                loginActivityBinding.inputLayoutUsername.isErrorEnabled = false
        }
        loginActivityBinding.inputLayoutPassword.password.setOnFocusChangeListener { v, hasFocus ->
            if (loginActivityBinding.inputLayoutPassword.isErrorEnabled)
                loginActivityBinding.inputLayoutPassword.isErrorEnabled = false
        }
        observeNetwork()
        observeUserLogin()
        observeResponse()
        loginActivityBinding.rememberMe.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                rememberMeIsChecked = true
            } else {
                rememberMeIsChecked = false
            }
        }
    }


    fun loginUser() {
        username = loginActivityBinding.username.text.toString().trim()
        password = loginActivityBinding.password.text.toString().trim()
        if (username.isNullOrEmpty() || username.isNullOrBlank()) {
            loginActivityBinding.inputLayoutUsername.error = CANNOT_BE_EMPTY
            return
        }
        if (password.isNullOrEmpty() || password.isNullOrBlank()) {
            loginActivityBinding.inputLayoutPassword.error = CANNOT_BE_EMPTY
            return
        }
        if (isNetworkAvailable) {
            loginViewModel.loginUser(username, password)
        } else {
            Utility.showMessage(applicationContext, isNetworkAvailable)
        }
    }

    private fun observeNetwork() {
        networkConnectionChecker.observe(this, Observer {
            when {
                it == true -> {
                    isNetworkAvailable = true
                }
                else -> {
                    isNetworkAvailable = false
                    hideProgress()
                }
            }
            Utility.showMessage(applicationContext, isNetworkAvailable)
        })
    }

    private fun observeUserLogin() {
        loginViewModel.isLoggingIn.observe(this, Observer {
            when {
                it == true -> {
                    isLoggingUser = true
                    showProgress()
                }
                else -> {
                    isLoggingUser = false
                    hideProgress()
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(LOGGING_USER, isLoggingUser)
    }

    private fun showProgress() {
        loginActivityBinding.loginProgress.visibility = View.VISIBLE
        disableOrEnable(false)
    }

    private fun hideProgress() {
        loginActivityBinding.loginProgress.visibility = View.INVISIBLE
        disableOrEnable(true)
    }

    private fun disableOrEnable(res: Boolean) {
        loginActivityBinding.username.isEnabled = res
        loginActivityBinding.password.isEnabled = res
        loginActivityBinding.login.isEnabled = res
        loginActivityBinding.rememberMe.isEnabled = res
    }

    private fun observeResponse() {
        loginViewModel.loginResponse.observe(this, Observer {
            if (it?.data != null) {
                clearViews()
                if (rememberMeIsChecked) {
                    Log.i("TAG", "STORED")
                    storeCredentials(username, password)
                }
                toHomeActivity(it)
            } else {
                Toast.makeText(applicationContext, INVALID_USER, Toast.LENGTH_LONG).show()
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

    private fun clearViews() {
        loginActivityBinding.username.text = emptyEditable()
        loginActivityBinding.username.text = emptyEditable()
    }

    private fun emptyEditable(): Editable {
        return Editable.Factory.getInstance().newEditable("")
    }

    fun storeCredentials(username: String?, password: String?) {
        CoroutineScope(Default).launch {
            if (!username.isNullOrEmpty() && !password.isNullOrEmpty()) {
                val sharedPreferences: SharedPreferences =
                    getSharedPreferences(USER_CREDENTIALS, Context.MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                Log.i("TAG", "storeCredentials: ${username} ${password}")
                editor.putString(USERNAME, username)
                editor.putString(PASSWORD, password)
                editor.apply()
                credentialsStored = true
            }
        }

    }
}