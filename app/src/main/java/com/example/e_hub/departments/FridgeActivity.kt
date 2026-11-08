
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
import android.widget.Button

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fridge)

        val requestsList =
            findViewById<LinearLayout>(R.id.fridgeIncomingRequestsList)

        // Fetch fridge requests asynchronously and render each result as a card.
        lifecycleScope.launch {
            try {
                val requests =
                    SupabaseClient.getDepartmentRequests("Ψυγεία")

                requestsList.removeAllViews()

                requests.forEach { request ->

                    // Create the request card
                    val requestCard = LinearLayout(this@FridgeActivity)
                    requestCard.tag =request.completed== true
                    requestCard.orientation = LinearLayout.VERTICAL



                    requestCard.setPadding(16 ,12, 16, 12)
                    requestCard.setBackgroundColor(Color.WHITE)

                    // First line: product code and name
                    val productText = TextView(this@FridgeActivity)

                    productText.text =
                        "${request.code} - ${request.product_name ?: "Άγνωστο"}"

                    productText.textSize = 16f
                    productText.setTextColor(Color.BLACK)

                    requestCard.addView(productText)

                    // Second line: quantity and location
                    val detailsText = TextView(this@FridgeActivity)

                    detailsText.text =
                        "Ποσότητα: ${request.quantity ?: "-"} | " +
                                "Τοποθεσία: ${request.location ?: "-"}"

                    detailsText.textSize = 14f
                    detailsText.setTextColor(Color.DKGRAY)

                    requestCard.addView(detailsText)

                    // Third line: employee and message
                    val employeeText = TextView(this@FridgeActivity)

                    employeeText.text =
                        "Ζητήθηκε από: ${request.requested_by ?: "-"} | " +
                                "Μήνυμα: ${request.message ?: "-"}"

                    employeeText.textSize = 14f
                    employeeText.setTextColor(Color.DKGRAY)

                    requestCard.addView(employeeText)


                    //Completion status
                    // Fourth line: completion status
                    val statusRow = LinearLayout(this@FridgeActivity)
                    statusRow.orientation = LinearLayout.HORIZONTAL
                    statusRow.gravity = android.view.Gravity.CENTER_VERTICAL

                    val statusText = TextView(this@FridgeActivity)
                    statusText.textSize = 14f

                    val checkButton = TextView(this@FridgeActivity)

                    checkButton.text = "✓"
                    checkButton.textSize = 28f
                    checkButton.setPadding(12, 0, 12, 0)
                   // Current status from supabase
                    if (request.completed == true) {
                        statusText.text = "Ολοκληρώθηκε"
                        statusText.setTextColor(Color.rgb(0, 150, 0))
                        checkButton.setTextColor(Color.rgb(0, 180, 0))
                        checkButton.isClickable = false
                    } else {
                        statusText.text = "Εκκρεμεί"
                        statusText.setTextColor(Color.DKGRAY)
                        checkButton.setTextColor(Color.GRAY)
                        checkButton.setOnClickListener {
                            checkButton.isClickable = false

                            lifecycleScope.launch {
                                try {
                                    SupabaseClient.completedRequest(request.id)
                                    requestCard.tag = true
                                    statusText.text = "Ολοκληρώθηκε"
                                    statusText.setTextColor(Color.rgb(0, 150, 0))
                                    checkButton.setTextColor(Color.rgb(0, 180, 0))

                                } catch (e: Exception) {
                                    checkButton.isClickable = true

                                    Toast.makeText(
                                        this@FridgeActivity,
                                        "Error: ${e.message}",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }

                    }
                    statusRow.addView(statusText)
                    statusRow.addView(checkButton)

                    requestCard.addView(statusRow)
                    // Add the card to the requests list
                    val cardParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    cardParams.bottomMargin = 8

                    requestsList.addView(requestCard, cardParams)
                }

            } catch (e: Exception) {
                Toast.makeText(
                    this@FridgeActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
  // Completed requests button actions and supabae

        val clearCompletedButton =
            findViewById<Button>(R.id.clearCompletedButton)
        clearCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    SupabaseClient.clearCompletedRequests("Ψυγεία")
                    for (i in requestsList.childCount - 1 downTo 0) {
                        val card = requestsList.getChildAt(i)
                        if (card.tag == true) {
                            requestsList.removeViewAt(i)
                        }
                    }

                    Toast.makeText(
                        this@FridgeActivity,
                        "Τα ολοκληρωμένα αιτήματα διαγράφηκαν",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {
                    Toast.makeText(
                        this@FridgeActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }


    }
}
