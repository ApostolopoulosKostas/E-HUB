
package com.example.e_hub.departments

import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_hub.Product
import com.example.e_hub.R
import com.example.e_hub.ReceivedRequests
import com.example.e_hub.SendRequests
import com.example.e_hub.SearchBar
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CustomerSupportActivity : AppCompatActivity() {

    // ----- REQUEST LIST -----
    private val requests = mutableListOf<ReceivedRequests>()
    private lateinit var adapter: CustomerSupportAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_support)

        val productSearch = findViewById<EditText>(R.id.customerSupportSearch)
        val productSuggestionScroll =
            findViewById<ScrollView>(R.id.customerSupSuggestionsScroll)
        val productSuggestionsList =
            findViewById<LinearLayout>(R.id.customerSupSuggestionsList)

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
                productSuggestionScroll.visibility = View.GONE
            }
        )
        searchBar.searchListener()

        // ----- INPUT FIELDS -----
        val quantityInput = findViewById<EditText>(R.id.quantityInput)
        val locationInput = findViewById<EditText>(R.id.locationInput)
        val messageInput = findViewById<EditText>(R.id.messageInput)
        val selectDepartment = findViewById<Spinner>(R.id.selectDepartment)

        val departments = listOf("Ψυγεία", "Μανάβικο", "Αποθήκη")

        // ----- REQUEST LIST -----
        val insertedProducts = findViewById<RecyclerView>(R.id.insertedProducts)

        insertedProducts.layoutManager = LinearLayoutManager(this)

        adapter = CustomerSupportAdapter(requests) { request ->
            deleteRequest(request)
        }

        insertedProducts.adapter = adapter

        // ----- LOAD REQUESTS (EVERY 10 SECONDS) -----
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                while (true) {
                    loadRequests()
                    delay(10_000)
                }
            }
        }

        // ----- CLEAR COMPLETED REQUESTS -----
        val clearCompletedButton =
            findViewById<Button>(R.id.clearCompletedRequestsButton)

        clearCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    SupabaseClient.clearCompletedRequests()
                    loadRequests()

                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Τα ολοκληρωμένα αιτήματα διαγράφηκαν.",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Δεν ήταν δυνατή η διαγραφή των αιτημάτων: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // ----- SEND REQUEST -----
        val sendRequest = findViewById<Button>(R.id.sendRequest)

        sendRequest.setOnClickListener {
            val code = selectedProduct?.code ?: productSearch.text.toString()
            val name = selectedProduct?.name ?: ""
            val url = selectedProduct?.url ?: ""

            if (code.length != 7 || !code.all { it.isDigit() }) {
                Toast.makeText(
                    this,
                    "Ο κωδικός πρέπει να αποτελείται από 7 ψηφία.",
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

                    // ----- RESET SEARCH AFTER SUCCESSFUL SEND -----
                    selectedProduct = null
                    productSearch.text.clear()
                    productSuggestionScroll.visibility = View.GONE
                    productSuggestionsList.removeAllViews()

                    loadRequests()

                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Το αίτημα στάλθηκε με επιτυχία.",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@CustomerSupportActivity,
                        "Δεν ήταν δυνατή η αποστολή του αιτήματος: ${e.message}",
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
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                    "Το αίτημα διαγράφηκε.",
                    Toast.LENGTH_SHORT
                ).show()

            } catch (e: Exception) {
                Toast.makeText(
                    this@CustomerSupportActivity,
                    "Δεν ήταν δυνατή η διαγραφή: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    // ----- LOAD REQUESTS -----
    private suspend fun loadRequests() {
        try {
            val result = SupabaseClient.getDepartmentRequests()

            requests.clear()
            requests.addAll(result)

            adapter.notifyDataSetChanged()

        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Toast.makeText(
                this@CustomerSupportActivity,
                "Δεν ήταν δυνατή η φόρτωση των αιτημάτων. Ελέγξτε τη σύνδεσή σας.",
                Toast.LENGTH_LONG
            ).show()
        }
    }
}
