package com.bidbeep.compareGroceryPrice.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.Product

class ProductsAdapter(
    private val actionOnClick: ActionOnClickProductInterface
    ): RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {

    private var list: List<Product> = listOf()

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val deleteButton: Button = view.findViewById(R.id.eachListActionButton)
        val clickAble: View = view.findViewById(R.id.clickableStoreItem)
        val title: TextView = view.findViewById(R.id.eachStoreName)
        val subTitle: TextView = view.findViewById(R.id.eachStoreLocation)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.each_store_list,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = list[position]
        val titleText = "${item.name} - ${item.brand}"
        val subText = "${item.scaleValue}  ${item.scaleType}"
        holder.apply {
            title.text = titleText
            subTitle.text = subText
            clickAble.setOnClickListener{
                actionOnClick.onClickProduct(item)
            }
            deleteButton.setOnClickListener{
                actionOnClick.onClickDelete(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newProductList: List<Product>){
        this.list = newProductList
        notifyDataSetChanged()
    }
}

interface ActionOnClickProductInterface{
    fun onClickProduct(product: Product)
    fun onClickDelete(product: Product)
}