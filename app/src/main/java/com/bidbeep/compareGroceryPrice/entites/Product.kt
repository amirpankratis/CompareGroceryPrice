package com.bidbeep.compareGroceryPrice.entites

import androidx.room.Entity
import androidx.room.PrimaryKey

// here table where store all products of any shop
@Entity(tableName = "products")
data class Product (
    @PrimaryKey(autoGenerate = true)
    val productId: Long,
    val name: String,
    val brand: String,
    val scaleType: String,
    val scaleValue: String
)