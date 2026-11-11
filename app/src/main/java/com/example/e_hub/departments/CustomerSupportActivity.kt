
package com.example.e_hub.departments


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
import com.example.e_hub.ReceivedRequests
import com.example.e_hub.SendRequests
import com.example.e_hub.SearchBar
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.launch

class CustomerSupportActivity : AppCompatActivity() {

    // ----- REQUEST LIST -----
    private val requests = mutableListOf<ReceivedRequests>()
    private lateinit var adapter: CustomerSupportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_support)

        val productSearch = findViewById<EditText>(R.id.customerSupportSearch)
        val productSuggestionScroll = findViewById<ScrollView>(R.id.customerSupSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.customerSupSuggestionsList)
        var selectedProduct: Product? = null

        // ----- PRODUCT SEARCH -----
        val searchBar = SearchBar(
            searchField = productSearch,
            productSuggestionScroll = productSuggestionScroll,
            productSuggestionsList = productSuggestionsList,
            scope = lifecycleScope,
            context = this,
            onProductSelected = { product ->
                selectedProduct = product
                productSearch.setText("${product.code} - ${product.name}")
                productSuggestionScroll.visibility = android.view.View.GONE
            }
        )
        searchBar.searchListener()

        val quantityInput = findViewById<EditText>(R.id.quantityInput)
        val locationInput = findViewById<EditText>(R.id.locationInput)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val selectDepartment = findViewById<Spinner>(R.id.selectDepartment)
        val departments = listOf("Ψυγεία", "Μανάβικο", "Αποθήκη")





        // ----- REQUEST LIST -----
        val insertedProducts = findViewById<RecyclerView>(R.id.insertedProducts)
        insertedProducts.layoutManager =
            androidx.recyclerview.widget.LinearLayoutManager(this)
        adapter = CustomerSupportAdapter(requests) { request ->
            deleteRequest(request)
        }

        insertedProducts.adapter = adapter

        // ----- LOAD REQUESTS -----
//        lifecycleScope.launch {
//            val result = SupabaseClient.getDepartmentRequests()
//
//            requests.clear()
//            requests.addAll(result)
//            requests.sortBy { it.completed == true }
//
//            adapter.notifyDataSetChanged()
//        }
        loadRequests()

        // ----- SEND REQUEST -----
        val sendRequest = findViewById<Button>(R.id.sendRequest)

        val clearCompletedButton =
            findViewById<Button>(R.id.clearCompletedRequestsButton)

        clearCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    SupabaseClient.clearCompletedRequests()
                    loadRequests()

                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Completed requests cleared",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Could not clear requests: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        sendRequest.setOnClickListener {
            val code = selectedProduct?.code ?: productSearch.text.toString()
            val name = selectedProduct?.name ?: ""
            val url = selectedProduct?.url ?: ""

            if (code.length != 7 || !code.all { it.isDigit() }) {
                Toast.makeText(
                    this,
                    "ο κωδικός δεν περιέχει 7 ψηφία",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            val quantity = quantityInput.text.toString().toDoubleOrNull()
            val location = locationInput.text.toString()
            val message = messageInput.text.toString()
            val department = selectDepartment.selectedItem.toString()

            val request = SendRequests(
                code = code,
                product_name = name,
                product_url = url,
                quantity = quantity,
                location = location,
                message = message,
                department = department,
                requested_by = null
            )

            lifecycleScope.launch {
                try {
                    SupabaseClient.sendRequest(request)
                    loadRequests()
                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Request sent",
                        Toast.LENGTH_SHORT
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Could not send request: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // ----- DEPARTMENT SELECTION -----
        selectDepartment.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            departments
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_item)
        }
    }

    // ----- DELETE REQUEST -----
    private fun deleteRequest(request: ReceivedRequests) {
        lifecycleScope.launch {
            try {
                SupabaseClient.deleteRequest(request.id)

                requests.remove(request)
                adapter.notifyDataSetChanged()

                Toast.makeText(
                    this@CustomerSupportActivity,
                    "Διαγράφηκε",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@CustomerSupportActivity,
                    "Δεν διαγράφηκε ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
    private fun loadRequests() {
        lifecycleScope.launch {
            val result = SupabaseClient.getDepartmentRequests()

            requests.clear()
            requests.addAll(result)
            requests.sortBy { it.completed == true }

            adapter.notifyDataSetChanged()
        }
    }
}
