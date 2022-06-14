package com.bidbeep.compareGroceryPrice.entites.relations

data class ProductsOfShop(
    val productId: Long,
    val name: String,
    val brand: String,
    val scaleType: String,
    val scaleValue: String,
    val price: Double,
)