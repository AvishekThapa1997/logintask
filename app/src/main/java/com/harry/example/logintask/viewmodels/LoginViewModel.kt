package com.harry.example.logintask.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.harry.example.logintask.pojos.ApiResponse
import com.harry.example.logintask.repository.AppRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(
    private val mapplication: Application,
    private val repository: AppRepository?
) : AndroidViewModel(mapplication) {
    var isLoggingIn: LiveData<Boolean> = MutableLiveData()
    var loginResponse: LiveData<ApiResponse?> = MutableLiveData()
    fun loginUser(username: String?, password: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            (isLoggingIn as MutableLiveData<Boolean>).postValue(true)
            val response = repository?.loginUser(username, password)
            (loginResponse as MutableLiveData<ApiResponse?>).postValue(response)
            (isLoggingIn as MutableLiveData<Boolean>).postValue(false)
        }
    }
}