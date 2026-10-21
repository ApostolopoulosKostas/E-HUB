
package com.example.e_hub

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.from
import java.util.Locale.filter

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
        return client.from("products")
            .select {
                filter {
                    ilike("name","%$query%")
                }
                limit(5)
            }.decodeList<Product>()


    }


}
