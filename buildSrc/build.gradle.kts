plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(Deps.kotlinGradlePlugin)
    }
}

kotlin {
    sourceSets.getByName("main").kotlin.srcDir("buildSrc/src/main/kotlin")
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation(Deps.gradleBuildToolsApi)
    implementation(Deps.gradleBuildTools)
    implementation(Deps.kotlinGradlePlugin)
    implementation(Deps.kotlinStdLib)
}