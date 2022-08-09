import com.android.build.gradle.BaseExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.project
import java.util.concurrent.TimeUnit

fun Project.applyAndroidConfig(configExtensions: (BaseExtension.() -> Unit)? = null) {
    android.run {
        compileSdkVersion(AppConfig.compileSdkVersion)
        defaultConfig {
            minSdk = AppConfig.minSdkVersion
            targetSdk = AppConfig.targetSdkVersion
            versionCode = AppConfig.versionCode
            versionName = AppConfig.versionName
        }

        dataBinding {
            isEnabled = true
        }

        buildTypes {
            getByName("debug") {
                versionNameSuffix = ".dev"

            }
            getByName("release") {
                isMinifyEnabled = false
                proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
                setProperty("archivesBaseName", "${AppConfig.versionName}-${AppConfig.versionCode}")
            }
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_1_8
            targetCompatibility = JavaVersion.VERSION_1_8
        }
        sourceSets {
            getByName("main").java.srcDirs(SourceSets.kotlinSrc)
            getByName("test").java.srcDirs(SourceSets.testSrc)
        }
        configExtensions?.invoke(this)

        configurations.all {
            resolutionStrategy {
                force(Deps.kotlinCore)
                force(Deps.kotlinStdLib)
                cacheChangingModulesFor(0, TimeUnit.SECONDS)
            }
        }
    }
}

fun DependencyHandlerScope.applicationDependency() {
    requiredKoinDependency()
    requiredDependency()
    implementation(project(Modules.module))
   // implementation(project(Modules.firebase))
    implementation(project(Modules.lang))
    implementation(project(Modules.prefs))
    implementation(project(Modules.design))
    implementation(project(Modules.toast))
    implementation(project(Modules.public))
    implementation(project(Modules.filesApi))

    implementation(project(Modules.tableTraffic))

    implementation(project(Modules.trafficWork))
    implementation(project(Modules.cryptoWallet))

    implementation(project(Modules.uiLogin))
}
fun DependencyHandlerScope.requiredDependency() {
    api(Deps.kotlinCore)
    api(Deps.kotlinStdLib)
    api(Deps.coroutinesPlay)
    api(Deps.timber)
}
fun DependencyHandlerScope.requiredUiDependency() {
    api(Deps.appCompat)
    api(Deps.material)
    api(Deps.constraintLayout)
}
fun DependencyHandlerScope.requiredRoomDependency() {
    api(Deps.room)
    //implementation(Deps.roomCoroutines)
    //annotationProcessor(Deps.roomCompiler)
    kapt(Deps.roomCompiler)

}
fun DependencyHandlerScope.requiredKoinDependency() {
    api(Deps.koinCore)
    api(Deps.koinAndroid)
    api(Deps.koinAndroidCompat)
    // implementation(Deps.koinAndroidWorkManager)
    api(Deps.koinAndroidNavigation)
}
fun DependencyHandlerScope.requiredFirebaseDependency() {
    implementation(platform(Deps.firebasePlatform))
    implementation(Deps.firebaseAnalytics)
    implementation(Deps.firebaseCrashlytics)
    implementation(Deps.firebaseConfig)
    implementation(Deps.firebasePerf)
    implementation(Deps.firebaseAuth)
    implementation(Deps.firebaseFireStore)
}
fun DependencyHandlerScope.requiredModuleDependency() {
    implementation(project(Modules.module))
}

private val Project.android: BaseExtension
    get() = extensions.findByName("android") as? BaseExtension
        ?: error("Project '$name' is not an Android module")

private fun DependencyHandlerScope.api(dependencyNotation: Any) {
    "api"(dependencyNotation)
}

private fun DependencyHandlerScope.implementation(dependencyNotation: Any) {
    "implementation"(dependencyNotation)
}

private fun DependencyHandlerScope.kapt(dependencyNotation: Any) {
    "kapt"(dependencyNotation)
}

