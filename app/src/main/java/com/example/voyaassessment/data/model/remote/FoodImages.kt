package com.example.voyaassessment.data.model.remote

import com.google.gson.annotations.SerializedName


data class FoodImages (

  @SerializedName("id"        ) var id       : Int?    = null,
  @SerializedName("image_url" ) var imageUrl : String? = null

)