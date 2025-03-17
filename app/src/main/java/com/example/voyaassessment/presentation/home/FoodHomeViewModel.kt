package com.example.voyaassessment.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyaassessment.data.model.Tags
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.domain.MealHomeRepository
import com.example.voyaassessment.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodHomeViewModel @Inject constructor(
    private val mealHomeRepository: MealHomeRepository
): ViewModel() {

    private val _getTags = MutableStateFlow<ApiResponse<Tags>>(ApiResponse.Idle)
    val getTags: StateFlow<ApiResponse<Tags>> = _getTags

    private val _categoriesState = MutableStateFlow<ApiResponse<Categories>>(ApiResponse.Idle)
    val categoriesState: StateFlow<ApiResponse<Categories>> = _categoriesState

    private val _allFoodState = MutableStateFlow<ApiResponse<Food>>(ApiResponse.Idle)
    val allFoodState: StateFlow<ApiResponse<Food>> = _allFoodState

    fun getTags() {
        _getTags.value = ApiResponse.Loading
        viewModelScope.launch {
            mealHomeRepository.getTags().collect { response ->
                _getTags.value = response
            }
        }
    }

    fun getCategories() {
        _categoriesState.value = ApiResponse.Loading
        viewModelScope.launch {
            mealHomeRepository.getCategories().collect { response ->
                _categoriesState.value = response
            }
        }
    }

    fun getAllFood(){
        _allFoodState.value = ApiResponse.Loading
        viewModelScope.launch {
            mealHomeRepository.getAllFood().collect { response ->
                _allFoodState.value = response
            }
        }
    }

    fun clearLoadingState() {
        _allFoodState.value = ApiResponse.Idle
        _categoriesState.value = ApiResponse.Idle
    }
}