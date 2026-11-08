package com.example.e_hub

import kotlinx.serialization.Serializable

// Request fields sent to Supabase when an employee asks a department for a product.
@Serializable
data class SendRequests (
    val code : String,
    val product_name : String?,
    val product_url : String?,
    val quantity : Double?,
    val location :String?,
    val message :String?,
    val department : String,
    val requested_by : String?
    )

