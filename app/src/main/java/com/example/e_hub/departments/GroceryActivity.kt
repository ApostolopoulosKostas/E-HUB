package com.example.e_hub.departments

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.launch

class GroceryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grocery)

        val requestsList = findViewById<LinearLayout>(
            R.id.groceryIncomingRequestsList
        )

        lifecycleScope.launch {
            try {
                val requests = SupabaseClient.getDepartmentRequests("Μανάβικο")

                requestsList.removeAllViews()

                requests.forEach { request ->

                    val requestCard = TextView(this@GroceryActivity)

                    requestCard.text = """
                        Κωδικός: ${request.code}
                        Προϊόν: ${request.product_name ?: "Άγνωστο"}
                        Ποσότητα: ${request.quantity ?: "-"}
                        Τοποθεσία: ${request.location ?: "-"}
                        Μήνυμα: ${request.message ?: "-"}
                        Ζητήθηκε από: ${request.requested_by ?: "-"}
                    """.trimIndent()

                    requestCard.setPadding(20, 20, 20, 20)
                    requestCard.textSize = 16f
                    requestCard.setTextColor(android.graphics.Color.BLACK)
                    requestCard.setBackgroundColor(android.graphics.Color.WHITE)

                    requestsList.addView(requestCard)
                }

            } catch (e: Exception) {
                android.widget.Toast.makeText(
                    this@GroceryActivity,
                    "Error: ${e.message}",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }
}