package com.bidbeep.compareGroceryPrice.adapters

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.dataClass.CompareShops
import java.text.NumberFormat
import java.util.*

class CompareAdapter(context: Context) : RecyclerView.Adapter<CompareAdapter.ViewHolder>() {

    var list: List<CompareShops> = listOf()

    // get sharedPreferences
    private val pref: SharedPreferences? = context.getSharedPreferences("locale_data", Context.MODE_PRIVATE)
    // get the data from shared preferences
    private val currencyCode = pref?.getString("currency", null)

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val productName: TextView = view.findViewById(R.id.compareResultItemName)
        val productBrand: TextView = view.findViewById(R.id.compareResultItemBrand)
        val productScale: TextView = view.findViewById(R.id.compareResultItemScale)
        val firstStoreName: TextView = view.findViewById(R.id.compareResultItemFirstStoreName)
        val firstStorePrice: TextView = view.findViewById(R.id.compareResultItemFirstStorePrice)
        val secondStoreName: TextView = view.findViewById(R.id.compareResultItemSecondStoreName)
        val secondStorePrice: TextView = view.findViewById(R.id.compareResultItemSecondStorePrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.compare_result_item,
            parent,
            false
        )

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val scaleText = item.scaleValue + " " + item.scaleType

        holder.apply {
            productName.text = item.name
            productBrand.text = item.brand
            productScale.text = scaleText
            firstStoreName.text = item.firstShopName
            secondStoreName.text = item.secondShopName
            firstStorePrice.text = getMoneyFormat(item.firstShopPrice)
            secondStorePrice.text = getMoneyFormat(item.secondShopPrice)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setListItems(newList : List<CompareShops>) {
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