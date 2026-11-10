
package com.example.e_hub.departments

import android.graphics.Color
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.e_hub.R
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.launch

class WarehouseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.warehouse)

        val root = findViewById<View>(R.id.warehouseRootLayout)

        // ----- KEEP CONTENT CLEAR OF THE SYSTEM BARS -----
        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->

            val systemBars = insets.getInsets(
                WindowInsetsCompat.Type.systemBars()
            )

            view.setPadding(
                0,
                systemBars.top,
                0,
                systemBars.bottom
            )

            insets
        }

        val requestsList =
            findViewById<LinearLayout>(R.id.warehouseIncomingRequestsList)

        // ----- LOAD REQUESTS FOR THIS DEPARTMENT -----

        lifecycleScope.launch {
            try {
                val requests =
                    SupabaseClient.getDepartmentRequests("Αποθήκη")

                requestsList.removeAllViews()

                requests.forEach { request ->

                    // ----- REQUEST CARD -----

                    val requestCard = LinearLayout(this@WarehouseActivity)
                    requestCard.tag = request.completed == true // USED TO REMOVE COMPLETED CARDS FROM THE SCREEN.
                    requestCard.orientation = LinearLayout.VERTICAL

                    requestCard.setPadding(16, 12, 16, 12)
                    requestCard.setBackgroundColor(Color.WHITE)

                    // ----- PRODUCT INFORMATION -----

                    val productText = TextView(this@WarehouseActivity)

                    productText.text =
                        "${request.code} - ${request.product_name ?: "Άγνωστο"}"

                    productText.textSize = 16f
                    productText.setTextColor(Color.BLACK)

                    requestCard.addView(productText)

                    // ----- QUANTITY AND LOCATION -----

                    val detailsText = TextView(this@WarehouseActivity)

                    detailsText.text =
                        "Ποσότητα: ${request.quantity ?: "-"} | " +
                                "Τοποθεσία: ${request.location ?: "-"}"

                    detailsText.textSize = 14f
                    detailsText.setTextColor(Color.DKGRAY)

                    requestCard.addView(detailsText)

                    // ----- EMPLOYEE AND MESSAGE -----

                    val employeeText = TextView(this@WarehouseActivity)

                    employeeText.text =
                        "Ζητήθηκε από: ${request.requested_by ?: "-"} | " +
                                "Μήνυμα: ${request.message ?: "-"}"

                    employeeText.textSize = 14f
                    employeeText.setTextColor(Color.DKGRAY)

                    requestCard.addView(employeeText)

                    // ----- COMPLETION STATUS -----

                    val statusRow = LinearLayout(this@WarehouseActivity)
                    statusRow.orientation = LinearLayout.HORIZONTAL
                    statusRow.gravity = Gravity.CENTER_VERTICAL

                    val statusText = TextView(this@WarehouseActivity)
                    statusText.textSize = 14f

                    val checkButton = TextView(this@WarehouseActivity)

                    checkButton.text = "✓"
                    checkButton.textSize = 28f
                    checkButton.setPadding(12, 0, 12, 0)

                    // ----- CURRENT REQUEST STATUS -----

                    if (request.completed == true) {

                        statusText.text = "Ολοκληρώθηκε"
                        statusText.setTextColor(Color.rgb(0, 150, 0))

                        checkButton.setTextColor(Color.rgb(0, 180, 0))
                        checkButton.isClickable = false

                    } else {

                        statusText.text = "Εκκρεμεί"
                        statusText.setTextColor(Color.DKGRAY)

                        checkButton.setTextColor(Color.GRAY)

                        // ----- MARK REQUEST AS COMPLETED -----

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
                                        this@WarehouseActivity,
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

                    // ----- ADD CARD TO REQUEST LIST -----

                    val cardParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    )

                    cardParams.bottomMargin = 8

                    requestsList.addView(requestCard, cardParams)
                }

            } catch (e: Exception) {

                Toast.makeText(
                    this@WarehouseActivity,
                    "Error: ${e.message}",
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        // ----- CLEAR COMPLETED REQUESTS -----

        val clearCompletedButton =
            findViewById<Button>(R.id.clearCompletedButton)

        clearCompletedButton.setOnClickListener {
            lifecycleScope.launch {
                try {
                    SupabaseClient.clearCompletedRequests("Αποθήκη")

                    for (i in requestsList.childCount - 1 downTo 0) {
                        val card = requestsList.getChildAt(i)

                        if (card.tag == true) {
                            requestsList.removeViewAt(i)
                        }
                    }

                    Toast.makeText(
                        this@WarehouseActivity,
                        "Τα ολοκληρωμένα αιτήματα διαγράφηκαν",
                        Toast.LENGTH_SHORT
                    ).show()

                } catch (e: Exception) {

                    Toast.makeText(
                        this@WarehouseActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}
