package com.bidbeep.compareGroceryPrice.entites.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop


// Data Access for Shops and products
@Dao
interface ShopDao {

    // Insert update delete Data

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertProduct(product: Product) :Long

    @Delete
    suspend fun deleteProduct(vararg product: Product)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShop(shop: Shop): Long

    @Delete
    suspend fun deleteShop(vararg shop: Shop)

    @Query("DELETE FROM ShopProductCrossRef WHERE shopId = :shopId")
    suspend fun deleteShopProductByShopId(shopId: Long)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertShopProduct(crossRef: ShopProductCrossRef)

    @Query("DELETE FROM ShopProductCrossRef WHERE" +
            " shopId = :shopId" +
            " AND productId = :productId")
    suspend fun deleteShopProductByIds(shopId: Long, productId: Long)

    // Get Data

    @Query("SELECT * FROM products WHERE productId = :id")
    fun getOneProductById(id: Long) : Product

    @Query("SELECT * FROM products WHERE" +
            " LOWER(name) = LOWER(:name)" +
            " AND LOWER(brand) = LOWER(:brand)" +
            " AND LOWER(scaleType) = LOWER(:scaleType)" +
            " AND LOWER(scaleValue) = LOWER(:scaleValue)")
    fun getProductSimilar(
        name: String,
        brand: String,
        scaleType: String,
        scaleValue: String
    ): Product

    @Query("SELECT * FROM products")
    fun getAllProduct(): LiveData<List<Product>>

    @Query("SELECT * FROM shops")
    fun getAllShop(): LiveData<List<Shop>>

    @Query("SELECT * FROM shops WHERE" +
            " LOWER(name) = LOWER(:name)" +
            " AND LOWER(location) = LOWER(:location)")
    fun getShopSimilar(name: String, location: String): Shop

    @Query("SELECT * FROM products AS p WHERE p.name LIKE :search OR p.brand LIKE :search")
    fun searchProducts(search: String): LiveData<List<Product>>

    @Query("SELECT " +
            "cr.price AS price," +
            "B.productId AS productId," +
            "B.name AS name," +
            "B.brand AS brand," +
            "B.scaleType AS scaleType," +
            "B.scaleValue AS scaleValue" +
            " FROM ShopProductCrossRef AS cr" +
            " JOIN shops A ON A.shopId = cr.shopId" +
            " JOIN products B ON B.productId = cr.productId" +
            " WHERE cr.shopId = :shopId")
    fun getProductsOfShop(shopId: Long): LiveData<List<ProductsOfShop>>

    @Query("SELECT " +
            "cr.price AS price," +
            "B.productId AS productId," +
            "B.name AS name," +
            "B.brand AS brand," +
            "B.scaleType AS scaleType," +
            "B.scaleValue AS scaleValue" +
            " FROM ShopProductCrossRef AS cr" +
            " JOIN shops A ON A.shopId = cr.shopId" +
            " JOIN products B ON B.productId = cr.productId" +
            " WHERE cr.shopId = :shopId")
    fun getProductsOfShopNotLive(shopId: Long): List<ProductsOfShop>

    @Query("SELECT * FROM ShopProductCrossRef WHERE productId = :productId")
    fun findProductsInUseOfStore(productId: Long): ShopProductCrossRef

}