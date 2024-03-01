package com.hrithikvish.ecommerce.models

data class Data (
    val name: String,
    val sku : String,
    val price : Double,
    val web_url : String,
    val brand_name : String,
    val description : String,
    val configurable_option : List<ConfigurableOption>
)