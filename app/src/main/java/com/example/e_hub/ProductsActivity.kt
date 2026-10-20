package com.example.e_hub
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity

class ProductsActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products)

        val productSearch = findViewById<EditText>(R.id.productsearch)//search
        val productsResultList = findViewById<LinearLayout>(R.id.productsResultsList)

              //suggestions while typing
        val productSuggestionScroll = findViewById<ScrollView>(R.id.productSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.productSuggestionsList)







    }



}









//TextWatcher detects when the search text changes.
//
//
//
//Editable represents the text being typed.
//
//
//
//View lets us show or hide the suggestions area.
//
//
//
//TextView lets us display product names and codes in the suggestions