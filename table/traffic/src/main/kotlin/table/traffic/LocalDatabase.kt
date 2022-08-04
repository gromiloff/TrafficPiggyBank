package table.traffic

import androidx.room.Database
import androidx.room.RoomDatabase
import table.traffic.room.TrafficDao
import table.traffic.room.TrafficEntity

/**
 * Объект базы данных модуля трафика
 *
 * @author gromiloff
 */
@Database(
    entities = [
        TrafficEntity::class,
    ],
    version = 1,
    exportSchema = false
)
internal abstract class LocalDatabase : RoomDatabase() {
    abstract fun trafficDao(): TrafficDao
    companion object {
        /* Название БД */
        const val DATABASE_NAME = "traffic-data-db"
    }
}
