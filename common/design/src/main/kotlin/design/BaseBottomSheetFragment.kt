package design

import android.os.Bundle
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.core.component.KoinComponent

/**
 * Базовый класс для [BottomSheetDialogFragment]
 * @author gromiloff
 */
abstract class BaseBottomSheetFragment<Command : Action> : BottomSheetDialogFragment(), KoinComponent {
    abstract val viewModel : BaseViewModel<Command>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActionObserver().observe(this) { action ->
            action?.let { handleAction(it) }
        }
    }

    protected open fun handleAction(action : Command) = Unit
}
