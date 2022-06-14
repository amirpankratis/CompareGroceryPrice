package com.bidbeep.compareGroceryPrice.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.entites.ShopProductCrossRef
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel
import java.util.*

class AddStoreProduct : Fragment() {

    private val args: AddStoreProductArgs by navArgs()
    private val shopViewModel: ShopViewModel by viewModels()
    private var product: Product? = null
    private lateinit var priceField : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_store_product, container, false)

        // init product by args if existed
        getProduct(v)

        //get currency details to show to user
        getCurrencyDetails(v)

        //buttons actions
        buttonsActions(v)

        //add action upon submit button
        addAndSaveAction(v)

        return v
    }

    private fun addAndSaveAction(v: View) {
        // get button view
        val addProductButton: Button = v.findViewById(R.id.addProductToStoreBtn)
        priceField = v.findViewById(R.id.addStoreProductPrice)

        //on add eventListener
        addProductButton.setOnClickListener {
            if (priceField.text.toString().isNotEmpty() &&
                product != null){
                addProductStore(v)
            }else {
                Toast.makeText(requireContext(),"Both Fields are required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCurrencyDetails(v: View) {
        // init the view
        val viewText: TextView = v.findViewById(R.id.addStoreProductCurrency)

        //init the SharedPreferences get data
        val pref = (activity as AppCompatActivity).getSharedPreferences("locale_data", Context.MODE_PRIVATE)
        val currencyCode = pref?.getString("currency", null)

        //get currency
        if(currencyCode == null) {
            val text = "no currency selected, would use phone default currency"
            viewText.text = text
            return
        }

        val currency = Currency.getInstance(currencyCode)

        //update data to view
        viewText.text = currency.displayName

    }

    private fun getProduct(v: View) {
        //get data view
        val productField = v.findViewById<EditText>(R.id.addStoreProductInfo)

        if (args.productId == 0) {
            // make it uncheck if not have value
            productField.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.style_product_btn,
                0, R.drawable.ic_no_check, 0)

            return
        }

        // if product id in args
        product = shopViewModel.getOneProductById(args.productId.toLong())
        productField.setText(product!!.name)
        // check mark if is ok
        productField.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        productField.setCompoundDrawablesWithIntrinsicBounds(
            R.drawable.style_product_btn,
            0, R.drawable.ic_check, 0)
    }

    private fun buttonsActions(v: View) {
        //here we handle buttons actions
        val addNewBtn: Button = v.findViewById(R.id.addProductNavBtn)
        val selectBtn: Button = v.findViewById(R.id.selectProductNav)

        addNewBtn.setOnClickListener {
            val action = AddStoreProductDirections.actionAddStoreProductToAddProduct(args.store)
            v.findNavController().navigate(action)
        }

        selectBtn.setOnClickListener {
            val action = AddStoreProductDirections.actionAddStoreProductToSelectProduct(args.store)
            v.findNavController().navigate(action)
        }
    }

    private fun addProductStore(v: View) {
        // add to store shop
        val price: Double = priceField.text.toString().toDouble()
        val shopProductCrossRef = ShopProductCrossRef(args.store.shopId, product!!.productId, price)
        shopViewModel.insertShopProduct(shopProductCrossRef)
        //now confirm the insert
        Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()
        //navigate back
        val action = AddStoreProductDirections.actionAddStoreProductToStoreItem(args.store)
        v.findNavController().navigate(action)
    }
}