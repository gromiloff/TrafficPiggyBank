plugins {
    id("base-library")
    id("kotlin-kapt")
}

dependencies {
    requiredDependency()
    requiredKoinDependency()

    api(project(Modules.module))
    api(project(Modules.public))
    api(project(Modules.tableTraffic))
}
