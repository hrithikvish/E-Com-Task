package com.hrithikvish.ecommerce.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hrithikvish.ecommerce.repo.ProductRepo

class ProductViewModelFactory(private val repo: ProductRepo) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ProductViewModel(repo) as T
    }
}