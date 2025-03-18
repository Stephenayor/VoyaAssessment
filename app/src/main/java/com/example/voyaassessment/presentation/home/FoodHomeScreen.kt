package com.example.voyaassessment.presentation.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.voyaassessment.R
import com.example.voyaassessment.data.model.remote.Categories
import com.example.voyaassessment.data.model.remote.Food
import com.example.voyaassessment.utils.ApiResponse
import com.example.voyaassessment.utils.CustomLoadingBar
import com.example.voyaassessment.utils.Route
import com.example.voyaassessment.utils.dialogs.CustomAlertDialog


@Composable
fun FoodHomeScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    foodHomeViewModel: FoodHomeViewModel = hiltViewModel()
) {
    val categoriesState by foodHomeViewModel.categoriesState.collectAsState()
    val allFoodState by foodHomeViewModel.allFoodState.collectAsState()
    // Categories
    var categoriesList by remember { mutableStateOf(emptyList<Categories.CategoriesData>()) }
    var mainCategoriesList = listOf<Categories.CategoriesData>()

    // Foods
    var foodsList by remember { mutableStateOf(emptyList<Food.FoodData>()) }
    var filteredFoodsList by remember { mutableStateOf(emptyList<Food.FoodData>()) }

    // UI states
    var isLoading by remember { mutableStateOf(false) }
    var isLoadingCategories by remember { mutableStateOf(false) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var isSuccess by remember { mutableStateOf(true) }
    var searchQuery by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var showRetryText by remember { mutableStateOf(false) }


    Scaffold(
        bottomBar = { BottomNavigationBar() },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(Route.CREATE_FOOD_DETAILS_SCREEN)
                },
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add"
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

            LaunchedEffect(Unit) {
                foodHomeViewModel.getCategories()
            }

            when (val state = categoriesState) {
                is ApiResponse.Idle -> {

                }

                is ApiResponse.Loading -> {
                    isLoadingCategories = true

                }

                is ApiResponse.Success -> {
                    isLoadingCategories = false
                    categoriesList = state.data?.data!!
                    mainCategoriesList = categoriesList
                    foodHomeViewModel.getAllFood()

                }

                is ApiResponse.Failure -> {
                    isLoadingCategories = false
                    errorMessage = state.message ?: "An error occurred"
                    showRetryText = true
                }
            }


            if (isLoading) {
                CustomLoadingBar(
                    "Please wait...",
                    imageResId = R.drawable.loading
                )
                foodHomeViewModel.clearLoadingState()
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.useravatar),
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(width = 42.dp, height = 42.dp)
                )

                Image(
                    painter = painterResource(id = R.drawable.notification_icon),
                    contentDescription = "Notification icon",
                    modifier = Modifier.size(width = 50.dp, height = 42.dp)
                )
            }

            Column(modifier = Modifier.padding(16.dp)) {
                var selectedCategory by remember {
                    mutableStateOf(
                        if (categoriesList.isNotEmpty()) categoriesList.first()
                        else "All"
                    )
                }


                Text(
                    text = "Hey there, Lucy!",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D2433)
                )
                Text(
                    text = "Are you excited to create a tasty dish today?",
                    fontSize = 16.sp,
                    modifier = Modifier.padding(top = 5.dp),
                    color = Color.LightGray,
                    fontFamily = FontFamily.SansSerif
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { newQuery ->
                        searchQuery = newQuery
                        filteredFoodsList = if (newQuery.isEmpty()) {
                            foodsList
                        } else {
                            foodsList.filter {
                                it.name?.contains(newQuery, ignoreCase = true) == true
                            }
                        }

                        val filtered = categoriesList.filter {
                            it.name?.contains(
                                newQuery,
                                ignoreCase = true
                            ) == true
                        }
                        categoriesList = if (newQuery.isEmpty()) mainCategoriesList else filtered
                    },
                    placeholder = {
                        Text(
                            "Search foods...",
                            fontFamily = FontFamily.SansSerif,
                            color = Color(0xFF9D9EA1)

                        )
                    },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = "Search Icon")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF5F6FA), shape = RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(categoriesList) { category ->
                        val isSelected = selectedCategory == category
                        Button(
                            onClick = { selectedCategory = category },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isSelected) Color(0xFF3D6CE7) else Color.White,
                                contentColor = if (isSelected) Color.White else Color.LightGray
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier
                                .height(40.dp)
                                .padding(vertical = 4.dp)
                        ) {
                            Text(
                                text = category.name.toString(),
                                fontFamily = FontFamily.SansSerif
                            )
                        }
                    }
                }

                if (isLoadingCategories) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    foodHomeViewModel.clearLoadingState()
                }


                if (showRetryText) {
                    val annotatedText = buildAnnotatedString {
                        append(errorMessage)
                        append("\n")
                        pushStringAnnotation(tag = "retry", annotation = "retry")
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append("Please Retry")
                        }
                        pop()
                    }

                    ClickableText(
                        text = annotatedText,
                        onClick = { offset ->
                            // Check if the click happened on the "Please Retry" annotation
                            annotatedText.getStringAnnotations(
                                tag = "retry",
                                start = offset,
                                end = offset
                            )
                                .firstOrNull()?.let {
                                    // Trigger the retry action
                                    foodHomeViewModel.getCategories()
                                }
                        },
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp)
                    )
                }

                when (val foodState = allFoodState) {
                    is ApiResponse.Idle -> {

                    }

                    is ApiResponse.Loading -> {
                        isLoading = true
                    }

                    is ApiResponse.Success -> {
                        isLoading = false
                        foodsList = foodState.data?.data ?: emptyList()
                        filteredFoodsList = foodsList
//                        FoodListScreen(filteredFoodsList)
                    }

                    is ApiResponse.Failure -> {
                        isLoading = false
                        dialogMessage = foodState.message ?: "Something went wrong!"
                        isSuccess = false
                        showDialog = true

                    }
                }


                FoodListScreen(filteredFoodsList)

                if (showDialog) {
                    CustomAlertDialog(
                        title = if (isSuccess) "Success" else "Error",
                        message = dialogMessage,
                        onDismiss = { showDialog = false },
                        onConfirm = { showDialog = false },
                        isSuccess = isSuccess
                    )
                }
            }
        }
    }
}


