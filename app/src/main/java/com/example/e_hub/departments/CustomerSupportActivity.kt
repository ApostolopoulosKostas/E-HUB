package com.example.e_hub.departments

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SearchBar

class CustomerSupportActivity : AppCompatActivity() {

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_support)

        //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.customerSupportSearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.customerSupSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.customerSupSuggestionsList)


        val searchBar = SearchBar(
            searchField = productSearch,
            productSuggestionScroll=productSuggestionScroll,
            productSuggestionsList =productSuggestionsList,
            scope =lifecycleScope,
            context = this,
            onProductSelected = { }



        )
        searchBar.searchListener()

    }


}