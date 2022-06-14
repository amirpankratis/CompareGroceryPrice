package com.bidbeep.compareGroceryPrice.entites

import androidx.room.ColumnInfo
import androidx.room.Entity

// the relation table btw shop and products and price of them
@Entity(primaryKeys = ["shopId", "productId"])
data class ShopProductCrossRef (
    @ColumnInfo(index = true)
    val shopId: Long,
    @ColumnInfo(index = true)
    val productId: Long,
    val price: Double
)