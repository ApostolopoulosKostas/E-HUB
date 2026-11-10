package com.example.e_hub

import kotlinx.serialization.Serializable

// ----- REQUEST DATA SENT TO SUPABASE -----
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

