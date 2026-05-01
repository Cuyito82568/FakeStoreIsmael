package com.example.fakestoreapp.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.fakestoreapp.components.ProductItem
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.models.productList
import com.example.fakestoreapp.services.ProductService
import com.example.fakestoreapp.ui.theme.FakeStoreAppTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductsScreen(
    innerPadding: PaddingValues = PaddingValues(10.dp),
    navController: NavController = rememberNavController()
){
    val BASE_URL = "https://fakestoreapi.com/"
    // que necesito una variable con by remember
    var products by remember {
        mutableStateOf(listOf<Product>())
    }
    var isLoading by remember {
        mutableStateOf(true)
    }
    //EFECTOS SECUNDARIOS
    LaunchedEffect(key1 = true) {
        try{
            // PATRON DE DISEÑO BUILDER / SOLID
            Log.i("ProductsScreen","Cargando la aplicacion")
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            Log.i("ProductsScreen","haciendo consulta a API $BASE_URL")

            val result = async(Dispatchers.IO) {
                val productService = retrofit.create(ProductService::class.java)
                productService.getAllProducts()
            }
            // APP INSIGHTS --> AZURE,, CLOUD WATCH ---> AWS, DATADOG
            products = result.await()
            isLoading = false
            Log.i("ProductsScreen",products.toString())

        }
        catch (e: Exception){
            Log.e("ProductsScreen",e.message.toString())
            isLoading = false
        }
    }

    if(isLoading){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{
        LazyVerticalGrid(
            modifier = Modifier
                .padding(innerPadding)
                .padding(10.dp)
                .fillMaxSize()
            ,
            columns = GridCells.Fixed(count = 2)
        ) {
            items(products){ product ->
                ProductItem(
                    product,
                    onClick = {
                        navController.navigate("products/${product.id}")
                    }
                )
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProductsScreenPreview(){
    FakeStoreAppTheme {
        ProductsScreen()
    }
}