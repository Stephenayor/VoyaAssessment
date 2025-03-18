package com.example.voyaassessment.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.CreateFoodResponse
import com.example.voyaassessment.data.model.remote.request.CreateFood
import com.example.voyaassessment.domain.CreateFoodRepository
import com.example.voyaassessment.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

@HiltViewModel
class CreateFoodViewModel @Inject constructor(
    private val createFoodRepository: CreateFoodRepository
): ViewModel() {

    private val _categoriesState = MutableStateFlow<ApiResponse<Categories>>(ApiResponse.Idle)
    val categoriesState: StateFlow<ApiResponse<Categories>> = _categoriesState

    private val _createFoodState = MutableStateFlow<ApiResponse<CreateFoodResponse>>(ApiResponse.Idle)
    val createFoodState: StateFlow<ApiResponse<CreateFoodResponse>> = _createFoodState


    fun getCategories(){
        _categoriesState.value = ApiResponse.Loading
        viewModelScope.launch {
            createFoodRepository.getCategories().collect{response ->
                _categoriesState.value = response
            }

        }
    }

    fun createFood(name: String,
                   description: String,
                   categoryId: Int,
                   calories: Int,
                   tags: List<String>,
                   imageFiles: List<File>){

        val imageParts = imageFiles.mapIndexed { index, file ->
            prepareImagePart(file, "images[$index]")
        }

        val tagList = tags.mapIndexed { index, tag ->
            val tagRequestBody = tag.toRequestBody("text/plain".toMediaType())
            MultipartBody.Part.createFormData("tags[$index]", null, tagRequestBody)
        }

        val createFoodRequest = CreateFood(
            name = name,
            description = description,
            categoryId = categoryId,
            calories = calories,
            tags = tagList,
            images = imageParts
        )
        _createFoodState.value = ApiResponse.Loading
        viewModelScope.launch {
            createFoodRepository.createFood(createFoodRequest).collect{response ->
                _createFoodState.value = response
            }

        }
    }

    private fun prepareImagePart(file: File, partName: String): MultipartBody.Part {
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData(partName, file.name, requestFile)
    }



}