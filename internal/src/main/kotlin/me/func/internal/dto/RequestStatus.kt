enum class RequestStatus(val statusCode: String) {
    REQUEST_COMPLETED("Заявка закончена"),
    CANCELLED_BY_PASSENGER("Отмена заявки по просьбе пассажира");

    companion object {
        private val map = entries.associateBy(RequestStatus::statusCode)
        fun fromCode(code: String) = map[code]
    }
}