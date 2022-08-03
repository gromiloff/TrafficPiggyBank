package api.model.data

import android.os.Parcelable
import androidx.annotation.Size
import kotlinx.parcelize.Parcelize

/**
 * Модель данных задания получаемая после парсинга данных Firebase Remote Config
 *
 * @param rarity актуальная раритетность паттерна
 * @param weightMin минимальный вес для вычислений
 * @param weightMax максимальный вес для вычислений
 * @param distanceMin минимальная дистанция для определения области перемещения
 * @param distanceMax максильманая дистанция для определения области перемещения
 * @param speed теоретическая скорость достижения области перемещения
 *
 * @author gromiloff
 * */
data class Template(
    val rarity : Rarity,
    val weightMin : Int,
    val weightMax : Int,
    val distanceMin : Int,
    val distanceMax : Int,
    val speed : Float
)

/** Набор ограничений при генерации задания. Определяется в коробке.
 * @param V дженерик ограничения, зависит от типа ограничения
 * @param value значение ограничений с заданным типом [V]
 *
 * @author gromiloff
 * */
sealed class Restriction<V>(open val value : V) : Parcelable {
    /** ограничения с типом 0 для выбора региона генерации */
    @Parcelize
    data class Type0(override val value : Int) : Restriction<Int>(value)
    /** ограничения с типом 1 для работы с радиусом области перемещения */
    @Parcelize
    data class Type1(@Size(4) override val value : List<Int>) : Restriction<List<Int>>(value)
}