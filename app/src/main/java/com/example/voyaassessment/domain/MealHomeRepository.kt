package com.example.voyaassessment.domain

import com.example.voyaassessment.data.model.Tags
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface MealHomeRepository {

    suspend fun getTags(): Flow<ApiResponse<Tags>>

    suspend fun getCategories(): Flow<ApiResponse<Categories>>

    suspend fun getAllFood(): Flow<ApiResponse<Food>>
}