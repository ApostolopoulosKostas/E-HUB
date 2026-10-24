package com.example.e_hub.departments
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SearchBar

class WarehouseActivity : AppCompatActivity(){

    override fun onCreate (savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warehouse)


        //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.warehouseSearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.warehouseRequestScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.warehouseRequestList)

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