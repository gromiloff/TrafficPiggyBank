plugins {
    id("base-library")
}

dependencies {
    requiredDependency()
    implementation(project(Modules.module))
    implementation(project(Modules.public))
}