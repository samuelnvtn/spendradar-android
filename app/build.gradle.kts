plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt.android)
}

android {
    namespace = "com.pef.mendelu.novotnysamuel.spendradar"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.pef.mendelu.novotnysamuel.spendradar"
        minSdk = 28
        targetSdk = 36
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
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.play.services.mlkit.text.recognition.common)
    implementation(libs.play.services.mlkit.text.recognition)
    implementation(libs.generativeai)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Room
    implementation(libs.room.ktx)
    implementation(libs.room.viewmodel)
    implementation(libs.room.lifecycle)
    implementation(libs.room.runtime)
    ksp(libs.room.compiler.ksp)


    // Hilt
    implementation(libs.hilt.android)
    implementation(libs.hilt.compose)
    ksp(libs.hilt.compiler.ksp)

    implementation(libs.datastore.core)
    implementation(libs.datastore.preferences)

    // Splashscreen
    implementation(libs.splashscreen)

    // Appintro
    implementation(libs.appintro)

    // Appcompat kvuli appintro
    implementation(libs.appcompat)

    implementation(libs.accompanist.permissions)
    implementation(libs.gson)

    //Maps
    implementation(libs.googlemap)
    implementation(libs.googlemap.compose)
    implementation(libs.googlemap.foundation)
    implementation(libs.googlemap.utils)
    implementation(libs.googlemap.widgets)
    implementation(libs.googlemap.compose.utils)

    implementation(libs.compose.charts)

}
