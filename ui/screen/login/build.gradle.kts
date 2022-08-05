plugins {
    id("base-library")
}

dependencies {
    requiredDependency()
    api(project(Modules.design))
    api(project(Modules.module))
    api(project(Modules.filesApi))
    //implementation(Deps.googleServicesAuth)
}