@Composable
fun FoodListScreen(allFoodState: List<Food.FoodData>) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(6.dp)
    ) {
        Column {
            Text(
                text = "All Foods",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                color = Color.Black,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(allFoodState) { food ->
                    FoodCard(food)
                }
            }
        }
    }
}

@Composable
fun FoodCard(food: Food.FoodData) {
    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(0.5.dp, Color(0xFFE0E0E0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            AsyncImage(
                model = food.foodImages.first().imageUrl,
                contentDescription = food.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
            )

            Column(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = food.name.toString(),
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = FontFamily.Default,
                        fontSize = 18.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_heart),
                        contentDescription = "Favourite",
                        tint = Color.LightGray,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.fire),
                        contentDescription = "Calories",
                        tint = Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = " ${food.calories} Calories",
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                }

                Text(
                    text = food.description.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 4,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 16.dp),
                    color = Color.Gray
                )

                Row(
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    food.foodTags.forEach { tag ->
                        TagItem(tag)
                    }
                }
            }
        }
    }
}

@Composable
fun TagItem(tag: String) {
    Box(
        modifier = Modifier
            .background(Color(0xFFFBF1F1), shape = RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = tag,
            color = Color(0xFF645D5D),
            fontSize = 12.sp
        )
    }
}




@Composable
fun BottomNavigationBar() {
    var selectedItem by remember { mutableIntStateOf(0) }
    val items = listOf("Home", "Generator", "Add", "Favourite", "Planner")
    val icons = listOf(
        rememberVectorPainter(image = Icons.Default.Home),
        painterResource(id = R.drawable.magicpen),
        painterResource(id = R.drawable.pluscircle),
        painterResource(id = R.drawable.heart),
        painterResource(id = R.drawable.calendar)
    )

    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
                onClick = { selectedItem = index },
                icon = {
                    Icon(
                        painter = icons[index],
                        contentDescription = item,
                        tint = if (selectedItem == index) Color(0xFF0D6EFD) else Color.Gray
                    )
                },
                label = {
                    Text(
                        text = item,
                        color = if (selectedItem == index) Color(0xFF0D6EFD) else Color.Gray
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Blue,
                    unselectedIconColor = Color.Gray,
                    selectedTextColor = Color.Blue,
                    unselectedTextColor = Color.Gray,
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
}



@Preview
@Composable
fun FoodHomeScreenPreview(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    FoodHomeScreen(modifier, navController)
}