package api.model.data

import android.content.res.Resources
import android.os.Parcelable
import api.model.text.TextData
import kotlinx.parcelize.Parcelize
import pro.gromiloff.api.R

/**
 * Модель данных коробки. Используется при покупке как модель данных для генерации задания.
 * Паттерн задания можно получить из конфиругатора через значение веса. Изначально в коробке не может
 * быть информации о ГЕО. Исключением является только [BoxRestrictions] так как это значение накладывает
 * ограничение при открытии. Так же влияет на цену коробки.
 *
 * @param id уникальный идентификатор коробки. значение NULL истользуется только для коробки которая не минтилась
 * @param created время минтинга коробки
 * @param owner ссылка на владельца коробки. значение NULL истользуется только для коробки которая не минтилась
 * @param weight значение веса для дальенейшего использования
 * @param restrictions ограничения для задания. Например изменение рабиуса, область для генерации центра.
 *
 * @author gromiloff
 * */
@Parcelize
data class Box(
    val id : String?,
    val created : Long,
    val owner : String?,
    val weight : Int,
    val restrictions : BoxRestrictions?
) : Parcelable

/**
 * Обертка над дополнительным ограничением при открытии коробки для задания.
 * @param owner ссылка на шаблон ограничения
 * @param customValue значение которое будет применяться при открытии коробки (для случаев когда
 * параметр не используется - его значение равно [Int.MIN_VALUE])
 *
 * @author gromiloff
 * */
@Parcelize
data class BoxRestrictions(
    val owner : Restriction<*>,
    val customValue : Int = Int.MIN_VALUE
) : Parcelable

/** маппер енумерейшена ограничения в отображаемый текст */
fun BoxRestrictions?.toTextData(resources: Resources) = when(this?.owner) {
    is Restriction.Type0 -> TextData.ResIdText(R.string.box_restrictions_center_in)
    is Restriction.Type1 -> TextData.NormalText(resources.getString(R.string.box_restrictions_change_radius, this.customValue))
    null -> TextData.NoText
}
