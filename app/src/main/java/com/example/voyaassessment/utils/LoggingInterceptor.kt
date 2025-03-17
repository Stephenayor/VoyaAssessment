package com.example.voyaassessment.utils

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

class LoggingInterceptor {

    val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // Logs URL, headers, and body
    }

    val client = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()

}