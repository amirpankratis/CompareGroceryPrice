package com.bidbeep.compareGroceryPrice.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.ui.MainFragmentDirections

class ShopAdapter: RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private var list: MutableList<Shop> = mutableListOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val deleteBtn: Button = view.findViewById(R.id.eachListActionButton)
        val clickItem: View = view.findViewById(R.id.clickableStoreItem)
        val nameOfStore: TextView = view.findViewById(R.id.eachStoreName)
        val locationOfStore: TextView = view.findViewById(R.id.eachStoreLocation)
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
        // current item in the list
        val item = list[position]

        holder.apply {
            deleteBtn.visibility = View.GONE
            nameOfStore.text = item.name
            locationOfStore.text = item.location

            clickItem.setOnClickListener{
                val action = MainFragmentDirections.actionMainFragmentToStoreItem(item)
                it.findNavController().navigate(action)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateList(storeList: MutableList<Shop>){
        list = storeList
        notifyDataSetChanged()
    }
}
