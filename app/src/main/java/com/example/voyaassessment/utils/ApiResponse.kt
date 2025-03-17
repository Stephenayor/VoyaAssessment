package com.example.voyaassessment.utils

sealed class ApiResponse<out T> {
    data object Idle : ApiResponse<Nothing>()
    data object Loading: ApiResponse<Nothing>()

    data class Success<out T>(
        val data: T?
    ): ApiResponse<T>()

    data class Failure(
        val e: Exception?,
        val message: String?
    ): ApiResponse<Nothing>()


}