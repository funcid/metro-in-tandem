package me.func.internal.converter

import me.func.internal.dto.RequestStatus
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class RequestStatusConverter : AttributeConverter<RequestStatus, String> {
    override fun convertToDatabaseColumn(attribute: RequestStatus?): String? {
        return attribute?.statusCode
    }

    override fun convertToEntityAttribute(dbData: String?): RequestStatus? {
        return dbData?.let { RequestStatus.fromCode(it) }
    }
}