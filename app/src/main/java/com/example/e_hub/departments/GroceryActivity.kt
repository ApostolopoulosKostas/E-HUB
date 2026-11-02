
package com.example.e_hub.departments

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.launch

class GroceryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery)

        val requestsList =
            findViewById<LinearLayout>(R.id.groceryIncomingRequestsList)

        lifecycleScope.launch {
            try {
                val requests =
                    SupabaseClient.getDepartmentRequests("Μανάβικο")

                requestsList.removeAllViews()

                requests.forEach { request ->

                    val requestCard = LinearLayout(this@GroceryActivity)
                    requestCard.orientation = LinearLayout.VERTICAL

                    requestCard.setPadding(16, 12, 16, 12)
                    requestCard.setBackgroundColor(Color.WHITE)

                    // First line: product code and name
                    val productText = TextView(this@GroceryActivity)

                    productText.text =
                        "${request.code} - ${request.product_name ?: "Άγνωστο"}"

                    productText.textSize = 16f
                    productText.setTextColor(Color.BLACK)

                    requestCard.addView(productText)

                    // Second line: quantity and location
                    val detailsText = TextView(this@GroceryActivity)

                    detailsText.text =
                        "Ποσότητα: ${request.quantity ?: "-"} | Τοποθεσία: ${request.location ?: "-"}"

                    detailsText.textSize = 14f
                    detailsText.setTextColor(Color.DKGRAY)

                    requestCard.addView(detailsText)

                    // Third line: employee and message
                    val employeeText = TextView(this@GroceryActivity)

                    employeeText.text =
                        "Ζητήθηκε από: ${request.requested_by ?: "-"} | Μήνυμα: ${request.message ?: "-"}"

                    employeeText.textSize = 14f
                    employeeText.setTextColor(Color.DKGRAY)

                    requestCard.addView(employeeText)

                    val cardParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    cardParams.bottomMargin = 8

                    requestsList.addView(requestCard, cardParams)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@GroceryActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}
