plugins {
    id("base-library")
    id("kotlin-parcelize")
    id("kotlin-kapt")
}

dependencies {
    requiredUiDependency()
    requiredDependency()
    requiredKoinDependency()
    api(project(Modules.public))
    api(project(Modules.module))
}
