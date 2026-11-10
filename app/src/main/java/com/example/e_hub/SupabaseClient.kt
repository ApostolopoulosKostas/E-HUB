
package com.example.e_hub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {

    // ----- SHARED SUPABASE CLIENT -----
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }

    // ----- SEARCH PRODUCTS BY CODE OR NAME -----
    suspend fun searchProducts(query: String): List<Product> {
        // USE A CODE PREFIX FOR DIGITS; USE THE DATABASE SEARCH FUNCTION FOR TEXT.
        if (query.all { it.isDigit() }) {
            return client.from("products")
                .select {
                    filter {
                        like("code", "$query%")
                    }
                    limit(5) // LIMIT CODE SEARCH SUGGESTIONS TO FIVE RESULTS.
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
    suspend fun getDepartmentRequests(department: String ?=null): List<ReceivedRequests> {
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


    // MARK THE REQUEST WITH THIS ID AS COMPLETED IN SUPABASE.
    suspend fun completedRequest(id:Long){
        client.from("requests").update({
            set("completed",true)
        }){
            filter{
                eq("id",id)
            }
        }
    }
    // DELETE THE REQUEST WITH THIS ID FROM SUPABASE.
    suspend fun deleteRequest(id: Long) {
        client.from("requests").delete {
            filter {
                eq("id", id)
            }
        }
    }

    // DELETE COMPLETED REQUESTS FOR THE SPECIFIED DEPARTMENT.
    suspend fun clearCompletedRequests(department: String){
        client.from("requests").delete {
            filter{
                eq("department",department)
                eq("completed",true)
            }
        }
    }

}