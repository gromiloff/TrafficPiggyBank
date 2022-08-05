package repository.sync.packages

import android.Manifest
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import timber.log.Timber


/** вспомогательный класс по работе с пакетами установленных приложений */
internal data class PackageHelper(
    val packageManager: PackageManager,
    val appPackageName : String
) {
    /** получаем все установленные пакета приложений на телефоне
     * @return список пар имя пакета и его uid */
    fun getAllPackages() : List<Pair<String, Int>> {
        val packageInfoList = packageManager.getInstalledApplications(0)
        val result = ArrayList<Pair<String, Int>>(packageInfoList.size)
        for (packageInfo in packageInfoList) {
            if (packageInfo.skipThisApp()) continue
            if (packageInfo.skipBySystemPackage()) continue
            if (packageInfo.skipByPermissionInternet()) continue
            if (!packageInfo.canIcon()) continue
            // все проверки пройдены - считаем пакет рабочим для дальнейшего обследования
            result.add(Pair(packageInfo.packageName, packageInfo.uid))
        }
        Timber.d("all packages size = ${packageInfoList.size}, app work with size ${result.size}")
        return result
    }

    /** проверка что пакет не от этого приложения
     *  @return true если надо пропустить пакет */
    private fun ApplicationInfo.skipThisApp() = packageName == appPackageName

    /** проверка на имя пакета начинаюзегося на 'com.google.android'
     *  @return true если надо пропустить пакет */
    private fun ApplicationInfo.skipBySystemPackage() =
        if (flags and ApplicationInfo.FLAG_SYSTEM != 0) {
            Timber.d("skipBySystemPackage $this")
            true
        } else false

    /** проверка пакета на использование разрешения интернета
     *  @return true если надо пропустить пакет */
    private fun ApplicationInfo.skipByPermissionInternet() =
        if(packageManager.checkPermission(Manifest.permission.INTERNET, packageName) == PackageManager.PERMISSION_DENIED) {
            Timber.d("skipByPermissionInternet $this")
            true
        } else false

    /** проверка пакета на информации об иконке приложения
     *  @return false если надо пропустить пакет */
    private fun ApplicationInfo.canIcon() : Boolean {
        return try {
            if(packageManager.getApplicationIcon(packageName) != packageManager.defaultActivityIcon) true
            else {
                Timber.d("canIcon $this")
                false
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.d(e)
            false
        }
    }

}