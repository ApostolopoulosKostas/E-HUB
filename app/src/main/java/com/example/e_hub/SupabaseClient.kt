
package com.example.e_hub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from


object SupabaseClient {

    val client = createSupabaseClient(
        supabaseUrl = BuildConfig.SUPABASE_URL,
        supabaseKey = BuildConfig.SUPABASE_PUBLISHABLE_KEY
    ) {
        install(Postgrest)
    }
    //Allows the function to perform a suspending operation,
    // such as waiting for a network request, without blocking the coroutine.

    suspend fun searchProducts(query :String):List<Product> {
        if (query.all { it.isDigit() }) {
            return client.from("products")
                .select{
                    filter{
                        like("code","%$query%")
                    }
                    limit(5)
                }.decodeList<Product>()

        } else {

            return client.from("products")
                .select {
                    filter {
                        ilike("name", "%$query%")
                    }
                    limit(5)
                }.decodeList<Product>()
        }
    }
    


}
