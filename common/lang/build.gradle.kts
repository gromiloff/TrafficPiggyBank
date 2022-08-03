plugins {
    id("base-library")
}

dependencies {
    requiredKoinDependency()
    requiredUiDependency()
    implementation(project(Modules.prefs))
    implementation(project(Modules.module))
}
