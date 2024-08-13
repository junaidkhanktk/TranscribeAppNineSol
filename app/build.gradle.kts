import java.util.UUID

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id ("kotlin-parcelize")

}

android {
    namespace = "com.example.transcribeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.transcribeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndkVersion = "25.2.9519653"
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "armeabi", "arm64-v8a", "x86_64", "x86"))
        }
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        sourceSets {
            getByName("main") {
                assets.srcDirs("src/main/assets", "$buildDir/generated/assets")
            }
        }
    }

    tasks.register("genUUID") {
        val uuid = UUID.randomUUID().toString()
        val odir = file("$buildDir/generated/assets/model-en-us")
        val ofile = file("$odir/uuid")

        doLast {
            mkdir(odir)
            ofile.writeText(uuid)
        }
    }

    tasks.named("preBuild") {
        dependsOn("genUUID")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig=true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation ("com.karumi:dexter:6.2.3")
    implementation("com.intuit.sdp:sdp-android:1.0.6")
    implementation("com.intuit.ssp:ssp-android:1.1.0")

    val retrofit_version = "2.9.0"
    implementation ("com.squareup.retrofit2:retrofit:$retrofit_version")
    implementation( "com.squareup.retrofit2:converter-gson:$retrofit_version")
    implementation( "com.squareup.retrofit2:converter-scalars:$retrofit_version")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation( "com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.1")
    // FOR DEPENDENCY INJECTION
    implementation ("io.insert-koin:koin-android:3.5.3")

    val room_version = "2.5.1"
    implementation ("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-ktx:$room_version")

    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation ("com.alphacephei:vosk-android:0.3.32+")
    implementation ("net.java.dev.jna:jna:5.13.0@aar")


    // Full exoplayer library
    implementation("com.google.android.exoplayer:exoplayer:2.17.1")

    val nav_version = "2.7.0" // Use the latest version
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")

}


