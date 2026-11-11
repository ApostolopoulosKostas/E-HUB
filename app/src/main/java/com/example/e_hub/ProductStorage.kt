package com.example.e_hub
import android.content.Context
import com.google.gson.Gson
import kotlinx.serialization.json.Json

object ProductStorage {
    private const val PREFS_NAME = "product_preferences"//PREFS_NAME identifies the storage cupboard.
    private const val PRODUCTS_KEY ="saved products"//identifies the box inside that cupboard where we'll keep our products.


    fun savedProducts(context: Context,products: List<Product>){

        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = Gson().toJson(products)
        sharedPreferences.edit().putString(PRODUCTS_KEY, json).apply()
    }

    fun loadProducts(context: Context): MutableList<Product>{
        val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val json = sharedPreferences.getString(PRODUCTS_KEY,null)

        if (json==null){
            return mutableListOf()
        }
        return Gson().fromJson(json, Array<Product>::class.java).toMutableList()
    }

}