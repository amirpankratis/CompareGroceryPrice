package com.bidbeep.compareGroceryPrice.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop
import java.text.NumberFormat
import java.util.*

class ShopProductsAdapter(
    context: Context,
    private val actionOnClickShopProductInterface: ActionOnClickShopProductInterface
): RecyclerView.Adapter<ShopProductsAdapter.ViewHolder>() {

    private var list: List<ProductsOfShop> = listOf()
    // get sharedPreferences
    private val pref: SharedPreferences? = context.getSharedPreferences("locale_data", Context.MODE_PRIVATE)
    // get the data from shared preferences
    private val currencyCode = pref?.getString("currency", null)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val deleteButton: Button = view.findViewById(R.id.eachListActionButton)
        val title: TextView = view.findViewById(R.id.eachStoreName)
        val subtitle: TextView = view.findViewById(R.id.eachStoreLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.each_store_list,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = list[position]
        val textTitle = " ${item.name} / ${getMoneyFormat(item.price)}"
        val textSubTitle = "${item.brand} - ${item.scaleValue} ${item.scaleType}"
        holder.apply {
            title.text = textTitle
            subtitle.text = textSubTitle
            deleteButton.setOnClickListener{
                actionOnClickShopProductInterface.onDeleteButton(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(newList: List<ProductsOfShop>){
        list = newList
        notifyDataSetChanged()
    }

    private fun getMoneyFormat(number: Double): String {
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

}

interface ActionOnClickShopProductInterface{
    fun onDeleteButton(productsOfShop: ProductsOfShop)
}