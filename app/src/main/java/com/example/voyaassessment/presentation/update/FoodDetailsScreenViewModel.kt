package com.example.voyaassessment.presentation.update

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.voyaassessment.data.model.remote.FoodDetails
import com.example.voyaassessment.domain.MealHomeRepository
import com.example.voyaassessment.utils.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoodDetailsScreenViewModel @Inject constructor(
    private val mealHomeRepository: MealHomeRepository
): ViewModel() {

    private val _foodDetails = MutableStateFlow<ApiResponse<FoodDetails>>(ApiResponse.Idle)
    val foodDetailsState: StateFlow<ApiResponse<FoodDetails>> = _foodDetails


    fun getFoodDetails(foodID: Int) {
        _foodDetails.value = ApiResponse.Loading
        viewModelScope.launch {
            mealHomeRepository.getFoodDetails(foodID).collect{response ->
                _foodDetails.value = response
            }
        }
    }

    fun clearLoadingState() {
        _foodDetails.value = ApiResponse.Idle
    }
}