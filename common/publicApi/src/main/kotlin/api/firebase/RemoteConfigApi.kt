package api.firebase

import api.model.data.BoxRestrictions
import api.model.data.Rarity
import api.model.data.Restriction
import api.model.data.Template

/** методы по работе с данными полученные при изменении Firebase Remote Config */
interface RemoteConfigApi {
    /** активация Firebase Remote Config. Вызов должен быть на первом экране приложения */
    @Throws(InstantiationException::class)
    fun activateConfig()

    /** @return размер радиуса в метрах по умолчанию */
    fun getRadius() : Int

    /** @return конечную стоимость объекта в заивисмости от веса и ограничений */
    fun getPriceBy(weight : Int, restriction : BoxRestrictions? = null) : Int

    /** @return [Rarity] в зависимости от указанного веса объекта */
    fun getRarityByWeight(weight : Int) : Rarity?

    /** @return список доступных шаблонов объектов */
    fun getTemplates() : List<Template>

    /** @return список доступных шаблонов ограничений */
    fun getRestrictions() : List<Restriction<*>>

    /** @return [BoxRestrictions] с владельцем типа0 */
    fun createRestrictionWithRandomType0() : BoxRestrictions

    /** @return [BoxRestrictions] с владельцем типа1 и уже рандомным значением коэффициента изменений */
    fun createRestrictionWithRandomType1() : BoxRestrictions
}