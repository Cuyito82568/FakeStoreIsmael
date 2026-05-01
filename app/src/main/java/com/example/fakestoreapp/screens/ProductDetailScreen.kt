package com.example.fakestoreapp.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.services.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductDetailScreen(id:Int){
    val BASE_URL = "https://fakestoreapi.com/"
    var product by remember {
        mutableStateOf<Product?>(null)
    }

    LaunchedEffect(key1 = true) {
        try{
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async(Dispatchers.IO) {
                val productsService = retrofit.create(ProductService::class.java)
                productsService.getProductById(id)
            }
            product = result.await()
        }
        catch (e: Exception){

        }
    }

    if(product != null){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = product!!.title
            )
        }
    }
}