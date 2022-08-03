package api.func

import api.model.data.Task

/**
 * Публичные функции для работы с задачами
 * @author gromiloff
 * */
interface TaskApi {
    fun generateList() : List<Task>
}