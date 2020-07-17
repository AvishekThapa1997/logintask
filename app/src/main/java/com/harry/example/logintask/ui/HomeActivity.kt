package com.harry.example.logintask.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.harry.example.logintask.R
import com.harry.example.logintask.databinding.HomeActivityBinding
import com.harry.example.logintask.utility.COMPANY_NAME
import com.harry.example.logintask.utility.EMPLOYEE_NAME
import com.harry.example.logintask.utility.NOTIFICATION_FILTER_ID

class HomeActivity : AppCompatActivity() {
    lateinit var homeActivityBinding: HomeActivityBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeActivityBinding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        val intent = intent
        val employee_name = intent.getStringExtra(EMPLOYEE_NAME)
        val company_name = intent.getStringExtra(COMPANY_NAME)
        val notification_filter_id = intent.getStringExtra(NOTIFICATION_FILTER_ID)
        homeActivityBinding.setEmployeeName(employee_name)
        homeActivityBinding.setCompanyName(company_name)
        homeActivityBinding.notificationFilterId = notification_filter_id
    }
}