package com.example.productappwithktor

import ApiService
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.productappwithktor.model.Categories
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.example.productappwithktor.ui.theme.ProductAppWithKtorTheme

class MainActivity : ComponentActivity() {
    private val apiService: ApiService = ApiService.create()

    @SuppressLint("ProduceStateDoesNotAssignValue")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val categoryList = remember { mutableStateListOf<String>() }

            LaunchedEffect(Unit) {
                try {
                    val categories = withContext(Dispatchers.IO) {
                        apiService.getCategory()
                    }
                    categoryList.addAll(categories.map { it.removeSurrounding("\"") })
                    Log.d("CategoryList", categoryList.toString())
                } catch (e: Exception) {
                    Log.e("CategoryError", e.message ?: "Unknown error occurred")
                }
            }

            ProductAppWithKtorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    if (categoryList.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(categoryList) { category ->
                                CardView(category = category)
                            }
                        }
                    } else {
                        ErrorView()
                    }
                }
            }
        }
    }

    @Composable
    fun CardView(category: String) {
        Card(elevation = 10.dp, modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)) {
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = category,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }

    @Composable
    fun ErrorView() {
        Text(
            text = "Error loading categories",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            style = MaterialTheme.typography.h6
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        ProductAppWithKtorTheme {
            CardView(category = "Sample Category")
        }
    }
}
