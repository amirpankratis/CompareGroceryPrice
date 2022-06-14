package com.bidbeep.compareGroceryPrice.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef
import com.bidbeep.compareGroceryPrice.entites.dao.ShopDao

@Database(entities = [
    Shop::class,
    Product::class,
    ShopProductCrossRef::class
], version = 1 , exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val shopDao: ShopDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            synchronized(this){
                return INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "compare_grocery_product"
                ).build().also {
                    INSTANCE = it
                }
            }
        }


    }
}