package com.bidbeep.compareGroceryPrice.repository

import androidx.lifecycle.LiveData
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef
import com.bidbeep.compareGroceryPrice.entites.dao.ShopDao
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop

// repository of data to change data indirectly main tread
class ShopRepository (private val shopDao: ShopDao) {

    val getAllShop: LiveData<List<Shop>> = shopDao.getAllShop()

    val getAllProducts: LiveData<List<Product>> = shopDao.getAllProduct()

    fun getOneProductById(id: Long): Product {
        return shopDao.getOneProductById(id)
    }

    suspend fun insertProduct(product: Product): Long{
        return shopDao.insertProduct(product)
    }

    suspend fun deleteProduct(product: Product){
        return shopDao.deleteProduct(product)
    }

    suspend fun insertShop(shop: Shop): Long{
        return  shopDao.insertShop(shop)
    }

    suspend fun deleteShop(shop: Shop){
        shopDao.deleteShopProductByShopId(shop.shopId)
        return shopDao.deleteShop(shop)

    }

    suspend fun deleteShopProductByIds(shopId: Long, productId: Long){
        return shopDao.deleteShopProductByIds(shopId, productId)
    }

    suspend fun insertShopProduct(shopProductCrossRef: ShopProductCrossRef){
        return shopDao.insertShopProduct(shopProductCrossRef)
    }

    fun getProductSimilar(product: Product): Product{
        return shopDao.getProductSimilar(
            product.name,
            product.brand,
            product.scaleType,
            product.scaleValue
        )
    }

    fun getProductsOfShop(shopId: Long): LiveData<List<ProductsOfShop>> {
        return shopDao.getProductsOfShop(shopId)
    }

    fun getShopSimilar(shop: Shop): Shop{
        return shopDao.getShopSimilar(shop.name, shop.location)
    }

    fun getProductsOfShopNotLive(shopId: Long): List<ProductsOfShop> {
        return shopDao.getProductsOfShopNotLive(shopId)
    }

    fun searchProducts(search: String): LiveData<List<Product>> {
        return shopDao.searchProducts(search)
    }

    fun findProductsInUseOfStore(productId: Long): ShopProductCrossRef{
        return shopDao.findProductsInUseOfStore(productId)
    }

}