plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.kotlin.android)
}

android {
    namespace = "com.interns.internscopeapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.interns.internscopeapp"
        minSdk = 26
        targetSdk = 35
        versionCode = 9
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures {
        viewBinding = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
    // Core Android & Jetpack
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    implementation(libs.flexbox)
    implementation(libs.glide)
    implementation(libs.picasso)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.v2300)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.database)
    implementation(libs.play.services.auth.v2111)

    // Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Database
    implementation(libs.mysql.connector.java)
    implementation(libs.core.ktx)
    implementation(libs.swiperefreshlayout)

    // Annotation processor
    annotationProcessor(libs.compiler)

    // PhonePe Intent SDK
    implementation("phonepe.intentsdk.android.release:IntentSDK:5.2.0")




    // Unit Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    //slider images
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
    implementation ("me.relex:circleindicator:2.1.6")

}
