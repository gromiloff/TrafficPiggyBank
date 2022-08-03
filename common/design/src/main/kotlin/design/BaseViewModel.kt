package design

import androidx.annotation.UiThread
import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import design.model.VisibilityChanger
import design.model.WaitingModel
import kotlinx.coroutines.*
import org.koin.core.component.KoinComponent
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

/**
 * Базовая вью модель содержащая функционал:
 *  - держатель подписок RX
 *  - шина событий ACTION
 *  - слушателя сканнера ШК от ТСД
 * */
abstract class BaseViewModel<ACTION : Action> : ViewModel(),
    WaitingModel,
    KoinComponent,
    CoroutineScope
{

    override val waitingVisibility = MutableLiveData<VisibilityChanger>(VisibilityChanger.Gone)

    // если что-то пойдет не так
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Timber.d(throwable)
        waitingVisibility.postValue(VisibilityChanger.Gone)
        coroutineExceptionHandler(throwable)
    }

    final override val coroutineContext: CoroutineContext = Dispatchers.IO + exceptionHandler

    // шина событий для уведомления владельца модели
    private val action : MutableLiveData<ACTION?> = MutableLiveData<ACTION?>()

    override fun onCleared() {
        coroutineContext.cancelChildren()
        super.onCleared()
    }

    /** получить ссылку на наблюдаемую шину данные. Автоматически зануляется при запросе */
    @UiThread
    fun getActionObserver() : MutableLiveData<ACTION?> {
        // мы не уверены что там ничего не было ранее
        action.value = null
        return action
    }

    /** асинхронно отправить команду в шину */
    @WorkerThread
    protected fun postCommand(value : ACTION) {
        Timber.d("postCommand $value")
        action.postValue(value)
    }

    /** синхронно отправить команду в шину */
    @UiThread
    protected fun sendCommand(value : ACTION) {
        Timber.d("sendCommand $value")
        action.value = value
    }

    /** синхронно очистить шину */
    @UiThread
    protected fun resetCommand() {
        action.value = null
    }

    protected open fun coroutineExceptionHandler(throwable : Throwable) = false

    /** обертываем вызов функции в показ и убирание ромашки с экрана */
    protected fun wrapperWaiting(func : suspend () -> Unit) {
        launch (Dispatchers.IO) {
            waitingVisibility.postValue(VisibilityChanger.Visible)
            func()
            waitingVisibility.postValue(VisibilityChanger.Gone)
        }
    }
}

/**
 * Базовый маркер шины событий между моделью и ее владельцем
 * */
interface Action
