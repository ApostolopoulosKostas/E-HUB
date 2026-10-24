package com.example.e_hub
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope


class ProductsActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products)



             //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.productsearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.productSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.productSuggestionsList)

        val searchBar = SearchBar(
            searchField = productSearch,
            productSuggestionScroll=productSuggestionScroll,
            productSuggestionsList =productSuggestionsList,
            scope =lifecycleScope,
            context = this

        )
        searchBar.searchListener()


    }
}