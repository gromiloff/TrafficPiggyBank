package api.ll

/**
 * Вспомогательный интерфейс описывающий работу с пермишенами, необходимыми для функционирования модуля.
 *
 * @author gromiloff
 * */
interface RequestPermissionApi {
    /** проверка необхдимых пермишенов для работы модуля */
    fun requestPermissions() : HashSet<PermissionWrap>
}

/** базовый класс описывающий модель необходимых пермишенов */
sealed class PermissionWrap {
    /** особый вариант запроса пермишенов. Для получия их необходимо создать специальный [Intent] и
     *  попасть в найтройки системы чтобы предоставить системные разрешения
     *
     *  startActivity(Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS))
     *  */
    object Settings : PermissionWrap()
    /** обычный запрос на пермишены */
    data class Normal(val list : ArrayList<String>) : PermissionWrap()
    /** варианта ответа когда нет необходимости предоставления разрешений, либо они уже есть */
    object Existing : PermissionWrap()
}