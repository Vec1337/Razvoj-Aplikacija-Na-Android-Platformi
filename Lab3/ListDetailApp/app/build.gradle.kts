plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-parcelize")
    //id("com.android.application")
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp") version "2.0.21-1.0.27"
}

android {
    namespace = "hr.tvz.android.listdetailapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "hr.tvz.android.listdetailapp"
        minSdk = 24
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
}

dependencies {

    implementation(platform("com.google.firebase:firebase-bom:33.14.0"))
    implementation("com.google.firebase:firebase-messaging")


    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


    // Room dependencies

    implementation("androidx.room:room-runtime:2.7.1")

    // If this project uses any Kotlin source, use Kotlin Symbol Processing (KSP)
    // See KSP Quickstart to add KSP to your build
    ksp("androidx.room:room-compiler:2.7.1")
}