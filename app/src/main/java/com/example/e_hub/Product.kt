package com.example.e_hub


import kotlinx.serialization.Serializable

// ----- PRODUCT DATA RETURNED BY SUPABASE -----
@Serializable
data class Product(
    val code : String,
    val name: String,
    val  url: String
)