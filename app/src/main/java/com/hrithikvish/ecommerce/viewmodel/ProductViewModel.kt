package com.hrithikvish.ecommerce.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hrithikvish.ecommerce.models.Product
import com.hrithikvish.ecommerce.repo.ProductRepo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProductViewModel(private val repo: ProductRepo) : ViewModel() {

    init {
        viewModelScope.launch(Dispatchers.IO){
            repo.getProducts()
        }
    }

    val products : LiveData<Product>
        get() = repo.products

    val progBarVisibility : LiveData<Boolean>
        get() = repo.progressBarVisibility

}