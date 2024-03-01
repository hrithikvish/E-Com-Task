package com.hrithikvish.ecommerce.repo

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.hrithikvish.ecommerce.api.ProductService
import com.hrithikvish.ecommerce.models.Product

class ProductRepo (private val productService: ProductService, private val applicationContext: Context) {

    private val productsLiveData = MutableLiveData<Product>()
    private val progBarVisibilityLiveData = MutableLiveData<Boolean>()

    val products: LiveData<Product>
        get() = productsLiveData

    val progressBarVisibility: LiveData<Boolean>
        get() = progBarVisibilityLiveData

    suspend fun getProducts() {
        try {
            progBarVisibilityLiveData.postValue(true)
            val result = productService.getProducts()
            if(result.body() != null){
                productsLiveData.postValue(result.body())
            } else {
                Handler(Looper.getMainLooper()).post {
                    Toast.makeText(applicationContext, "Server Issue, Try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: Exception) {
            Log.d("REPO ERROR", e.localizedMessage+"")
        } finally {
            progBarVisibilityLiveData.postValue(false)
        }
    }
}