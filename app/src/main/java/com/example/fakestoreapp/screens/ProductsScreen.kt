package com.example.fakestoreapp.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fakestoreapp.components.ProductItem
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.services.ProductService
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductsScreen(
    innerPadding: PaddingValues = PaddingValues(0.dp),
    navController: NavController = rememberNavController()
) {
    val BASE_URL = "https://fakestoreapi.com/"
    var products by remember { mutableStateOf(listOf<Product>()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            Log.i("ProductsScreen", "Cargando productos...")
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async(Dispatchers.IO) {
                val productService = retrofit.create(ProductService::class.java)
                productService.getAllProducts()
            }
            products = result.await()
            isLoading = false
            Log.i("ProductsScreen", "Productos cargados: ${products.size}")
        } catch (e: Exception) {
            Log.e("ProductsScreen", e.message.toString())
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
            .padding(innerPadding)
    ) {
        // Header
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Text(
                text = "FakeStore",
                color = Color(0xFF6C63FF),
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = "Descubre nuestros productos",
                color = Color(0xFF888888),
                fontSize = 13.sp
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6C63FF))
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 8.dp),
                columns = GridCells.Fixed(count = 2)
            ) {
                items(products) { product ->
                    ProductItem(
                        product = product,
                        onClick = {
                            navController.navigate("products/${product.id}")
                        }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProductsScreenPreview() {
    FakeStoreAppTheme {
        ProductsScreen()
    }
}
