package com.bidbeep.compareGroceryPrice.entites

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

// here table where store all shop name
@Parcelize
@Entity(tableName = "shops")
data class Shop(
    @PrimaryKey(autoGenerate = true)
    val shopId: Long,
    val name: String,
    val location: String,
): Parcelable