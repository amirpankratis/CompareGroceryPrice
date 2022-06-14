package com.bidbeep.compareGroceryPrice.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.adapters.CompareAdapter
import com.bidbeep.compareGroceryPrice.dataClass.CompareShops
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import java.text.NumberFormat
import java.util.*

class CompareResult : Fragment() {
    private val args: CompareResultArgs by navArgs()
    private val shopViewModel: ShopViewModel by viewModels()
    private val compareAdapter: CompareAdapter by lazy { CompareAdapter(requireContext()) }
    private val listOfCompare: MutableList<CompareShops> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_compare_result, container, false)

        // init of args
        val firstStore : Shop = args.firstStore
        val secondStore : Shop = args.secondStore

        // get args value and get the stores data from database
        var firstList: List<ProductsOfShop>
        var secondList: List<ProductsOfShop>
        runBlocking(Dispatchers.IO){
            firstList = shopViewModel.getProductsOfShopNotLive(firstStore.shopId)
            secondList = shopViewModel.getProductsOfShopNotLive(secondStore.shopId)
        }

        // init and add all item to new list
        initList(firstList, secondList, firstStore, secondStore)

        // recycle view init
        setupRv(v)

        // compare calculation and update
        compareCalculate(v, firstStore, secondStore)

        return v
    }

    private fun compareCalculate(v: View, firstStore: Shop, secondStore: Shop) {
        val pref: SharedPreferences? = (activity as AppCompatActivity).getSharedPreferences("locale_data", Context.MODE_PRIVATE)
        // get the data from shared preferences
        val currencyCode = pref?.getString("currency", null)

        // init the view
        val firstStoreTextTitle: TextView = v.findViewById(R.id.compareResultFirstStoreTotalTitle)
        val firstStoreTextTotal: TextView = v.findViewById(R.id.compareResultFirstStoreTotalPrice)
        val secondStoreTextTitle: TextView = v.findViewById(R.id.compareResultSecondStoreTotalTitle)
        val secondStoreTextTotal: TextView = v.findViewById(R.id.compareResultSecondStoreTotalPrice)
        val bestStoreText: TextView = v.findViewById(R.id.compareResultBestStore)

        // do the calculate
        val firstStoreTotal = listOfCompare.sumOf { it.firstShopPrice }
        val secondStoreTotal = listOfCompare.sumOf { it.secondShopPrice }


        // update values and check if which store has greater amount to update best store price
        if(firstStoreTotal < secondStoreTotal){
            val savedAmount = getMoneyFormat(secondStoreTotal.minus(firstStoreTotal), currencyCode)
            val text = "In total comparison, $savedAmount would spend less at ${firstStore.name}"
            bestStoreText.text = text
        } else{
            val savedAmount = getMoneyFormat(firstStoreTotal.minus(secondStoreTotal), currencyCode)
            val text = "In total comparison, $savedAmount would spend less at ${secondStore.name}"
            bestStoreText.text = text
        }

        firstStoreTextTitle.text = firstStore.name
        secondStoreTextTitle.text = secondStore.name
        firstStoreTextTotal.text = getMoneyFormat(firstStoreTotal, currencyCode)
        secondStoreTextTotal.text = getMoneyFormat(secondStoreTotal, currencyCode)

    }

    private fun getMoneyFormat(number: Double, currencyCode: String?): String {
        //if not data use default locale
        if (currencyCode == null ) {
            val numberFormat = NumberFormat.getCurrencyInstance(Locale.getDefault())
            numberFormat.maximumFractionDigits = 2
            return numberFormat.format(number)
        }
        val currency = Currency.getInstance(currencyCode)
        val formatted = String.format("%.2f", number)
        return "${currency.symbol} $formatted"
    }

    private fun setupRv(v: View) {
        // get recycleView of list
        val rv: RecyclerView = v.findViewById(R.id.rvCompareResult)

        // init adapter
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = compareAdapter

        // set the list to adapter
        compareAdapter.setListItems(listOfCompare)

        if(listOfCompare.isEmpty()){
            v.findViewById<TextView>(R.id.compareResultNoItemInList).visibility = View.VISIBLE
        }
    }

    private fun initList(
        firstList: List<ProductsOfShop>,
        secondList: List<ProductsOfShop>,
        firstStore: Shop,
        secondStore: Shop,
    ) {
        for (first in firstList){
            for (second in secondList){
                if(first.productId == second.productId){
                    // init list item
                    val newItem = CompareShops(
                        productId = first.productId,
                        name = first.name,
                        brand = first.brand,
                        scaleType = first.scaleType,
                        scaleValue = first.scaleValue,
                        firstShopId = firstStore.shopId,
                        secondShopId = secondStore.shopId,
                        firstShopName = firstStore.name,
                        secondShopName = secondStore.name,
                        firstShopPrice = first.price,
                        secondShopPrice = second.price
                    )
                    // add to list
                    listOfCompare.add(newItem)
                }
            }
        }
    }

}