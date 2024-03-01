package com.hrithikvish.ecommerce.models

data class Attributes (
    val value: String,
    val option_id: String,
    val attribute_image_url: String,
    val price: String,
    val images: List<String>,
    val color_code: Any,
    val swatch_url: String
)