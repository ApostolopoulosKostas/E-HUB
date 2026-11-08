package com.example.e_hub


import kotlinx.serialization.Serializable

// Product fields returned by Supabase and displayed in search results.
@Serializable
data class Product(
    val code : String,
    val name: String,
    val  url: String
)