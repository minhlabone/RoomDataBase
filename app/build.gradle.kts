plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.roomdatabaseviewmodellivedata"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.roomdatabaseviewmodellivedata"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    val room_version = "2.6.1"
    val lifecycle_version ="2.7.0"
    implementation ("androidx.room:room-runtime:$room_version")
    annotationProcessor ("androidx.room:room-compiler:$room_version")
    // ViewModel
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycle_version")
    // LiveData
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.github.dhaval2404:imagepicker:2.1")
    implementation("io.github.ParkSangGwon:tedpermission-normal:3.3.0")



}