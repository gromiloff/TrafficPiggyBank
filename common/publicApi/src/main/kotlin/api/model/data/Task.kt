package api.model.data

/**
 * Модель данных задания с которым может работать пользователь
 *
 * @param weight вес задания (от него генерятся другие вспомогательные данные)
 * @param cost стоимость коробки с заданием
 * @param timeEnd время протухания задания (после достижения этого времени задание не актуально)
 * @param centerPoint центр области перемещения (массив из двух [Double] коогдинат)
 * @param radius радиус области перемещения в метрах от [centerPoint]
 *
 * @author gromiloff
 * */
data class Task(
    val weight : Int,
    val cost : Int,
    val timeEnd : Long,
    val centerPoint : List<Double>,
    val radius : Int
)