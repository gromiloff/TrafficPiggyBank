plugins {
    id("base-library")
}

dependencies {
    requiredDependency()
    api(project(Modules.design))
    api(project(Modules.module))
    api(project(Modules.filesApi))
    api(project(Modules.cryptoWallet))
}