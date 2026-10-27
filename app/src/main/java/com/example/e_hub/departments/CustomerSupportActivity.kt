package com.example.e_hub.departments

import android.health.connect.datatypes.units.Length
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.example.e_hub.Product
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
        var selectedProduct: Product?=null
        val searchBar = SearchBar(
            searchField = productSearch,
            productSuggestionScroll=productSuggestionScroll,
            productSuggestionsList =productSuggestionsList,
            scope =lifecycleScope,
            context = this,

            onProductSelected = {product ->
                selectedProduct = product


                productSearch.setText("${product.code} - ${product.name}")
                productSuggestionScroll.visibility = android.view.View.GONE
            })
        searchBar.searchListener()

        val quantityInput =findViewById<EditText>(R.id.quantityInput)

        val locationInput = findViewById<EditText>(R.id.locationInput)

        val messageInput =findViewById<EditText>(R.id.messageInput)

        val selectDepartment =findViewById<Spinner>(R.id.selectDepartment)
        val departments = listOf("Ψυγεία", "Μανάβικο", "Αποθήκη")

        val insertedProducts =findViewById<RecyclerView>(R.id.insertedProducts)

        // ------ SEND THE REQUEST TO THE DEPARTMENTS HANDLING ------
        val sendRequest= findViewById<Button>(R.id.sendRequest)
        sendRequest.setOnClickListener {
            val code = selectedProduct?.code ?: productSearch.text.toString()
            val name = selectedProduct?.name ?: ""
            val url = selectedProduct?.url ?: ""
            if (code.length !=7 ||  !code.all { it.isDigit() }){
                Toast.makeText(this, "ο κωδικός δεν περιέχει 7 ψηφία",
                    Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val quantity =quantityInput.text.toString()
            val location = locationInput.text.toString()
            val message = messageInput.text.toString()
            val department = selectDepartment.selectedItem.toString()

        }
              //  ---------- DEPARTMENT SELECTION DROPDOWN -----------
        selectDepartment.adapter= ArrayAdapter(
            this,android.R.layout.simple_spinner_item,departments
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_item)
        }
    }
}