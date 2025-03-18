package com.example.voyaassessment.data.repository

import com.example.voyaassessment.data.WebService
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.CreateFoodResponse
import com.example.voyaassessment.data.model.remote.request.CreateFood
import com.example.voyaassessment.domain.CreateFoodRepository
import com.example.voyaassessment.utils.ApiResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class CreateFoodRepositoryImpl @Inject constructor(
    private val webService: WebService
) : CreateFoodRepository {

    override suspend fun getCategories(): Flow<ApiResponse<Categories>> = flow {
        emit(ApiResponse.Loading)
        try {
            val response = webService.getCategories()
            emit(ApiResponse.Success(response))
        } catch (e: Exception) {
            emit(ApiResponse.Failure(e, e.message))
        }
    }

    override suspend fun createFood(createFood: CreateFood): Flow<ApiResponse<CreateFoodResponse>> =
        flow {
            emit(ApiResponse.Loading)
            val tags = listOf(1, 2, 3)
            val tagsParts = tags.map { tag ->
                MultipartBody.Part.createFormData("tags[]", tag.toString())
            }
            try {
                val response = webService.createFood(
                    name = createFood.name.toRequestBody(),
                    description = createFood.description.toRequestBody(),
                    categoryId = createFood.categoryId.toString().toRequestBody(),
                    calories = createFood.calories.toString().toRequestBody(),
                    tags = tagsParts,
                    images = createFood.images
                )
                emit(ApiResponse.Success(response))
            } catch (e: Exception) {
                emit(ApiResponse.Failure(e, e.message))
            }
        }

    private fun String.toRequestBody(): RequestBody =
        RequestBody.create("text/plain".toMediaTypeOrNull(), this)

}