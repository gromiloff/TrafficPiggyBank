plugins {
    id("base-library")
}

dependencies {
    requiredDependency()
    requiredKoinDependency()
    requiredFirebaseDependency()
    requiredModuleDependency()
    implementation(project(Modules.prefs))
    implementation(project(Modules.public))
    implementation(Deps.googleServicesAuth)
    implementation(Deps.gson)
}
