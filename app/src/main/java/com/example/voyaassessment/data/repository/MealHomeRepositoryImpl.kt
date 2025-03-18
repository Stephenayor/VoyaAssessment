package com.example.voyaassessment.data.repository

import com.example.voyaassessment.data.WebService
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.domain.MealHomeRepository
import com.example.voyaassessment.utils.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class MealHomeRepositoryImpl @Inject constructor(
    private val webService: WebService
): MealHomeRepository {
    override suspend fun getTags() = flow {
        emit(ApiResponse.Loading)
        try {
            val response = webService.getTags()
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, null))
        }
    }

    override suspend fun getCategories(): Flow<ApiResponse<Categories>> = flow{
        emit(ApiResponse.Loading)
        try {
            val response = webService.getCategories()
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    override suspend fun getAllFood(): Flow<ApiResponse<Food>> = flow {
        val response = webService.getFood()
        emit(ApiResponse.Loading)
        try {
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, response.message))
        }
    }
}