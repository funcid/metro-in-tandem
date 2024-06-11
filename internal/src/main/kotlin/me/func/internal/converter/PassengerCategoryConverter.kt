package me.func.internal.converter

import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter
import me.func.internal.model.PassengerCategory

@Converter(autoApply = true)
class PassengerCategoryConverter : AttributeConverter<PassengerCategory, String> {
    override fun convertToDatabaseColumn(attribute: PassengerCategory?): String? {
        return attribute?.categoryCode?.uppercase()
    }

    override fun convertToEntityAttribute(dbData: String?): PassengerCategory? {
        println("Converting dbData: $dbData")
        return dbData?.let {
            val category = PassengerCategory.fromCode(it.uppercase())
            if (category == null) {
                println("No matching category for code: $it")
            }
            category
        }
    }
}