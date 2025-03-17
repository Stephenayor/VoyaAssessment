package com.example.voyaassessment.data.model.remote

import com.google.gson.annotations.SerializedName


data class Category (

  @SerializedName("id"          ) var id          : Int?    = null,
  @SerializedName("name"        ) var name        : String? = null,
  @SerializedName("description" ) var description : String? = null,
  @SerializedName("created_at"  ) var createdAt   : String? = null,
  @SerializedName("updated_at"  ) var updatedAt   : String? = null

)