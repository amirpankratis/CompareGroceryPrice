package com.bidbeep.compareGroceryPrice.entites.relations

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef

// relation joining for products belongs to a shop
data class ShopWithProducts (
    @Embedded val shop : Shop,
    @Relation(
        parentColumn = "shopId",
        entityColumn = "productId",
        associateBy = Junction(ShopProductCrossRef::class)
    )
    val products: List<Product>

)