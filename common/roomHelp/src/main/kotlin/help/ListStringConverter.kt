package help

import androidx.room.TypeConverter


/** внутренний конвертер для списка операций */
class ListStringConverter {
    private val separator = "##"
    @TypeConverter
    fun fromString(value: String?): ArrayList<String>? {
        val split = value?.split(separator)
        return if(split.isNullOrEmpty()) null else {
            ArrayList<String>().apply { split.forEach { add(it) } }
        }
    }
    @TypeConverter
    fun fromList(list: ArrayList<String>?): String? {
        val sb = StringBuilder()
        list?.forEachIndexed { idx, value ->
            sb.append(value)
            if(list.size - 1 > idx){
                sb.append(separator)
            }
        }
        return if(sb.isBlank()) null else sb.toString()
    }
}