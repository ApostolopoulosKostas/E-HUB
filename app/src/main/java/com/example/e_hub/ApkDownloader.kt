
package com.example.e_hub

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.http.isSuccess
import java.io.File

object ApkDownloader {

    private val client = HttpClient(Android) // Ktor creates a reusable HTTP client

    suspend fun downloadApk(context: Context, url: String): File {

        val apkFile =
            File(context.getExternalFilesDir(null), "e-hub-update.apk") // Private external files folder.

        val response = client.get(url) // Request the APK from Supabase

        if (!response.status.isSuccess()) {
            throw Exception("APK download failed: ${response.status}")
        }

        val apkBytes =
            response.body<ByteArray>() // Takes the actual APK data that Supabase sent us and stores it in the variable apkBytes

        // 1. client.get(url) asks Supabase to send the APK.
        // 2. response receives the server's response.
        // 3. response.status.isSuccess() checks whether the request succeeded.
        // 4. response.body<ByteArray>() reads the APK's binary data.
        // 5. apkBytes holds that data temporarily in the phone's memory.

        apkFile.writeBytes(apkBytes) // Take the APK data from memory and write it to the phone as e-hub-update.apk

        if (!apkFile.exists()) {
            throw Exception("APK file was not created")
        }

        if (apkFile.length() == 0L) {
            throw Exception("Downloaded APK is empty") // Verify that the file isn't empty. It returns the file size in bytes. 0L means zero bytes. L tells Kotlin that 0 is a Long value, which matches the type returned by length().
        }

        return apkFile // Send the downloaded file back to whoever called this function.
    }

    // OPENING ANDROID'S INSTALLATION SCREEN.
    fun installApk(context: Context, apkFile: File) {

        // ------------CHECK IF E-HUB IS ALLOWED TO INSTALL APK FILES.
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O &&
            !context.packageManager.canRequestPackageInstalls()
        ) {
            val settingsIntent = Intent(
                android.provider.Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES
            )

            settingsIntent.data =
                android.net.Uri.parse("package:${context.packageName}")

            context.startActivity(settingsIntent)

            return
        }
        //------------------------------------------------------------------------------------

        val apkUri = FileProvider.getUriForFile( // FileProvider safely shares files. getUriForFile() creates an address for our APK.
            context,
            "${context.packageName}.fileprovider",
            apkFile
        )
        val installIntent =Intent (Intent.ACTION_VIEW)
        installIntent.setDataAndType(
            apkUri, // location of the app
            "application/vnd.android.package-archive" //type of the file MIME type
        )
        installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//allow the app handling installation request to read the apk at this url
        context.startActivity(installIntent)   //open installation screen
    }

}
