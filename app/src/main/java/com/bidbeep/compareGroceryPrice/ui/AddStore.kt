package com.bidbeep.compareGroceryPrice.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel

class AddStore : Fragment() {

    private lateinit var shopViewModel: ShopViewModel
    private lateinit var shopName: EditText
    private lateinit var shopLocation: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_store, container, false)

        // Init View Model
        shopViewModel = ViewModelProvider(this)[ShopViewModel::class.java]

        // get the view Data
        shopName = v.findViewById(R.id.addShopName)
        shopLocation = v.findViewById(R.id.addShopLocation)
        val addBtn = v.findViewById<Button>(R.id.addShopBtn)

        // now if submitted
        addBtn.setOnClickListener {
            if(shopLocation.text.toString().isNotEmpty() && shopName.text.toString().isNotEmpty()){
                saveShop(v)
            }else {
                Toast.makeText(requireContext(), "All inputs are required", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    private fun saveShop(view: View) {
        // save the shop by viewModel
        val shop = Shop(0, shopName.text.toString(),
            shopLocation.text.toString())

        if(checkIfSimilarStoreExistInDB(shop)){
            Toast.makeText(requireContext(),
                "Similar Store exist, Can not add same store",
                Toast.LENGTH_SHORT).show()
            return
        }

        shopViewModel.insertShop(shop)

        //now confirm the insert
        Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()

        //navigate back
        view.findNavController().navigate(R.id.action_addStore_to_mainFragment)

    }

    private fun checkIfSimilarStoreExistInDB(shop: Shop): Boolean {
        val dbStore = shopViewModel.getShopSimilar(shop)
        return dbStore != null
    }
}