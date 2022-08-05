package impl

import java.text.CharacterIterator
import java.text.SimpleDateFormat
import java.text.StringCharacterIterator


/**
 * Специальное расширение размерности для байтовго значения.
 * Используется для читабельного представления информации.
 *
 * @author gromiloff
 * */
fun printBytes(value : Long) : String {
    var bytes : Long = value
    if (-1000 < bytes  && bytes < 1000) {
        return "$bytes  B"
    }
    val ci: CharacterIterator = StringCharacterIterator("kMGTPE")
    while (bytes <= -999950 || bytes >= 999950) {
        bytes /= 1000
        ci.next()
    }
    return java.lang.String.format("%.1f %cB", bytes / 1000.0, ci.current())
}


/**
 * Специальное расширение размерности для значения времени в мс.
 * Используется для читабельного представления информации.
 *
 * @author gromiloff
 * */
fun printTimeStump(value : Long) : String {
    return SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(value)
}
