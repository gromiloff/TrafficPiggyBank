package files

import android.content.Context
import android.content.Intent
import android.os.Environment
import androidx.annotation.WorkerThread
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import module.FeatureDataBaseApiModule
import org.koin.java.KoinJavaComponent
import timber.log.Timber
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Вспомогательная функция сохранения маркированных баз данных в файл директории
 * [Environment.DIRECTORY_DOWNLOADS]
 * @author gromiloff
 *
suspend fun cloneDataBase() {

    // todo требуется пермишен MANAGE_EXTERNAL_STORAGE
    withContext(Dispatchers.IO) {
        val api = KoinJavaComponent.get<ShowToastApi>(ShowToastApi::class.java)
        try {
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "RMK".plus(SimpleDateFormat("dd-MM-yy HH:mm:sss", Locale.getDefault())
                    .format(System.currentTimeMillis()).toString())
                    .plus(".json")
            )
            file.createNewFile()
            file.setWritable(true)

            fillFile(file)

            // todo реализовать логику отправки его куда нить
            api.show(
                KoinJavaComponent.get(Context::class.java),
                TextHolder.Normal(text = "база данных выгружена по пути ${file.path}")
            )
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}
*/
/**
 * Вспомогательная функция сохранения маркированных баз данных в файл и после шарим файл
 *
 * @author gromiloff
 * */
suspend fun shareDB(activity : Context) {
    withContext(Dispatchers.IO) {
        try {
            val file = createFile(
                activity,
                "TPB_"
                    .plus(SimpleDateFormat("dd-MM-yy HH:mm:sss", Locale.getDefault())
                    .format(System.currentTimeMillis()).toString())
                    .plus(".txt")
            )
            val contentUri = FileProvider.getUriForFile(activity, "pro.gromiloff.files.fileprovider", file);

            val intentShareFile = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_STREAM, contentUri)
                putExtra(Intent.EXTRA_SUBJECT,"Sharing File...")
                putExtra(Intent.EXTRA_TEXT, "Sharing File...")
            }
            activity.startActivity(Intent.createChooser(intentShareFile, "Share File"))
        } catch (e: Exception) {
            Timber.e(e)
        }
    }
}

/** создать файл который будет виден внешним приложениям */
fun createFile(context: Context, name : String) : File {
    val dir = File(context.cacheDir, "cache")
    if(!dir.exists()) {
        dir.mkdirs()
    }
    return File(dir, name).apply {
        createNewFile()
        setWritable(true)
    }
}

@WorkerThread
private suspend fun fillFile(file : File) {
    // формирование данных из БД
    val sb = StringBuilder()
    KoinJavaComponent.getKoin().getAll<FeatureDataBaseApiModule>().distinct().forEach {
        val str = it.createJsonFromDatabase()
        if(!str.isNullOrBlank()) sb.append(str).append("\n")
    }
    file.writeText(sb.toString())
}