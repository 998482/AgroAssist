plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)

        id ("kotlin-kapt")



}

android {
    namespace = "com.example.agroinnovexa20"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.agroinnovexa20"
        minSdk = 29
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    implementation(libs.firebase.auth)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val nav_version = "2.9.3"
    implementation("androidx.navigation:navigation-compose:$nav_version")
    dependencies {
        // Jetpack DataStore (Preferences)
        implementation ("androidx.datastore:datastore-preferences:1.1.0")

        // Coroutines (if not already added)
        implementation  ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    }
    // Material Icons for Compose
    implementation ("androidx.compose.material:material-icons-extended:1.5.3")

    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //✅ Retrofit 2.11.0 – newest stable release
    //✅ Gson Converter 2.11.0 – matches Retrofit version
    //✅ OkHttp 4.12.0 – works perfectly with Retrofit 2.11.0
    //OkHttp Logging Interceptor, very useful for debugging API responses:
    implementation("io.coil-kt:coil:2.6.0")
    // For Jetpack Compose
    implementation("io.coil-kt:coil-compose:2.6.0")


    implementation("androidx.activity:activity-compose:1.7.2")

    dependencies {

        // Room
        implementation("androidx.room:room-runtime:2.6.1")
        implementation ("androidx.room:room-ktx:2.6.1")
        kapt ("androidx.room:room-compiler:2.6.1")

    }
    dependencies {

        // WorkManager
        implementation ("androidx.work:work-runtime-ktx:2.8.1")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")


    }






}