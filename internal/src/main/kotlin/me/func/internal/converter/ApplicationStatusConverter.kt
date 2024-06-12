package me.func.internal.converter

import me.func.internal.model.ApplicationStatus
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class ApplicationStatusConverter : AttributeConverter<ApplicationStatus, String> {
    override fun convertToDatabaseColumn(attribute: ApplicationStatus?): String {
        return attribute?.statusCode ?: ApplicationStatus.NOT_APPROVED.statusCode
    }

    override fun convertToEntityAttribute(dbData: String?): ApplicationStatus {
        return dbData?.let { ApplicationStatus.fromCode(it) } ?: ApplicationStatus.NOT_APPROVED
    }
}