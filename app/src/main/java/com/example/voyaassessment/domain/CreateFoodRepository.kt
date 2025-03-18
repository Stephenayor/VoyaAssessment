package com.example.voyaassessment.domain

import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.CreateFoodResponse
import com.example.voyaassessment.data.model.remote.request.CreateFood
import com.example.voyaassessment.utils.ApiResponse
import kotlinx.coroutines.flow.Flow

interface CreateFoodRepository {

    suspend fun getCategories(): Flow<ApiResponse<Categories>>

    suspend fun createFood(createFood: CreateFood): Flow<ApiResponse<CreateFoodResponse>>
}