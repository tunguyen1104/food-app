plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.foodapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.foodapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

    /*
    flavorDimensions += "user"

    productFlavors {
        create("user1") {
            dimension = "user"
            applicationId = "com.example.foodapp.user1"
            versionNameSuffix = "-user1"
            resValue("string", "app_name", "FoodApp User1")
        }

        create("user2") {
            dimension = "user"
            applicationId = "com.example.foodapp.user2"
            versionNameSuffix = "-user2"
            resValue("string", "app_name", "FoodApp User2")
        }
    }*/
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // Retrofit
    implementation(libs.retrofit)
    // Gson Converter (for JSON serialization)
    implementation(libs.converter.gson)
    // OkHttp Logging Interceptor (for debugging API requests)
    implementation(libs.logging.interceptor)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    //Glide
    implementation(libs.glide)
    annotationProcessor(libs.compiler)
}

dependencies {
    // STOMP
    implementation("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")
    // JavaWebSocket
    implementation("org.java-websocket:Java-WebSocket:1.6.0")
    implementation("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
}
