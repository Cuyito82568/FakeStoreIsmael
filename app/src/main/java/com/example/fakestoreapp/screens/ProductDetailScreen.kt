package com.example.fakestoreapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.fakestoreapp.models.Product
import com.example.fakestoreapp.services.ProductService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Composable
fun ProductDetailScreen(id: Int) {
    val BASE_URL = "https://fakestoreapi.com/"
    var product by remember { mutableStateOf<Product?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(key1 = true) {
        try {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val result = async(Dispatchers.IO) {
                val productsService = retrofit.create(ProductService::class.java)
                productsService.getProductById(id)
            }
            product = result.await()
            isLoading = false
        } catch (e: Exception) {
            isLoading = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF121212))
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = Color(0xFF6C63FF)
            )
        } else if (product != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                // Imagen grande
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                        .background(Color(0xFFF5F5F5))
                ) {
                    AsyncImage(
                        model = product!!.image,
                        contentDescription = product!!.title,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                // Contenido
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                        .background(Color(0xFF1E1E2E))
                        .padding(20.dp)
                ) {
                    // Categoría
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .background(Color(0xFF6C63FF).copy(alpha = 0.2f))
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = product!!.category.uppercase(),
                            color = Color(0xFF6C63FF),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Título
                    Text(
                        text = product!!.title,
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 28.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Precio y Rating en fila
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "$${product!!.price}",
                            color = Color(0xFF6C63FF),
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )

                        // Rating box
                        Column(horizontalAlignment = Alignment.End) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                repeat(5) { index ->
                                    Icon(
                                        imageVector = Icons.Default.Star,
                                        contentDescription = null,
                                        tint = if (index < product!!.rating.rate.toInt())
                                            Color(0xFFFFD700) else Color(0xFF444444),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "${product!!.rating.rate} / 5.0",
                                color = Color(0xFFAAAAAA),
                                fontSize = 12.sp
                            )
                            Text(
                                text = "${product!!.rating.count} reseñas",
                                color = Color(0xFF777777),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                    Divider(color = Color(0xFF2E2E3E))
                    Spacer(modifier = Modifier.height(20.dp))

                    // Descripción
                    Text(
                        text = "Descripción",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = product!!.description,
                        color = Color(0xFFAAAAAA),
                        fontSize = 14.sp,
                        lineHeight = 22.sp,
                        textAlign = TextAlign.Justify
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Botón agregar al carrito (decorativo)
                    Button(
                        onClick = {},
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF6C63FF)
                        )
                    ) {
                        Text(
                            text = "Agregar al carrito",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
    }
}