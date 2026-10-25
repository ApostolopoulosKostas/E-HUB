package com.example.e_hub.departments

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SearchBar

class GroceryActivity : AppCompatActivity() {

    override fun onCreate (savedInstantState: Bundle?) {
        super.onCreate(savedInstantState)
        setContentView(R.layout.grocery)


        //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.grocerySearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.groceryRequestScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.groceryRequestList)

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