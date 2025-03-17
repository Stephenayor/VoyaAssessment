package com.example.voyaassessment.data.model

import com.example.voyaassessment.data.model.remote.Data
import com.google.gson.annotations.SerializedName

data class Tags (
  @SerializedName("status"  ) var status  : String?         = null,
  @SerializedName("message" ) var message : String?         = null,
  @SerializedName("data"    ) var data    : ArrayList<Data> = arrayListOf()

)