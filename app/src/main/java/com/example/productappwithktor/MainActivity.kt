package com.example.productappwithktor

import ApiService
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.example.productappwithktor.model.Categories
import com.example.productappwithktor.model.Product
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
            val productList = remember { mutableStateListOf<Product>() }

            LaunchedEffect(Unit) {
                try {
                    val categories = withContext(Dispatchers.IO) {
                        apiService.getCategory()
                    }
                    val products = withContext(Dispatchers.IO) {
                        apiService.getProduct()
                    }
                    productList.addAll(products.products)
                    categoryList.addAll(categories.map { it.removeSurrounding("\"") })
                    Log.d("CategoryList", categoryList.toString())
                    Log.d("ProductList", productList.toString())
                } catch (e: Exception) {
                    Log.e("CategoryError", e.message ?: "Unknown error occurred")
                }
            }

            ProductAppWithKtorTheme {
                Surface(color = MaterialTheme.colors.background) {
                    if (categoryList.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(productList) { product ->
                                CardView(product = product)
                            }
                        }
                    } else {
                        ErrorView()
                    }
                }
            }
        }
    }

//    @Composable
//    fun CardView(category: String) {
//        Card(elevation = 10.dp, modifier = Modifier
//            .fillMaxSize()
//            .padding(10.dp)) {
//            Column(modifier = Modifier.padding(10.dp)) {
//                Text(
//                    text = category,
//                    modifier = Modifier.align(Alignment.CenterHorizontally)
//                )
//            }
//        }
//    }
    @Composable
fun CardView(product: Product) {
        Card(
            elevation = 10.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberImagePainter(product.thumbnail),
                    contentDescription = null,
                    modifier = Modifier
                        .width(100.dp)
                        .height(100.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.weight(2f).padding(start = 10.dp)) {
                    Text(
                        text = product.title,
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Text(
                        text = "Price: $${product.price}",
                        modifier = Modifier.align(Alignment.Start)
                    )
                    Text(
                        text = "Rating: ${product.rating}",
                        modifier = Modifier.align(Alignment.Start)
                    )
                }
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

//    @Preview(showBackground = true)
//    @Composable
//    fun DefaultPreview() {
//        ProductAppWithKtorTheme {
//            CardView(category = "Sample Category")
//        }
//    }
}
