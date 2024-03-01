package com.hrithikvish.ecommerce

import android.app.Application
import com.hrithikvish.ecommerce.api.ProductService
import com.hrithikvish.ecommerce.api.RetrofitHelper
import com.hrithikvish.ecommerce.repo.ProductRepo

class ECommerceApplication : Application() {
    lateinit var productRepo : ProductRepo

    override fun onCreate() {
        super.onCreate()
        init()
    }

    private fun init() {
        val productService = RetrofitHelper.getRFInstance().create(ProductService::class.java)
        productRepo = ProductRepo(productService, applicationContext)
    }

}