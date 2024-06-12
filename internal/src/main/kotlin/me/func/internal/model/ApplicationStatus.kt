package me.func.internal.model

enum class ApplicationStatus(val statusCode: String) {
    REQUEST_COMPLETED("Заявка закончена"),
    NOT_APPROVED("Не подтверждена"),
    CANCELLED_BY_PASSENGER("Отмена заявки по просьбе пассажира"),
    NOT_VISITED_BY_PASSENGER("Отмена заявки по неявке пассажира"),
    ACCEPTED("Принята"),
    INSPECTOR_ON_THE_WAY("Инспектор выехал"),
    INSPECTOR_ARRIVED("Инспектор на месте"),
    TRIP("Поездка"),
    PASSENGER_DELAYED("Пассажир опаздывает"),
    INSPECTOR_DELAYED("Инспектор опаздывает");

    companion object {
        private val map = entries.associateBy(ApplicationStatus::statusCode)
        fun fromCode(code: String) = map[code]
    }
}