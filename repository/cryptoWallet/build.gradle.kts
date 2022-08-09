plugins {
    id("base-library")
    id("kotlin-kapt")
}

dependencies {
    requiredDependency()
    requiredKoinDependency()

    api(project(Modules.module))
    api(project(Modules.public))
    api(project(Modules.filesApi))
    api(project(Modules.prefs))

    implementation(Deps.walletConnect)
    implementation (Deps.moshi)
    implementation (Deps.okhttp)
    implementation ("org.java-websocket:Java-WebSocket:1.4.0")
    implementation ("com.github.komputing:khex:1.1.0")
}
