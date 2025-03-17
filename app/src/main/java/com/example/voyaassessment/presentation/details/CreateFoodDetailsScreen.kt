package com.example.voyaassessment.presentation.details

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.voyaassessment.R
import com.example.voyaassessment.utils.Tools

@Composable
fun CreateFoodDetailsScreen(
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val context = LocalContext.current
    var selectedImages by remember { mutableStateOf(listOf<Uri>()) }
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var description by remember { mutableStateOf(TextFieldValue("")) }
    var calories by remember { mutableStateOf(TextFieldValue("")) }
    var tags by remember { mutableStateOf(TextFieldValue("")) }
    var selectedCategory by remember { mutableStateOf("") }
    var categories by remember { mutableStateOf(listOf<String>()) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    // Button is enabled if all required fields have valid input.
    val isButtonEnabled = name.text.isNotEmpty() &&
            description.text.isNotEmpty() &&
            tags.text.isNotEmpty() &&
//            selectedCategory.isNotEmpty() &&
            selectedImages.isNotEmpty()

    val launcherCamera =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = Tools.saveImageToInternalStorage(context, it)
                selectedImages = selectedImages + uri
            }
        }

    val launcherGallery =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let {
                selectedImages = selectedImages + it
            }
        }

    LaunchedEffect(Unit) {
        // Fetch categories from an API or any other source if needed
        // categories = fetchCategoriesFromApi()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .padding(top = 40.dp)
    ) {
        // Top bar with back icon and title
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {navController.popBackStack() }) {
                Icon(
                    painter = painterResource(R.drawable.arrow_square_right),
                    contentDescription = "Back",
                    tint = Color.Black,
                    modifier = Modifier.size(25.dp)
                )
            }
            Text(
                text = stringResource(R.string.addnewfood),
                fontFamily = FontFamily.SansSerif,
                color = Color(0xFF1D2433),
                modifier = Modifier.padding(start = 8.dp),
                fontSize = 20.sp,
            )
        }

        // Row for Camera and Upload Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherCamera.launch(null) }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color(0xFFE4E7EC)),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.camera),
                        contentDescription = "Take photo"
                    )
                    Text(
                        text = "Take photo",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }

            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { launcherGallery.launch("image/*") }
                    .padding(8.dp)
                    .height(90.dp),
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, Color.Gray),
                elevation = 4.dp
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.uploadsimple),
                        contentDescription = "Upload"
                    )
                    Text(
                        text = "Upload",
                        color = Color(0xFF1D2433),
                        modifier = Modifier.padding(top = 5.dp),
                        fontFamily = FontFamily.SansSerif
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Show selected images using a LazyRow
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(selectedImages) { uri ->
                Box(
                    modifier = Modifier
                        .size(90.dp)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(uri),
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .fillMaxSize()
                    )
                    IconButton(
                        onClick = { selectedImages = selectedImages - uri },
                        modifier = Modifier
                            .size(25.dp)
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 5.dp, end = 5.dp)
                    ) {
                        Image(
                            painter = painterResource(R.drawable.closeselectedimage),
                            contentDescription = "Remove Image"
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Name Field
        Text(
            text = "Name",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter food name") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Description Field
        Text(
            text = "Description",
            color = Color(0xFF1D2433),
            fontFamily = FontFamily.SansSerif,
            fontSize = 15.sp
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Enter food description") },
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp)
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Category Dropdown
        Text(text = "Category")
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    IconButton(onClick = { isDropdownExpanded = !isDropdownExpanded }) {
                        Icon(
                            painter = painterResource(R.drawable.ic_arrow_drop_down),
                            contentDescription = "Dropdown"
                        )
                    }
                }
            )
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(onClick = {
                        selectedCategory = category
                        isDropdownExpanded = false
                    }) {
                        Text(text = category)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        // Calories Field
        Text(text = "Calories")
        OutlinedTextField(
            value = calories,
            onValueChange = { calories = it },
            label = { Text("Enter number of Calories") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(20.dp))

        // Tags Field
        Text(text = "Tags")
        OutlinedTextField(
            value = tags,
            onValueChange = { tags = it },
            label = { Text("Add a Tag") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Add Food Button with conditional colors:
        Button(
            onClick = { /* Handle food submission */ },
            enabled = isButtonEnabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isButtonEnabled) Color(0xFF0D6EFD) else Color(0xFF0E7F0FF),
                contentColor = if (isButtonEnabled) Color(0xFFFFFFFF) else Color(0xFF98A2B3)
            ),
            shape = RectangleShape,
            modifier = Modifier
                .fillMaxWidth()
                .height(65.dp)
                .padding(top = 25.dp, bottom = 16.dp)
        ) {
            Text(text = "Add Food")
        }
    }
}



@Composable
fun CategoryDropdown(selectedCategory: String, categories: List<String>, onCategorySelected: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxWidth()) {
        TextField(
            value = selectedCategory,
            onValueChange = {},
            label = { Text("Category") },
            readOnly = true,
            trailingIcon = {
                Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null, Modifier.clickable { expanded = true })
            },
            modifier = Modifier.fillMaxWidth()
        )
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            categories.forEach { category ->
                DropdownMenuItem(onClick = {
                    onCategorySelected(category)
                    expanded = false
                }) {
                    Text(text = category)
                }
            }
        }
    }
}






@Preview
@Composable
fun CreateFoodDetailsScreenPreview(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    CreateFoodDetailsScreen(modifier, navController = navController)
}
