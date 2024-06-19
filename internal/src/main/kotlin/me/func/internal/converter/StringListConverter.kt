package me.func.internal.converter
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Converter

@Converter(autoApply = true)
class StringListConverter : AttributeConverter<List<String>, String> {

    override fun convertToDatabaseColumn(stringList: List<String>): String {
        return stringList.joinToString(SPLIT_CHAR)
    }

    override fun convertToEntityAttribute(string: String?): List<String> {
        return string?.split(SPLIT_CHAR) ?: listOf()
    }

    companion object {
        private const val SPLIT_CHAR = ";"
    }
}