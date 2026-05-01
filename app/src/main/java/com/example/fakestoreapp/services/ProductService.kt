package com.example.fakestoreapp.services

import com.example.fakestoreapp.models.Product
import retrofit2.http.GET
import retrofit2.http.Path

interface ProductService {

    @GET("products")
    suspend fun getAllProducts() : List<Product>

    /*
    * Path ---> /2
    * QueryString ---> ?name=asdasd&apellido=sadasd
    * Body ---> {} /XML, POST PUT PATCH DELETE
    * */
    @GET("products/{id}")
    suspend fun getProductById( @Path("id") id: Int) : Product
}