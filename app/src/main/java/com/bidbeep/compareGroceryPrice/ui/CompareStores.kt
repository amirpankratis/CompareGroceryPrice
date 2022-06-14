package com.bidbeep.compareGroceryPrice.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.adapters.StoreAdapter
import com.bidbeep.compareGroceryPrice.entites.Shop
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel

class CompareStores : Fragment() {

    private val shopViewModel: ShopViewModel by viewModels()
    private var storesList: List<Shop> = listOf()
    private lateinit var firstChoice: AutoCompleteTextView
    private lateinit var firstAdapter: StoreAdapter
    private lateinit var firstSelected: TextView
    private var firstValue: Shop? = null
    private lateinit var secondChoice: AutoCompleteTextView
    private lateinit var secondAdapter: StoreAdapter
    private lateinit var secondSelected: TextView
    private var secondValue: Shop? = null
    private lateinit var compareButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_compare_stores, container, false)

        //button init
        compareButton = v.findViewById(R.id.compareStoresButton)
        firstSelected = v.findViewById(R.id.firstCompareSelectedText)
        secondSelected = v.findViewById(R.id.secondCompareSelectedText)

        // get the list of stores and init
        shopViewModel.getAllShop.observe(viewLifecycleOwner){
            // set list
            storesList = it

            // set adapters
            setAdapters(v, it)

            // events of fields
            firstFieldsEvent()
            secondFieldEvent()
        }

        // on compare button event
        compareActionButton()

        return v
    }

    private fun compareActionButton() {
        //on compare button event listener
        compareButton.setOnClickListener {
            if (firstValue != null && secondValue != null) {
                val action = CompareStoresDirections.actionCompareStoresToCompareResult(
                    firstValue!!,
                    secondValue!!
                )
                findNavController().navigate(action)
            }else{
                Toast.makeText(requireContext(),
                    "the fields are required please select stores to compare",
                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setAdapters(v: View, list: List<Shop>) {
        //init adapter
        firstAdapter = StoreAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list
        )
        firstChoice = v.findViewById(R.id.autoCompleteCompareFirst)
        firstChoice.setAdapter(firstAdapter)


        //2nd init adapter
        secondAdapter = StoreAdapter(
            requireContext(),
            android.R.layout.simple_list_item_1,
            list
        )

        secondChoice = v.findViewById(R.id.autoCompleteCompareSecond)
        secondChoice.setAdapter(secondAdapter)

    }

    private fun firstFieldsEvent(){
        // event listener on click item of suggestion
        firstChoice.onItemClickListener =
            AdapterView.OnItemClickListener { _, _, p2, _ ->
                //save value of selected
                firstValue = firstAdapter.getItem(p2)

                //show if selected
                val item = firstAdapter.getItem(p2)
                val theText = "Selected " + item.name + " - " + item.location
                firstSelected.text = theText

                // empathy the second value
                val nothingSelectedText = "Nothing Selected"
                secondValue = null
                secondSelected.text = nothingSelectedText
                secondChoice.setText("")
            }
    }

    private fun secondFieldEvent(){
        // event listener on click item of suggestion
        secondChoice.onItemClickListener =
            AdapterView.OnItemClickListener {_, _, p2,_ ->
                //validate if second selected is not same first
                if (secondAdapter.getItem(p2) == firstValue){
                    // empathy the second value
                    val nothingSelectedText = "Nothing Selected"
                    secondValue = null
                    secondSelected.text = nothingSelectedText
                    secondChoice.setText("")

                    //alert user
                    Toast.makeText(requireContext(),
                        "Can not compare same store please select different store",
                    Toast.LENGTH_LONG).show()
                }else {
                    //save value of selected
                    secondValue = secondAdapter.getItem(p2)

                    //show if selected
                    val item = secondAdapter.getItem(p2)
                    val theText = "Selected " + item.name + " - " + item.location
                    secondSelected.text = theText
                }
            }
    }
}