package com.bidbeep.compareGroceryPrice.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bidbeep.compareGroceryPrice.R
import com.bidbeep.compareGroceryPrice.adapters.ActionOnClickProductInterface
import com.bidbeep.compareGroceryPrice.adapters.ProductsAdapter
import com.bidbeep.compareGroceryPrice.entites.Product
import com.bidbeep.compareGroceryPrice.viewModel.ShopViewModel

class SelectProduct : Fragment(), SearchView.OnQueryTextListener, ActionOnClickProductInterface {

    private val adapter: ProductsAdapter by lazy { ProductsAdapter(this) }
    private val shopViewModel: ShopViewModel by viewModels()
    private val args: SelectProductArgs by navArgs()

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
        val v = inflater.inflate(R.layout.fragment_select_product, container, false)

        val rvProducts : RecyclerView = v.findViewById(R.id.rvSelectProduct)
        rvProducts.layoutManager = LinearLayoutManager(requireContext())
        rvProducts.adapter = adapter

        shopViewModel.getProductsOfShop(args.store.shopId).observe(viewLifecycleOwner) {shop ->
            shopViewModel.getAllProduct.observe(viewLifecycleOwner) {
                val newList = it.filterNot{a ->
                    shop.any {b -> a.productId == b.productId}
                }
                adapter.updateList(newList)

                if(newList.isEmpty()){
                    v.findViewById<TextView>(R.id.selectProductNoItemInList).visibility = View.VISIBLE
                }
            }
        }



        return v
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.product_search, menu)

        val search = menu.findItem(R.id.searchProductMenu)
        val searchView = search.actionView as SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        if (newText != null){
            searchProducts(newText)
        }
        return true
    }

    private fun searchProducts(searchText: String){
        val searchQuery = "%$searchText%"

        shopViewModel.getProductsOfShop(args.store.shopId).observe(viewLifecycleOwner) {shop ->
            shopViewModel.searchProducts(searchQuery).observe(this){list ->
                list.let {
                    val newList = it.filterNot{a ->
                        shop.any {b -> a.productId == b.productId}
                    }
                    adapter.updateList(newList)
                }
            }
        }
    }

    override fun onClickProduct(product: Product) {
        val action = SelectProductDirections
            .actionSelectProductToAddStoreProduct(args.store,product.productId.toInt())
        findNavController().navigate(action)
    }

    override fun onClickDelete(product: Product) {
        //check if product is in use of some store not allow delete
        val inUseProduct = shopViewModel.findProductsInUseOfStore(product.productId)
        if (inUseProduct != null){
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Product cannot be deleted!")
            dialog.setMessage("Product is in use of many or a store in order to delete it, you may delete first where been used")
            dialog.setNegativeButton("I understand"){_,_ ->}
            dialog.show()
        }else{
            val dialog = AlertDialog.Builder(requireContext())
            dialog.setTitle("Delete Product!")
            dialog.setMessage("Are you sure want to delete this product?")
            dialog.setNegativeButton("No"){_,_ ->}
            dialog.setPositiveButton("Yes Delete"){_,_ ->
                shopViewModel.deleteProduct(product)
            }
            dialog.show()
        }
    }
}