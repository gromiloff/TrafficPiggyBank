plugins {
    id("base-library")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

dependencies {
    requiredUiDependency()
    requiredDependency()
    requiredKoinDependency()
}
