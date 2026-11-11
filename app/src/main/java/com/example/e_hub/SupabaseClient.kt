package com.example.e_hub



import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime
import io.github.jan.supabase.storage.Storage

object SupabaseClient {

    // ----- SHARED SUPABASE CLIENT -----
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
        install(Storage)

    }

    // ----- SEARCH PRODUCTS BY CODE OR NAME -----
    suspend fun searchProducts(query: String): List<Product> {

        if (query.all { it.isDigit() }) {
            return client.from("products")
                .select {
                    filter {
                        like("code", "$query%")
                    }
                    limit(5)
                }
                .decodeList<Product>()

        } else {
            return client.postgrest.rpc(
                "search_products_by_name",
                mapOf("search_text" to query)
            ).decodeList<Product>()
        }
    }

    // ----- INSERT A NEW REQUEST -----
    suspend fun sendRequest(request: SendRequests) {
        client.from("requests").insert(request)
    }

    // ----- LOAD REQUESTS, OPTIONALLY FILTERED BY DEPARTMENT -----
    suspend fun getDepartmentRequests(
        department: String? = null
    ): List<ReceivedRequests> {

        return client.from("requests")
            .select {
                if (department != null) {
                    filter {
                        eq("department", department)
                    }
                }
            }
            .decodeList<ReceivedRequests>()
    }

    // ----- MARK REQUEST AS COMPLETED -----
    suspend fun completedRequest(id: Long) {
        client.from("requests").update({
            set("completed", true)
        }) {
            filter {
                eq("id", id)
            }
        }
    }

    // ----- DELETE ONE REQUEST -----
    suspend fun deleteRequest(id: Long) {
        client.from("requests").delete {
            filter {
                eq("id", id)
            }
        }
    }

    // ----- CLEAR COMPLETED REQUESTS -----
    suspend fun clearCompletedRequests(department: String? = null) {
        client.from("requests").delete {
            filter {
                if (department != null) {
                    eq("department", department)
                }
                eq("completed", true)
            }
        }
    }
}