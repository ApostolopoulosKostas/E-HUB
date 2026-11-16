package com.example.e_hub

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.delay


class SearchBar (
    private val searchField: EditText,
    private val productSuggestionScroll : ScrollView,
    private val productSuggestionsList : LinearLayout,
    private val scope: CoroutineScope,
    private val context: Context,
    val onProductSelected:(Product) -> Unit
) {
    private var searchJob: Job? = null

    // ----- WATCH SEARCH TEXT AND RETURN SELECTED PRODUCTS -----
    fun searchListener() {
        searchField.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(text: Editable?) {
            }

            override fun beforeTextChanged(
                text: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = text.toString().trim()

                // CANCEL THE PREVIOUS SEARCH SO OLD RESULTS CAN'T ARRIVE LATE.
                searchJob?.cancel()

                if (query.length >= 4) {
                    searchJob = scope.launch {
                        try {
                            // WAIT A MOMENT; IF THE USER KEEPS TYPING, THIS GETS CANCELLED.
                            delay(300)

                            val products = SupabaseClient.searchProducts(query)

                            productSuggestionsList.removeAllViews()
                            productSuggestionScroll.visibility = View.VISIBLE

                            products.forEach { product ->
                                val suggestion = android.widget.TextView(context)
                                suggestion.setPadding(16, 16, 16, 16)
                                suggestion.setTextColor(Color.BLACK)
                                suggestion.setTypeface(null, Typeface.BOLD)
                                suggestion.text = product.code + " \n " + product.name.trim()

                                productSuggestionsList.addView(suggestion)

                                suggestion.setOnClickListener {
                                    onProductSelected(product)
                                }
                            }
                        } catch (e: CancellationException) {
                            throw e // NORMAL: A NEWER SEARCH REPLACED THIS ONE.
                        } catch (e: Exception) {
                            Toast.makeText(
                                context,
                                "Search failed: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    productSuggestionScroll.visibility = View.GONE
                }
            }
        })
    }
}


