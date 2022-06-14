package com.bidbeep.compareGroceryPrice.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.adapters.ShopAdapter
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel
import java.util.*

class MainFragment : Fragment() {

    private val shopAdapter: ShopAdapter by lazy { ShopAdapter() }
    private val shopViewModel: ShopViewModel by viewModels()

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.hide()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.fragment_main, container, false)

        // get and save currency locale
        checkSetCurrency()

        // navigation buttons
        navigationButtonAction(v)

        // setup recycleView and adapter
        setupRvStores(v)

        return v
    }

    private fun setupRvStores(v: View) {
        val rv = v.findViewById<RecyclerView>(R.id.rvMainStores)

        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = shopAdapter

        shopViewModel.getAllShop.observe(viewLifecycleOwner){
            shopAdapter.updateList(it.toMutableList())

            if (it.isEmpty()){
                v.findViewById<TextView>(R.id.mainNoItemInList).visibility = View.VISIBLE
            }
        }
    }

    private fun navigationButtonAction(v: View) {
        val goTOCompare = v.findViewById<Button>(R.id.mainCompareBtn)
        goTOCompare.setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_compareStores)
        }

        //init data
        val addStoreBtn = v.findViewById<Button>(R.id.mainAddStoreBtn)

        addStoreBtn.setOnClickListener{
            v.findNavController().navigate(R.id.action_mainFragment_to_addStore)
        }
    }

    private fun checkSetCurrency() {
        val pref = (activity as AppCompatActivity?)
            ?.getSharedPreferences("locale_data", Context.MODE_PRIVATE)
        pref.let {
            val getLocaleCurrency = pref?.getString("currency", null)
            if (getLocaleCurrency == null) {
                newLocaleDialog(pref!!)
            }
        }

    }

    private fun newLocaleDialog(pref: SharedPreferences) {
        //list of currencies
        val currencies : List<Currency> = listOf(
            Currency.getInstance("USD"),
            Currency.getInstance("EUR"),
            Currency.getInstance("PHP"),
            Currency.getInstance("BRL"),
            Currency.getInstance("GBP"),
        )
        val listOptions: List<String> = listOf(
            "US Dollar", "Euro", "Philippine Peso", "Brazilian Real", "British Pound")

        // setup builder
        val builder = AlertDialog.Builder(requireContext())

        // get layout Inflater and layout and get spinner from it
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.alert_dialog_with_dropdown, null)
        val spinner = dialogLayout.findViewById<Spinner>(R.id.spinnerForAlertDialog)

        //adapter of spinner
        val spinnerAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_selectable_list_item,
            listOptions
        )
        spinner.adapter = spinnerAdapter

        // event select item
        spinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedValue = currencies[p2].currencyCode
                saveCurrency(pref, selectedValue)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
        // setup dialog
        builder.setTitle("Select a currency")
        builder.setView(dialogLayout)
        builder.setPositiveButton("save") { _, _ ->
            Toast.makeText(requireContext(), "Saved Successfully", Toast.LENGTH_SHORT).show()
        }
        builder.show()
    }

    private fun saveCurrency(pref: SharedPreferences, value:String) {
        // save Currency Code to SharedPreferences
        val editor = pref.edit()
        editor.apply{
            putString("currency", value)
            apply()
        }
    }

}