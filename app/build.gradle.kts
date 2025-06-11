plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
}

val openaiKey = File(rootDir, "local.properties").reader().useLines { lines ->
    lines.firstOrNull { it.startsWith("OPENAI_API_KEY") }
        ?.split("=")
        ?.getOrNull(1)
        ?.replace("\"", "")?.trim()
} ?: throw IllegalArgumentException("OPENAI_API_KEY not found in local.properties")



android {
    buildFeatures {
        compose = true
        buildConfig = true
    }

    namespace = "com.example.floplusnew"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.floplusnew"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        buildConfigField("String", "OPENAI_API_KEY", "\"$openaiKey\"")

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    implementation("androidx.navigation:navigation-compose:2.7.3")

    implementation("com.google.firebase:firebase-auth-ktx:22.3.1")

    // Retrofit & Gson For CycleBot
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

// OkHttp (for auth headers) CycleBot
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    //CycleBot for loggin in LogCat (see API logs)
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")


}
