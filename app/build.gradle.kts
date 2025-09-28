plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")            // gunakan KAPT untuk Hilt & Room
    id("com.google.dagger.hilt.android")      // plugin Hilt (tanpa versi di sini)
}

android {
    namespace = "com.alung.notamu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.alung.notamu"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions { jvmTarget = "17" }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    // Versi lib
    val room = "2.6.1"
    val lifecycle = "2.8.3"
    val nav = "2.7.7"
    val hilt = "2.51.1"

    // Core UI
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.0.0")          // ✅ tambahkan
    implementation("androidx.fragment:fragment-ktx:1.8.2")          // ✅ rekomendasi
    implementation("javax.inject:javax.inject:1")                    // ✅ cukup sekali

    // Room
    implementation("androidx.room:room-runtime:$room")
    implementation("androidx.room:room-ktx:$room")
    kapt("androidx.room:room-compiler:$room")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$nav")
    implementation("androidx.navigation:navigation-ui-ktx:$nav")

    // Hilt (SATU versi konsisten)
    implementation("com.google.dagger:hilt-android:$hilt")
    kapt("com.google.dagger:hilt-compiler:$hilt")

    // (Opsional) Hilt helpers untuk Navigation
    // implementation("androidx.hilt:hilt-navigation-fragment:1.2.0")
}


kapt {
    correctErrorTypes = true
    showProcessorStats = true
    // verbose = true // aktifkan jika perlu log detail
}
