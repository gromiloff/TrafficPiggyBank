package module

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.module.Module

/**
 * Глобмальный интерфейс описывающий верхнеуровневый слой модулей
 * */
interface FeatureModule : KoinComponent {
    fun create(context : Context = get()) : Module
}