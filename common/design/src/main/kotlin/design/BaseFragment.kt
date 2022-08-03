package design

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import org.koin.core.component.KoinComponent

/**
 * Базовый класс для [Fragment]
 * @author gromiloff
 */
abstract class BaseFragment<Command : Action> : Fragment(), KoinComponent {
    abstract val viewModel : BaseViewModel<Command>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getActionObserver().observe(viewLifecycleOwner) { action ->
            action?.let { handleAction(it) }
        }
    }

    protected open fun handleAction(action : Command) = Unit
}
