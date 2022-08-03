package api.func

import api.model.data.Box
import api.model.data.Rarity

/**
 * Публичные функции для работы с боксами (для магазина или мастерской)
 * @author gromiloff
 * */
interface BoxApi {
    /** сгенерировать новую коробку (без ее синхронизации в облако)
     *
     * @param rarity раритетность коробки
     * @param type индекс для ограничения, где 0 - бех ограничений, 1 - сектор, 2 - радиус
     * @return новое значение заполненного объекта [Box] */
    suspend fun newBox(rarity : Rarity, type : Int) : Box

    /** получить список всех коробок доступных пользователю. Сортируем по весу коробки */
    suspend fun getAvailableBoxesForMe() : List<Box>

    /** получить список всех моих коробок выставленных на продажу. Сортируем обратно по времени создания */
    suspend fun getMyBoxesInStore() : List<Box>
}