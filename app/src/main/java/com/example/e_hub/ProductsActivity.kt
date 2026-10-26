package com.example.e_hub
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope


class ProductsActivity : AppCompatActivity (){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.products)



             //   -------------SEARCH BAR -----------------------
        val productSearch = findViewById<EditText>(R.id.productsearch)//search
        val productSuggestionScroll = findViewById<ScrollView>(R.id.productSuggestionsScroll)
        val productSuggestionsList = findViewById<LinearLayout>(R.id.productSuggestionsList)
        val productsResultList = findViewById<LinearLayout>(R.id.productsResultsList)



        val searchBar = SearchBar(
            searchField = productSearch,
            productSuggestionScroll=productSuggestionScroll,
            productSuggestionsList =productSuggestionsList,
            scope =lifecycleScope,
            context = this,

            onProductSelected ={product ->
                val productRow = LinearLayout(this)
                productRow.orientation = LinearLayout.HORIZONTAL

                val productBackground = android.graphics.drawable.GradientDrawable()
                productBackground.setColor(Color.rgb(110, 78, 58))
                productBackground.setCornerRadius(12f)
                productBackground.setStroke(4, Color.GRAY)
                productRow.background = productBackground
                productRow.setPadding(16, 16, 16, 16)


                val productInfo = LinearLayout(this)
                productInfo.orientation = LinearLayout.VERTICAL


                val productCode =TextView(this)
                productCode.text = product.code
                productCode.setTextColor(Color.YELLOW)

                val productName =TextView (this)
                productName.text = product.name
                productName.setTextColor(Color.WHITE)

                val productUrl = TextView(this)
                productUrl.text = product.url
                productUrl.setTextColor(Color.RED)
                productUrl.setOnClickListener {
                    val intent = Intent (Intent.ACTION_VIEW, Uri.parse(product.url))
                    startActivity(intent)
                }
                productInfo.addView(productCode)
                productInfo.addView(productName)
                productInfo.addView(productUrl)
                productRow.addView(
                    productInfo,
                    LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f)
                )
                val deleteButton= Button(this)
                deleteButton.text="Delete"
                deleteButton.setOnClickListener { productsResultList.removeView(productRow) }

                productRow.addView(deleteButton)
                productsResultList.addView(productRow)







            }
        )
        searchBar.searchListener()
    }
}