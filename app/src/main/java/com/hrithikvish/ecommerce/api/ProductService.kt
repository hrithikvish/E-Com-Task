package com.hrithikvish.ecommerce.api

import com.hrithikvish.ecommerce.models.Product
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("253620")
    suspend fun getProducts() : Response<Product>

}