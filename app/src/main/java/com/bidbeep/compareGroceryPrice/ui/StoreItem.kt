package com.bidbeep.compareGroceryPrice.ui

import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.adapters.ActionOnClickShopProductInterface
import com.bidbeep.compareGroceryPrice.adapters.ShopProductsAdapter
import com.bidbeep.compareGroceryPrice.entites.relations.ProductsOfShop
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel

class StoreItem : Fragment(), ActionOnClickShopProductInterface {

    private val args: StoreItemArgs by navArgs()
    private val shopViewModel: ShopViewModel by viewModels()
    private val shopProductsAdapter: ShopProductsAdapter by lazy { ShopProductsAdapter(requireContext(), this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val v = inflater.inflate(R.layout.fragment_stores_item, container, false)

        //init
        val rv = v.findViewById<RecyclerView>(R.id.rvStoresItem)
        val btn = v.findViewById<Button>(R.id.storeAddProductBtn)
        val listShowBtn = v.findViewById<TextView>(R.id.storeProductListBtn)

        // get store from args
        val store = args.store
        val theText = "Products for ${store.name} , ${store.location}"
        listShowBtn.text = theText

        //recycleView
        rv.layoutManager = LinearLayoutManager(requireContext())
        rv.adapter = shopProductsAdapter

        //load product to recycleView
        shopViewModel.getProductsOfShop(store.shopId).observe(viewLifecycleOwner) {
            shopProductsAdapter.updateList(it)

            if (it.isEmpty()){
                v.findViewById<TextView>(R.id.storeItemsNoItemInList).visibility = View.VISIBLE
            }
        }

        //on click directions
        btn.setOnClickListener{
            val action = StoreItemDirections.actionStoreItemToAddStoreProduct(args.store)
            v.findNavController().navigate(action)
        }

        return v
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.store_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.removeStore){
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Delete Store")
            dialog.setMessage("Are you sure you want to delete this store?")
            dialog.setPositiveButton("Yes Delete"){_,_ ->
                shopViewModel.deleteShop(args.store)
                findNavController().navigate(R.id.action_storeItem_to_mainFragment)
            }
            dialog.setNegativeButton("No Close"){_,_ ->}
            dialog.show()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDeleteButton(productsOfShop: ProductsOfShop) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Delete Product of Store")
        dialog.setMessage("Are you sure want to delete this product?")
        dialog.setNegativeButton("No"){_,_ ->}
        dialog.setPositiveButton("Yes Delete"){_,_ ->
            shopViewModel.deleteShopProductByIds(args.store.shopId, productsOfShop.productId)
        }
        dialog.show()
    }
}