package com.bidbeep.compareGroceryPrice.dataClass

data class CompareShops (
    val productId: Long,
    val name: String,
    val brand: String,
    val scaleType: String,
    val scaleValue: String,
    val firstShopId: Long,
    val secondShopId: Long,
    val firstShopName: String,
    val secondShopName: String,
    val firstShopPrice: Double,
    val secondShopPrice: Double
)