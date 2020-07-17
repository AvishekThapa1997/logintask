package com.harry.example.logintask.pojos

import com.google.gson.annotations.SerializedName

data class ApiResponse(@SerializedName("data") val data: ApiData) {
    data class ApiData(
        @SerializedName("employer_name") val employee_name: String,
        @SerializedName("company_name") val company_name: String,
        @SerializedName("notification_filter") val notificationFilter: List<NotificationFilter>
    ) {
        data class NotificationFilter(@SerializedName("notification_filter_id") val notification_filter_id: String)
    }
}