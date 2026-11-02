package com.example.e_hub

import kotlinx.serialization.Serializable

@Serializable
data class ReceivedRequests(
    val id: Long,
    val code: String,
    val product_name: String?,
    val product_url: String?,
    val quantity: Double?,
    val location: String?,
    val message: String?,
    val department: String,
    val requested_by: String?,
    val completed: Boolean?,
    val created_at: String?,
    val completed_by: String?,
    val completed_at: String?
)