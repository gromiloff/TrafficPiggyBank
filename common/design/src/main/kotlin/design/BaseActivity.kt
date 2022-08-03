package design

import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import api.model.text.TextHolder
import lang.LangActivity
import org.koin.core.component.KoinComponent
import pro.gromiloff.design.R
import timber.log.Timber

private const val PERMISSION = 15042022
/**
 * Базовый класс для активити
 * @author gromiloff
 */
abstract class BaseActivity<Command : Action> : LangActivity(), KoinComponent {
    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    open val permissions = emptyArray<String>()

    abstract fun createView() : View

    abstract val viewModel : BaseViewModel<Command>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(createView())

        setSupportActionBar(findViewById(R.id.app_toolbar))

        viewModel.getActionObserver().observe(this) { action ->
            action?.let { handleAction(it) }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            PERMISSION -> checkPermissions()
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onOptionsItemSelected(item: android.view.MenuItem): Boolean = when(item.itemId){
        android.R.id.home -> {
            onBackPressed()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    fun toolbarUpdate(
        title : TextHolder.Normal? = null,
        subTitle : TextHolder.Normal? = null,
        enableBack : Boolean? = null
    ) {
        runOnUiThread {
            supportActionBar?.let {
                title?.apply { it.title = text ?: getString(textId!!) }
                subTitle?.apply { it.subtitle = text ?: getString(textId!!) }
                enableBack?.apply {
                    it.setDisplayHomeAsUpEnabled(enableBack)
                    it.setDisplayShowHomeEnabled(enableBack)
                }
            }
        }
    }

    protected open fun permissionChecked() = Unit

    protected open fun handleAction(action : Command) = Unit

    protected fun checkPermissions() {
        // проверим по запрашиваемым пермишенам, пропуская те у которым доступ уже есть
        val request = permissions
            .filter { ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }
            .toTypedArray()

        if(request.isEmpty()) {
            permissionChecked()
        }
        else {
            try {
                ActivityCompat.requestPermissions(this, request, PERMISSION)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
    }
}
