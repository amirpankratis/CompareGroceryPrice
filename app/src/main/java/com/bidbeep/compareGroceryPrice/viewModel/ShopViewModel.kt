package com.bidbeep.compareGroceryPrice.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.bidbeep.compareGroceryPrice.database.AppDatabase
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop
import com.bidbeep.compareGroceryPrice.repository.ShopRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShopViewModel(application: Application): AndroidViewModel(application) {
    private var repository: ShopRepository
    var getAllProduct: LiveData<List<Product>>
    var getAllShop: LiveData<List<Shop>>

    init {
        val dao = AppDatabase.getInstance(application).shopDao
        repository = ShopRepository(dao)
        getAllShop = repository.getAllShop
        getAllProduct = repository.getAllProducts
    }

    fun getOneProductById(id: Long): Product? {
        var result: Product?
        runBlocking(Dispatchers.IO){
            result = repository.getOneProductById(id)
        }
        return result
    }

    suspend fun insertProduct(product: Product): Long {
        return repository.insertProduct(product)
    }

    fun deleteProduct(product: Product){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteProduct(product)
        }
    }

    fun insertShopProduct(shopProductCrossRef: ShopProductCrossRef){
        viewModelScope.launch(Dispatchers.IO){
            repository.insertShopProduct(shopProductCrossRef)
        }
    }

    fun insertShop(shop: Shop){
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertShop(shop)
        }
    }

    fun deleteShop(shop: Shop){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteShop(shop)
        }
    }

    fun deleteShopProductByIds(shopId: Long, productId: Long){
        viewModelScope.launch(Dispatchers.IO){
            repository.deleteShopProductByIds(shopId, productId)
        }
    }

    fun getProductSimilar(product: Product): Product {
        val newProduct: Product
        runBlocking(Dispatchers.IO){
            newProduct = repository.getProductSimilar(product)
        }
        return newProduct
    }

    fun getProductsOfShop(shopId: Long): LiveData<List<ProductsOfShop>> {
        return repository.getProductsOfShop(shopId)
    }

    fun getShopSimilar(shop: Shop): Shop {
        val newShop: Shop
        runBlocking (Dispatchers.IO){ newShop = repository.getShopSimilar(shop) }
        return newShop
    }

    fun getProductsOfShopNotLive(shopId: Long): List<ProductsOfShop> {
        return repository.getProductsOfShopNotLive(shopId)
    }

    fun searchProducts(search: String): LiveData<List<Product>> {
        return repository.searchProducts(search)
    }

    fun findProductsInUseOfStore(productId: Long): ShopProductCrossRef{
        val dbProduct : ShopProductCrossRef
        runBlocking(Dispatchers.IO){
            dbProduct = repository.findProductsInUseOfStore(productId)
        }
        return dbProduct
    }

}