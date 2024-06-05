package me.func.internal.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import me.func.internal.dto.PassengerCategory

@Converter(autoApply = true)
class PassengerCategoryConverter : AttributeConverter<PassengerCategory, String> {
    override fun convertToDatabaseColumn(attribute: PassengerCategory?): String? {
        return attribute?.categoryCode
    }

    override fun convertToEntityAttribute(dbData: String?): PassengerCategory? {
        return dbData?.let { PassengerCategory.fromCode(it) }
    }
}