package com.example.e_hub
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.appcompat.app.AppCompatActivity
import io.ktor.http.CacheControl

class ProductsActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products)

        val productSearch = findViewById<EditText>(R.id.productsearch)//search
        val productsResultList = findViewById<LinearLayout>(R.id.productsResultsList)


              //suggestions while typing
        val productSuggestionScroll = findViewById<ScrollView>(R.id.productSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.productSuggestionsList)

        productSearch.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
                val query = text.toString().trim()
                if (query.length>=4){
                    productSuggestionsList.removeAllViews()
                    productSuggestionScroll.visibility= View.VISIBLE

                }else{
                    productSuggestionScroll.visibility= View.GONE
                }

            }

            override fun beforeTextChanged

                        (text: CharSequence? ,
                         start: Int, count: Int , after: Int) {

            }

            override fun onTextChanged
                        (text: CharSequence?,
                         start: Int, before: Int, count: Int) {

            }
        })
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