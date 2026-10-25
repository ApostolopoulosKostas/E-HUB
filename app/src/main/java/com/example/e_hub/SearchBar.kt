package com.example.e_hub

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class SearchBar (
    private val searchField: EditText,
    private val productSuggestionScroll : ScrollView,
    private val productSuggestionsList : LinearLayout,
    private val scope: CoroutineScope,
    private val context: Context,
    val onProductSelected:(Product) -> Unit
) {

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

            override  fun onTextChanged(
                text: CharSequence?,
                start: Int,
                before: Int,
                count: Int
            ) {
                val query = text.toString().trim()
                if (query.length >= 4) {
                    productSuggestionsList.removeAllViews()
                    productSuggestionScroll.visibility = View.VISIBLE
                    scope.launch {
                        val products = SupabaseClient.searchProducts(query)
                        products.forEach { product ->
                            val suggestion = android.widget.TextView(context)

                            suggestion.text = product.code + " - " + product.name.trim()


                            productSuggestionsList.addView(suggestion)

                            suggestion.setOnClickListener {
                                onProductSelected(product)
                            }
                        }
                    }


                }else{
                    productSuggestionScroll.visibility= View.GONE
                }


            }
        })
    }
}




