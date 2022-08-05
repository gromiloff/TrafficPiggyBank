package login.ui

import android.content.IntentSender
import api.ll.PermissionWrap
import design.Action

internal sealed class LoginAction : Action {
    object NextScreen : LoginAction()
    object Cancel : LoginAction()
    data class RequestPermission(val set : HashSet<PermissionWrap>) : LoginAction()
    data class Error(val e : Exception) : LoginAction()
    data class Launch(val sender : IntentSender) : LoginAction()
}
