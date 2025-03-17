package com.example.voyaassessment.utils

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.Response

@Module
@InstallIn(SingletonComponent::class)
class RequestInterceptor : Interceptor {
  override fun intercept(chain: Interceptor.Chain): Response {
    val originalRequest = chain.request()
    val originalUrl = originalRequest.url
    val url = originalUrl.newBuilder()
      .build()
    val requestBuilder = originalRequest.newBuilder().url(url)
    val request = requestBuilder.build()
    return chain.proceed(request)
  }
}