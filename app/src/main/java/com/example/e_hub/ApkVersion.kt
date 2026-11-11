package com.example.e_hub

import kotlinx.serialization.Serializable


@Serializable
data class ApkVersion (
    val version_code:Int,
    val version_name:String,
    val apk_path:String,
    val release_notes: String?


)