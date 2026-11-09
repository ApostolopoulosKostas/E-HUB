
package com.example.e_hub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc
import io.github.jan.supabase.realtime.Realtime

object SupabaseClient {

    // Shared Supabase client configured with the app's build-time credentials.
    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
    ) {
        install(Postgrest)
        install(Realtime)
    }

    // Finds products by code prefix or by name, returning matching product records.
    suspend fun searchProducts(query: String): List<Product> {
        // Numeric searches use a code prefix; text searches use the database RPC.
        if (query.all { it.isDigit() }) {
            return client.from("products")
                .select {
                    filter {
                        like("code", "$query%")
                    }
                    limit(5) // Keep code-prefix suggestions short.
                }
                .decodeList<Product>()

        } else {
            return client.postgrest.rpc(
                "search_products_by_name",
                mapOf("search_text" to query)
            ).decodeList<Product>()
        }
    }

    // Persists a new request to the requests table.
    suspend fun sendRequest(request: SendRequests) {
        client.from("requests").insert(request)
    }

    // Loads only the requests assigned to the requested department.
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


    suspend fun completedRequest(id:Long){
        client.from("requests").update({
            set("completed",true)
        }){
            filter{
                eq("id",id)
            }
        }
    }
    suspend fun deleteRequest(id: Long) {
        client.from("requests").delete {
            filter {
                eq("id", id)
            }
        }
    }

    suspend fun clearCompletedRequests(department: String){
        client.from("requests").delete {
            filter{
                eq("department",department)
                eq("completed",true)
            }
        }
    }

}