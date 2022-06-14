package com.bidbeep.compareGroceryPrice.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.bidbeep.compareGroceryPrice.entites.Shop
import java.util.*

class StoreAdapter(
    context: Context,
    @LayoutRes private val layoutResource: Int,
    storesList: List<Shop>
): ArrayAdapter<Shop>(context, layoutResource, storesList),
Filterable {
    private val allStores: List<Shop> = storesList
    private var store: MutableList<Shop> = storesList.toMutableList()

    override fun getCount(): Int {
        return store.size
    }
    override fun getItem(position: Int): Shop {
        return store[position]
    }
    override fun getItemId(position: Int): Long {
        return store[position].shopId
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val text = "${store[position].name} ${store[position].location}"
        val view: TextView = convertView as TextView? ?: LayoutInflater
            .from(context).inflate(layoutResource, parent, false) as TextView
        view.text = text
        return view
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun convertResultToString(resultValue: Any) :String {
                return (resultValue as Shop).name
            }
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint != null) {
                    val suggestion: MutableList<Shop> = ArrayList()
                    for (store in allStores) {
                        if (store.name.lowercase(Locale.ROOT)
                                .startsWith(constraint.toString().lowercase(Locale.ROOT))
                        ) {
                            suggestion.add(store)
                        }
                    }
                    filterResults.values = suggestion
                    filterResults.count = suggestion.size
                }
                return filterResults
            }
            override fun publishResults(
                constraint: CharSequence?,
                results: FilterResults
            ) {
                store.clear()
                if (results.count > 0) {
                    for (result in results.values as List<*>) {
                        if (result is Shop) {
                            store.add(result)
                        }
                    }
                    notifyDataSetChanged()
                } else if (constraint == null) {
                    store.addAll(allStores)
                    notifyDataSetInvalidated()
                }
            }
        }
    }
}