package com.example.e_hub

import android.content.Context
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsBytes
import io.ktor.http.isSuccess
import java.io.File

object ApkDownloader

private val client = HttpClient { Android } //ktor ,CRREATES A REUSABLE HTTP CLIENT

suspend fun downloadApk(context: Context,url: String){
    val apkFile = File(context.getExternalFilesDir(null),"e-hub-update.apk")//private external files folder.
    val response =client. get(url)//request the apk from SupaBase
    if (!response.status.isSuccess()){
        throw Exception("Apk download failed :${response.status}")
        val apkBytes =response.body<ByteArray>()//Takes the actual APK data that Supabase sent us and stores it in the variable apkBytes
        //1client.get(url) asks Supabase to send the APK.2 response receives the server's response.3 response.status.isSuccess() checks whether the request succeeded.4 response.body<ByteArray>() reads the APK's binary data. 5apkBytes holds that data temporarily in the phone's memory.
       apkFile.writeBytes(apkBytes)//Take the APK data from memory and write it to the phone as ehub-update.apk
        if (!apkFile.exists())
            throw Exception("apk file was not created")
    }




}