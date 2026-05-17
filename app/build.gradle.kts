plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.oop"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.example.oop"
        minSdk = 24
        targetSdk = 37
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
}

dependencies {
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    implementation(libs.gson)
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    annotationProcessor(libs.room.compiler)
    
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
