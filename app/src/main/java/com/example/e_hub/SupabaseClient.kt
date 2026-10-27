
package com.example.e_hub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.rpc

object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
    ) {
        install(Postgrest)
    }

    suspend fun searchProducts(query :String):List<Product> {
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
    


}
