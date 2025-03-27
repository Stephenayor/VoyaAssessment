package com.example.voyaassessment.presentation.update

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.voyaassessment.R
import com.example.voyaassessment.data.model.remote.FoodDetails
import com.example.voyaassessment.utils.ApiResponse
import com.example.voyaassessment.utils.Route
import com.example.voyaassessment.utils.Tools
import com.example.voyaassessment.utils.dialogs.CustomAlertDialog

@Composable
fun FoodDetailsScreen(
    foodId: String, navController: NavController,
    foodDetailsScreenViewModel: FoodDetailsScreenViewModel = hiltViewModel()
) {
    val foodDetailsState by foodDetailsScreenViewModel.foodDetailsState.collectAsState()
    var foodDetails by remember { mutableStateOf<FoodDetails?>(null) }

    //UI States
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(true) }


    LaunchedEffect(Unit) {
        foodDetailsScreenViewModel.getFoodDetails(foodId.toInt())
    }

    when (val state = foodDetailsState) {
        is ApiResponse.Idle -> {

        }

        is ApiResponse.Loading -> {
            Column {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.CenterHorizontally)
                ) {

                    CircularProgressIndicator()

                }
            }

        }

        is ApiResponse.Success -> {
            foodDetails = state.data
        }

        is ApiResponse.Failure -> {
            showDialog = true
            isSuccess = false
            dialogMessage = state.message ?: "Something went wrong!"

        }
    }

    if (showDialog) {
        CustomAlertDialog(
            title = if (isSuccess) "Success" else "Error",
            message = dialogMessage,
            onDismiss = {
                Log.d("Dismiss", "onDismiss clicked")
                showDialog = false
            },
            onConfirm = {
                Log.d("Confirm", "onConfirm clicked")
                showDialog = false
            },
            isSuccess = isSuccess
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color.White)
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.navigate(Route.FOOD_HOME_SCREEN) }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Row {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite")
                }
                IconButton(onClick = { /* Handle edit action */ }) {
                    Icon(painterResource(R.drawable.ic_edit), contentDescription = "Edit")
                }
            }
        }

        // Food Image
        Box {
            AsyncImage(
                model = foodDetails?.data?.foodImages?.first()?.imageUrl,
                contentDescription = "Food Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
                    .clip(RoundedCornerShape(12.dp)),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .background(Color.Black.copy(alpha = 0.6f), RoundedCornerShape(12.dp))
                    .padding(6.dp)
            ) {
                Text("1/10", color = Color.White, fontSize = 12.sp)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Title & Tags
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = foodDetails?.data?.name.toString(),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.Monospace
            )

            Row(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                foodDetails?.data?.foodTags?.forEach { tag ->
                    TagItem2(tag)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Description
            Text(
                text = foodDetails?.data?.description.toString(),
                fontSize = 14.sp,
                color = Color(0xFF1D2433),
                fontFamily = FontFamily.SansSerif
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nutrition Section
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp),
            shape = RoundedCornerShape(4.dp),
            border = BorderStroke(1.dp, Color.LightGray),
            backgroundColor = Color(0xFFF9FAFB)
        ) {
            Text(
                text = "Nutrition",
                fontSize = 16.sp,
                modifier = Modifier.padding(start = 16.dp, top = 5.dp),
                fontWeight = FontWeight.Light,
                color = Color(0xFF707989)
            )
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(R.drawable.fire),
                    contentDescription = "Nutrition",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "${foodDetails?.data?.calories.toString()} Calories",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF707989)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Notes Section
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(text = "Notes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.clickable { },
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(painterResource(R.drawable.note), contentDescription = "Add notes")
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Add notes", color = Color(0xFF0D6EFD), fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Remove from Collection Button
        Button(
            onClick = { },
            modifier = Modifier
                .fillMaxWidth()
                .height(85.dp)
                .padding(horizontal = 5.dp, vertical = 20.dp),
            shape = RectangleShape,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
        ) {
            Text(text = "Remove from collection", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Tag Item
@Composable
fun TagItem(text: String) {
    Box(
        modifier = Modifier
            .background(Color.LightGray, RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(text = text, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun TagItem2(tag: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFBF1F1), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        androidx.compose.material3.Text(
            text = tag,
            color = Color(0xFF645D5D),
            fontSize = 12.sp
        )
    }
}


@Preview
@Composable
private fun FoodDetailsScreenPreview() {
    val navController = rememberNavController()
    FoodDetailsScreen("", navController)
}