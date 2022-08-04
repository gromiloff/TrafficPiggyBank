@file:Suppress("unused")

private object Versions {
    const val gradleBuildTools = "7.0.3"
    const val kotlin = "1.6.10"
    const val koin = "3.2.0"
    const val compose = "1.0.5"

    const val androidx_activity = "1.1.0"
    const val glide_version = "4.8.0"
    const val google_maps = "17.0.0"
    const val google_location = "17.0.0"
    const val lifecycle = "2.3.1"
    const val room = "2.4.2"
}

object SourceSets {
    const val kotlinSrc = "src/main/kotlin"
    //const val javaSrc = "src/main/java"
    const val testSrc = "src/test/kotlin"
}

object AppConfig {
    private const val versionMajor = 0
    private const val versionMinor = 1
    private const val versionBuild = 0

    const val minSdkVersion = 26
    const val compileSdkVersion = 31
    const val targetSdkVersion = 31
    const val versionCode = versionMajor * 100000 + versionMinor * 1000 + versionBuild
    const val versionName = "${versionMajor}.${versionMinor}.${versionBuild}"
}

object Modules {
    const val module = ":common:module"
    const val firebase = ":common:firebase"
    const val lang = ":common:lang"
    const val design = ":common:design"
    const val toast = ":common:toast"
    const val roomHelp = ":common:roomHelp"
    const val prefs = ":common:LocalPrefs"
    const val public = ":common:publicApi"

    const val table_traffic = ":table:traffic"

    const val sync = ":repository:sync"
}

object Deps {
    // базовые настройки грейдла
    const val gradleBuildToolsApi = "com.android.tools.build:gradle-api:${Versions.gradleBuildTools}"
    const val gradleBuildTools = "com.android.tools.build:gradle:${Versions.gradleBuildTools}"
    // гугл-сервисы
    const val googleServices = "com.google.gms:google-services:4.3.8"
    const val googleServicesVision = "com.google.android.gms:play-services-vision:20.1.3"
    const val googleServicesAuth = "com.google.android.gms:play-services-auth:20.2.0"
    // сервисы firebase
    const val gradleFirebasePerformance = "com.google.firebase:perf-plugin:1.4.1"
    const val gradleFirebaseCrash = "com.google.firebase:firebase-crashlytics-gradle:2.9.0"
    const val firebasePlatform = "com.google.firebase:firebase-bom:29.3.1"
    const val firebaseAnalytics = "com.google.firebase:firebase-analytics-ktx"
    const val firebaseCrashlytics = "com.google.firebase:firebase-crashlytics-ktx"
    const val firebaseConfig = "com.google.firebase:firebase-config-ktx"
    const val firebaseAuth = "com.google.firebase:firebase-auth-ktx"
    const val firebasePerf = "com.google.firebase:firebase-perf-ktx"
    const val firebaseFireStore = "com.google.firebase:firebase-firestore-ktx"

    // сервисы котлина
    const val kotlinCore = "androidx.core:core-ktx:1.6.0"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerializationClasspath = "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val kotlinStdLib = "org.jetbrains.kotlin:kotlin-stdlib:${Versions.kotlin}"
    const val kotlinSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"

    // сервисы UI
    const val material = "com.google.android.material:material:1.5.0-rc01"
    const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
    const val swiperefreshlayout = "androidx.swiperefreshlayout:swiperefreshlayout:1.1.0"
    // сервисы AndroidX
    const val appCompat = "androidx.appcompat:appcompat:1.3.1"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:2.1.1"
    const val navigationFragmentKtx = "androidx.navigation:navigation-fragment-ktx:2.3.5"
    const val navigationUiKtx = "androidx.navigation:navigation-ui-ktx:2.3.5"
    const val lifecycleViewmodelKtx = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleCommonJava8 = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"

    // DI
    const val koinCore = "io.insert-koin:koin-core:${Versions.koin}"
    const val koinAndroid = "io.insert-koin:koin-android:${Versions.koin}"
    const val koinAndroidCompat = "io.insert-koin:koin-android-compat:${Versions.koin}"
    const val koinAndroidWorkManager = "io.insert-koin:koin-androidx-workmanager:${Versions.koin}"
    const val koinAndroidNavigation =  "io.insert-koin:koin-androidx-navigation:${Versions.koin}"
    const val koinAndroidCompose =  "io.insert-koin:koin-androidx-compose:${Versions.koin}"
    const val koinTest = "io.insert-koin:koin-test:${Versions.koin}"

    const val robolectric = "org.robolectric:robolectric:4.6.1"
    const val mockk = "io.mockk:mockk:1.12.0"

    const val composeCompiler = "androidx.compose.compiler:compiler:${Versions.compose}"
    const val composeUi = "androidx.compose.ui:ui:${Versions.compose}"
    const val composeUiTooling = "androidx.compose.ui:ui-tooling:${Versions.compose}"
    const val composeUiFoundation = "androidx.compose.foundation:foundation:${Versions.compose}"
    const val composeMaterial = "androidx.compose.material:material:${Versions.compose}"
    const val composeConstraintLayout = "androidx.constraintlayout:constraintlayout-compose:1.0.0-rc01"

    const val coilCompose = "io.coil-kt:coil-compose:1.4.0"

    // сервисы RX
    //const val rxAndroid = "io.reactivex.rxjava2:rxandroid:2.1.1"
    // сервисы корутин
    const val coroutinesCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
    const val coroutinesAndroid = "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2"
    const val coroutinesPlay = "org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.1.1"

    // Work Manager
    const val workRuntime = "androidx.work:work-runtime-ktx:2.5.0"

    // Timber
    const val timber =  "com.jakewharton.timber:timber:5.0.1"

    // сервисы Retrofit 2
    const val gson = "com.google.code.gson:gson:2.8.6"
    val retrofit = arrayOf(
        "com.squareup.retrofit2:retrofit:2.9.0",
        "com.squareup.retrofit2:converter-gson:2.6.2",
        "com.squareup.retrofit2:adapter-rxjava2:2.4.0",
        "com.squareup.okhttp3:logging-interceptor:4.9.0",
        gson
    )
    // сервисы Sentry https://github.com/getsentry/sentry-android-gradle-plugin/releases
    const val gradleSentry = "io.sentry:sentry-android-gradle-plugin:1.7.36"
    const val sentryAndroid = "io.sentry:sentry-android:4.3.0"
    const val sentryExt = "org.slf4j:slf4j-nop:1.7.25"

    // Room
    const val room = "androidx.room:room-ktx:${Versions.room}"
    // const val roomCoroutines = "androidx.room:room-coroutines:2.1.0-alpha04"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    // open street map android lib
    const val osmdroid = "org.osmdroid:osmdroid-android:6.1.13"
}