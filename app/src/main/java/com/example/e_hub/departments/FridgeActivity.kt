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
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.e_hub.R
import com.example.e_hub.SupabaseClient
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class FridgeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fridge)

        val root = findViewById<View>(R.id.fridgeRootLayout)

        // ----- KEEP CONTENT CLEAR OF THE SYSTEM BARS -----

        ViewCompat.setOnApplyWindowInsetsListener(root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())

            view.setPadding(0, systemBars.top, 0, systemBars.bottom)
            insets
        }

        val requestsList =
            findViewById<LinearLayout>(R.id.fridgeIncomingRequestsList)

        // ----- CLEAR COMPLETED REQUESTS BUTTON -----

        val clearCompletedButton =
            findViewById<Button>(R.id.clearCompletedButton)

        clearCompletedButton.setOnClickListener {

            lifecycleScope.launch {
                try {
                    SupabaseClient.clearCompletedRequests("Ψυγεία")

                    // Remove completed cards from the screen.

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

                } catch (e: CancellationException) {
                    throw e

                } catch (e: Exception) {

                    Toast.makeText(
                        this@FridgeActivity,
                        "Δεν ήταν δυνατή η διαγραφή των ολοκληρωμένων αιτημάτων.",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }

        // ----- LOAD AND REFRESH DEPARTMENT REQUESTS -----

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

                while (true) {

                    try {
                        val requests =
                            SupabaseClient.getDepartmentRequests("Ψυγεία")

                        // Clear the old cards before displaying updated requests.

                        requestsList.removeAllViews()

                        requests.forEach { request ->

                            // ----- REQUEST CARD -----

                            val requestCard = LinearLayout(this@FridgeActivity)

                            requestCard.tag =
                                request.completed == true // Identifies completed cards.

                            requestCard.orientation = LinearLayout.VERTICAL

                            requestCard.setPadding(16, 12, 16, 12)
                            requestCard.setBackgroundColor(Color.WHITE)

                            // ----- PRODUCT INFORMATION -----

                            val productText = TextView(this@FridgeActivity)

                            productText.text =
                                "${request.code} - ${request.product_name ?: "Άγνωστο"}"

                            productText.textSize = 16f
                            productText.setTextColor(Color.BLACK)

                            requestCard.addView(productText)

                            // ----- QUANTITY AND LOCATION -----

                            val detailsText = TextView(this@FridgeActivity)

                            detailsText.text =
                                "Ποσότητα: ${request.quantity ?: "-"} | " +
                                        "Ράμπα: ${request.location ?: "-"}"

                            detailsText.textSize = 14f
                            detailsText.setTextColor(Color.DKGRAY)

                            requestCard.addView(detailsText)

                            // ----- EMPLOYEE AND MESSAGE -----

                            val employeeText = TextView(this@FridgeActivity)

                            employeeText.text =
                                "Ζητήθηκε από: ${request.requested_by ?: "-"} | " +
                                        "Μήνυμα: ${request.message ?: "-"}"

                            employeeText.textSize = 14f
                            employeeText.setTextColor(Color.DKGRAY)

                            requestCard.addView(employeeText)

                            // ----- COMPLETION STATUS -----

                            val statusRow = LinearLayout(this@FridgeActivity)

                            statusRow.orientation = LinearLayout.HORIZONTAL
                            statusRow.gravity = Gravity.CENTER_VERTICAL

                            val statusText = TextView(this@FridgeActivity)
                            statusText.textSize = 14f

                            val checkButton = TextView(this@FridgeActivity)

                            checkButton.text = "✓"
                            checkButton.textSize = 28f
                            checkButton.setPadding(12, 0, 12, 0)

                            // ----- DISPLAY CURRENT REQUEST STATUS -----

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

                                            // Update the card immediately.

                                            requestCard.tag = true

                                            statusText.text = "Ολοκληρώθηκε"
                                            statusText.setTextColor(Color.rgb(0, 150, 0))

                                            checkButton.setTextColor(Color.rgb(0, 180, 0))

                                        } catch (e: CancellationException) {
                                            throw e

                                        } catch (e: Exception) {

                                            // Allow another attempt if the update fails.

                                            checkButton.isClickable = true

                                            Toast.makeText(
                                                this@FridgeActivity,
                                                "Δεν ήταν δυνατή η ενημέρωση του αιτήματος. Δοκίμασε ξανά.",
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

                    } catch (e: CancellationException) {
                        throw e

                    } catch (e: Exception) {

                        Toast.makeText(
                            this@FridgeActivity,
                            "Δεν ήταν δυνατή η φόρτωση των αιτημάτων. Έλεγξε τη σύνδεση.",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                    // ----- REFRESH REQUESTS EVERY 10 SECONDS -----

                    delay(10_000)
                }
            }
        }
    }
}