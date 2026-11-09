package com.example.e_hub.departments

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.e_hub.ReceivedRequests

class CustomerSupportAdapter(
    private val requests: MutableList<ReceivedRequests>,
    private val onDeleteClick: (ReceivedRequests) -> Unit
):
    RecyclerView.Adapter<CustomerSupportAdapter.RequestViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RequestViewHolder {
        val card = LinearLayout(parent.context)
        val params = RecyclerView.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        params.setMargins(0, 0, 0, 16)
        card.layoutParams = params
        return RequestViewHolder(card)

    }

    override fun onBindViewHolder(
        holder: RequestViewHolder,
        position: Int
    ) {
        val request = requests[position]

        holder.deleteButton.setOnClickListener {
            onDeleteClick(request)
        }

        holder.productText.text =
            "${request.code} - ${request.product_name ?: "Άγνωστο"}"
        holder.detailsText.text =
            "Ποσότητα: ${request.quantity ?: "-"} | " +
                    "Ράμπα: ${request.location ?: "-"}"
        holder.employeeText.text =
            "Ζητήθηκε από: ${request.requested_by ?: "-"} | " +
                    "Μήνυμα: ${request.message ?: "-"}"

        holder.departmentText.text =
            "Τμήμα: ${request.department}"

        if (request.completed == true) {
            holder.statusText.text = "Ολοκληρώθηκε"
            holder.statusText.setTextColor(Color.rgb(0, 150, 0))
        } else {
            holder.statusText.text = "Εκκρεμεί"
            holder.statusText.setTextColor(Color.DKGRAY)
        }
    }

    override fun getItemCount(): Int {
         return requests.size
    }

    class RequestViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val productText = TextView(itemView.context)
        val detailsText = TextView(itemView.context)
        val employeeText = TextView(itemView.context)
        val departmentText = TextView(itemView.context)
        val statusText = TextView(itemView.context)
        val deleteButton = Button(itemView.context)

        init {
            val container = itemView as LinearLayout
            container.addView(departmentText)



            productText.textSize =16f
            productText.setTextColor(Color.BLACK)
            productText.setTypeface(null,android.graphics.Typeface.BOLD)

            container.orientation = LinearLayout.VERTICAL
            container.setPadding(12, 10,12 , 10)
            container.setBackgroundColor(Color.WHITE)
            deleteButton.text = "Διαγραφή"

            container.addView(productText)
            container.addView(detailsText)
            container.addView(employeeText)
            container.addView(statusText)
            container.addView(deleteButton)




        }

    }
}