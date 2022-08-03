plugins {
    id("base-library")
    id("kotlin-kapt")
}

dependencies {
    implementation(Deps.room)
    kapt(Deps.roomCompiler)
}
