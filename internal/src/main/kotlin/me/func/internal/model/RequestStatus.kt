package me.func.internal.model

enum class RequestStatus(val statusCode: String) {
    REQUEST_COMPLETED("Заявка закончена"),
    NOT_APPROVED("Не подтверждена"),
    CANCELLED_BY_PASSENGER("Отмена заявки по просьбе пассажира"),
    NOT_VISITED_BY_PASSENGER("Отмена заявки по неявке пассажира");

    companion object {
        private val map = entries.associateBy(RequestStatus::statusCode)
        fun fromCode(code: String) = map[code]
    }
}