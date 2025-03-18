package com.example.voyaassessment.data.model.remote.request

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody

data class CreateFood(
    val name: String,
    val description: String,
    @SerializedName("category_id") val categoryId: Int,
    val calories: Int,
    val tags: List<MultipartBody.Part>,
    val images: List<MultipartBody.Part>
)
