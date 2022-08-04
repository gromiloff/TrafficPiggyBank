package table.traffic.room

import androidx.room.*

/**
 * Dao-интерфейс описывающий возможные действия с БД
 *
 * @author gromiloff
 */
@Dao
internal interface TrafficDao {
    /** получить все записи в талице */
    @Query("SELECT * FROM `${TrafficEntity.TABLE_NAME}`")
    suspend fun all() : List<TrafficEntity>

    /** Добавить запись с данными в БД */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrReplace(data: TrafficEntity)

    /** Обновить конкретную запись в базе */
    @Update
    suspend fun update(data: TrafficEntity)

    /** Удалить событие из БД */
    @Delete
    suspend fun delete(data: TrafficEntity)

    /** Удалить все события аналитики из БД */
    @Query("DELETE FROM `${TrafficEntity.TABLE_NAME}`")
    suspend fun deleteAll()
}