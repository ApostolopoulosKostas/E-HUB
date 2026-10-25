package com.example.e_hub.departments

import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SearchBar

class FridgeActivity : AppCompatActivity() {

    override fun onCreate (savedInstantState: Bundle?){
        super.onCreate(savedInstantState)
        setContentView(R.layout.fridge)



        //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.fridgeSearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.fridgeSuggestionScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.fridgeRequestList)

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