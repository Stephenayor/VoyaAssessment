package com.example.voyaassessment.data

import com.example.voyaassessment.data.model.Tags
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.utils.ApiResponse
import retrofit2.http.GET

interface WebService {
    @GET("api/tags")
    suspend fun getTags(): Tags

    @GET("api/categories")
    suspend fun getCategories(): Categories

    @GET("api/foods")
    suspend fun getFood(): Food
}