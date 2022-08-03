package design.list.model

import androidx.annotation.LayoutRes

/**
 * Модель данных ячейки списка. Используется как базовый класс для наследников моделей данных для
 * ячеек списка.
 *
 * @param layoutId ссылка на верстку
 *
 * @author gromiloff
 * */
abstract class ItemAdapterModel(@LayoutRes val layoutId : Int)