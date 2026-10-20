package com.example.e_hub

import android.R
import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val code : String,
    val name: String,
    val  url: String
)