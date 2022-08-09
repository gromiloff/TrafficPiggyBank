plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    //id("io.sentry.android.gradle")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

applyAndroidConfig {
    defaultConfig {
        applicationId = "pro.gromiloff.trafficpiggybank"
    }
    buildTypes {
        getByName("debug") {
            applicationIdSuffix = ".dev"
        }
    }
}

dependencies {
    applicationDependency()
}
