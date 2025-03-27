package com.example.voyaassessment.data.repository

import android.util.Log
import com.example.voyaassessment.data.WebService
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.data.model.remote.FoodDetails
import com.example.voyaassessment.domain.MealHomeRepository
import com.example.voyaassessment.utils.ApiResponse
import com.example.voyaassessment.utils.Tools
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import javax.inject.Inject

class MealHomeRepositoryImpl @Inject constructor(
    private val webService: WebService
) : MealHomeRepository {
    override suspend fun getTags() = flow {
        emit(ApiResponse.Loading)
        try {
            val response = webService.getTags()
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, null))
        }
    }

    override suspend fun getCategories(): Flow<ApiResponse<Categories>> = flow {
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

    override suspend fun getFoodDetails(foodID: Int): Flow<ApiResponse<FoodDetails>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = webService.getFoodDetails(foodID)
            if (response.isSuccessful) {
                emit(ApiResponse.Success(response.body()))
            } else {
                val errorMessage = handleError(response)
                emit(
                    ApiResponse.Failure(
                        error = Exception("Error code: ${response.code()}"),
                        message = errorMessage,
                        errorCode = response.code()
                    )
                )
            }
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    private fun <T : Any?> handleError(response: Response<T>): String? {
        val errorBody = response.errorBody()?.string()
        val errorMessage = if (!errorBody.isNullOrEmpty()) {
            try {
                val errorResponse = Tools.handleErrorResponse(errorBody)
                errorResponse?.message
            } catch (e: Exception) {
                response.message()
            }
        } else {
            response.message()
        }
        return errorMessage
    }


}