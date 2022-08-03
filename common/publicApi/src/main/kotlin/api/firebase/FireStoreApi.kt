package api.firebase

import api.model.BoxBuyStatus
import api.model.BoxMintStatus
import api.model.data.Box
import api.model.data.Task

/** методы по работе с базой данных Firebase Firestore */
interface FireStoreApi {
    /** получить актуальное задание пользователя если оно имеется.
     * проверка на протухаемость не осуществляется ! */
    suspend fun currentTask() : Task?

    /** минтим коробку для продажи в магазине. проверяем сумму коммисионных для минта */
    suspend fun mintBox(box : Box) : BoxMintStatus

    /** произвести проверку на наличие текущего задания, достаточного количества денег на счету и
     * залочить коробку за пользователем */
    suspend fun lockBoxIf(box : Box) : BoxBuyStatus

    /** получить список всех коробок доступных пользователю из базы данных Firestore */
    suspend fun getAllBoxes(my : Boolean) : List<Box>

    /** найти коробку по заданному уникальному идентификатору.
     *  Коробку в этот момент могли купить и вернуться null */
    suspend fun findBox(id : String) : Box?

    /** @return количество денег у пользователя */
    suspend fun userBalance() : Int
}
