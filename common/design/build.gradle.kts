plugins {
    id("base-library")
    id("kotlin-kapt")
}

dependencies {
    requiredUiDependency()
    requiredDependency()
    requiredKoinDependency()
    api(project(Modules.lang))
    api(project(Modules.public))
    implementation(Deps.swiperefreshlayout)
}