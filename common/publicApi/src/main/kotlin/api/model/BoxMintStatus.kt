package api.model

/**
 * Возможные состояния при минтинге коробки
 * @author gromiloff
 * */
enum class BoxMintStatus {
    /** коробку успешно заминчена */
    Success,
    /** для проведения операции недостаточно денег на счету */
    NoMoney,
    /** операция провалена и не закончена */
    DefaultFail
}