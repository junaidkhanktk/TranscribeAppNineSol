import java.util.UUID

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("kotlin-kapt")
    id("kotlin-parcelize")

}

android {
    namespace = "com.example.transcribeapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.transcriber.App"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        ndkVersion = "25.2.9519653"
        /*ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "armeabi", "arm64-v8a", "x86_64", "x86"))
        }*/
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    /*    sourceSets {
            getByName("main") {
                assets.srcDirs("src/main/assets", "$buildDir/generated/assets")
            }
        }*/
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
        buildConfig = true
    }

    externalNativeBuild {
        cmake {
            path ("src/main/cpp/CMakeLists.txt")
        }
    }

}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.identity.credentials)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    implementation(libs.dexter)
    implementation(libs.sdp.android)
    implementation(libs.ssp.android)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.scalars)
    implementation(libs.gson)
    implementation(libs.logging.interceptor)
    // FOR DEPENDENCY INJECTION
    implementation(libs.koin.android)


    implementation(libs.androidx.room.runtime)
    kapt(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    implementation(libs.timber)
    implementation(libs.vosk.android)
   // implementation(libs.jna)
    implementation("net.java.dev.jna:jna:5.13.0@aar")

    // Full exoplayer library
    implementation(libs.exoplayer)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.viewpager2)

    implementation (libs.singledateandtimepicker)


    //googleSignUp
    implementation (libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation (libs.googleid)

}


