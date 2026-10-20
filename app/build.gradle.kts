import java.util.Properties
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.serialization)
}




android {
    namespace = "com.example.e_hub"
    compileSdk {
        version = release(37)
    }
    buildFeatures {
        buildConfig = true
    }
    val localProperties = Properties().apply {
        load(rootProject.file("local.properties").inputStream())
    }

    defaultConfig {
        applicationId = "com.example.e_hub"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        buildConfigField(
            "String",
            "SUPABASE_URL",
            "\"${localProperties.getProperty("SUPABASE_URL")}\""
        )
        buildConfigField(
            "String",
            "SUPABASE_PUBLISHABLE_KEY",
            "\"${localProperties.getProperty("SUPABASE_PUBLISHABLE_KEY")}\""
        )

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = true
                packageScope = setOf("androidx.**", "kotlin.**", "kotlinx.**")
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

}


dependencies {
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation(platform(libs.supabase.bom))
    implementation(libs.supabase.postgrest)
    implementation(libs.ktor.client.android)

}