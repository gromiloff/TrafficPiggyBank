package design.model

import androidx.lifecycle.MutableLiveData

/**
 * Модель управления глобальной ромашкой
 *
 * @author gromiloff
 * */
interface WaitingModel {
    val waitingVisibility : MutableLiveData<VisibilityChanger>
}
