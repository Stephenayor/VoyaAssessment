package com.example.voyaassessment.data

import com.example.voyaassessment.data.model.Tags
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.CreateFoodResponse
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.data.model.remote.FoodDetails
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface WebService {
    @GET("api/tags")
    suspend fun getTags(): Tags

    @GET("api/categories")
    suspend fun getCategories(): Categories

    @GET("api/foods")
    suspend fun getFood(): Food

    @GET("api/foods/" +"{id}")
    suspend fun getFoodDetails(@Path("id") foodId: Int): Response<FoodDetails>


    @Multipart
    @POST("api/foods")
    suspend fun createFood(
        @Part("name") name: RequestBody,
        @Part("description") description: RequestBody,
        @Part("category_id") categoryId: RequestBody,
        @Part("calories") calories: RequestBody,
        @Part tags: List<MultipartBody.Part>,
        @Part images: List<MultipartBody.Part>
    ): Response<CreateFoodResponse>
}