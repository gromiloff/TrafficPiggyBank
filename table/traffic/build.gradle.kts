plugins {
    id("base-library")
    id("kotlin-kapt")
}

dependencies {
    requiredDependency()
    requiredKoinDependency()
    requiredRoomDependency()

    api(project(Modules.module))
    api(project(Modules.public))
    api(project(Modules.prefs))
    api(Deps.gson)
}
