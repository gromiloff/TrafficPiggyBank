package table.traffic

import android.content.Context
import androidx.room.Room
import api.table.TableTrafficApi
import module.FeatureDataBaseApiModule
import module.FeatureModule
import org.koin.dsl.bind
import org.koin.dsl.module

/**
 * Описание зависимостей предоставляемых модулем трафика
 * */
object TableTrafficModule : FeatureModule {
    override fun create(context: Context) = module {
        val model = TableTrafficImpl(
            Room.databaseBuilder(context, LocalDatabase::class.java, LocalDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
                .trafficDao()
        )
        PrefHelper.create(context)
        single<TableTrafficApi> {
            model
        }
        single {
            model
        } bind FeatureDataBaseApiModule::class
    }
}