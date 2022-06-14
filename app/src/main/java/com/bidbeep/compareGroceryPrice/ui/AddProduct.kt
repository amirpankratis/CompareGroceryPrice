package com.bidbeep.compareGroceryPrice.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class AddProduct : Fragment() {

    private val args: AddProductArgs by navArgs()
    private lateinit var shopViewModel: ShopViewModel
    private lateinit var productName: EditText
    private lateinit var productBrand: EditText
    private lateinit var productScaleType: String
    private lateinit var productScaleValue: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_add_product, container, false)

        // Init View Model
        shopViewModel = ViewModelProvider(this)[ShopViewModel::class.java]

        // init the view fields
        productName = v.findViewById(R.id.addProductName)
        productBrand = v.findViewById(R.id.addProductBrand)
        productScaleValue = v.findViewById(R.id.addProductScaleValue)
        val addBtn = v.findViewById<Button>(R.id.addProductBtn)

        // spinner ( dropdown )
        spinnerAction(v)

        // on add product button event listener
        addBtn.setOnClickListener {
            if(productName.text.toString().isNotEmpty()){
                saveProduct(v)
            }else {
                Toast.makeText(requireContext(), "All inputs are required", Toast.LENGTH_SHORT).show()
            }
        }

        return v
    }

    private fun spinnerAction(v: View) {
        //get the spinner field
        val scaleTypeSpinner: Spinner = v.findViewById(R.id.addProductScaleType)

        //init scale units
        val scaleTypeList = listOf("milligram", "gram", "kilogram", "liter", "milliliter", "pieces", "oz")
        //adapter
        val spinnerAdapter = ArrayAdapter(
            v.context,
            android.R.layout.simple_selectable_list_item,
            scaleTypeList
        )
        scaleTypeSpinner.adapter = spinnerAdapter

        //on change listener
        scaleTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                productScaleType = scaleTypeList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                productScaleType = ""
            }
        }
    }

    private fun saveProduct(view: View) {
        // save the shop by viewModel
        val product = Product(0,
            productName.text.toString(),
            productBrand.text.toString(),
            productScaleType,
            productScaleValue.text.toString()
        )

        // if similar product exist not add it
        if (checkIfProductExistInDB(product)){
            Toast.makeText(requireContext(),
                "Similar products exist, you may add the product from existing products",
                Toast.LENGTH_SHORT).show()
            return
        }

        var productId: Long
        runBlocking(Dispatchers.IO) {
            //now save the product
            productId = shopViewModel.insertProduct(product)
        }
        //now confirm the insert
        Toast.makeText(requireContext(), "Successfully Added", Toast.LENGTH_SHORT).show()

        //navigate back
        val action = AddProductDirections.actionAddProductToAddStoreProduct(args.store, productId.toInt())
        view.findNavController().navigate(action)

    }

    private fun checkIfProductExistInDB(product: Product): Boolean {
        val dbProduct = shopViewModel.getProductSimilar(product)
        return dbProduct != null
    }
}