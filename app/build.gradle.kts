plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.phantom.banguminote"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.phantom.banguminote"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
        release {
            isMinifyEnabled = false
            isDebuggable = true
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
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
    buildFeatures {
        buildConfig = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    val navVersion = "2.7.6"
    implementation ("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation ("androidx.navigation:navigation-ui-ktx:$navVersion")
    implementation ("androidx.navigation:navigation-dynamic-features-fragment:$navVersion")
    implementation ("androidx.navigation:navigation-compose:$navVersion")

    val retrofit2Version = "2.9.0"
    implementation ("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation ("com.squareup.retrofit2:adapter-rxjava2:$retrofit2Version")
    implementation ("com.squareup.retrofit2:converter-gson:$retrofit2Version")
    implementation ("com.squareup.retrofit2:converter-scalars:$retrofit2Version")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.9.3")

    implementation ("io.github.cymchad:BaseRecyclerViewAdapterHelper4:4.1.4")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.8.22")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}