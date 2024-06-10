package me.func.internal.dto

enum class PassengerCategory(val categoryCode: String) {
    IZ("ИЗ"),     // Инвалид по зрению (тотальный)
    IZT("ИЗТ"),     // Инвалид по зрению (слабовидящий)
    IS("ИС"),     // Инвалид по слуху
    IK("ИК"),     // Инвалид колясочник
    IO("ИО"),     // Инвалид опорник
    DI("ДИ"),     // Ребенок инвалид
    PL("ПЛ"),     // Пожилой человек
    RD("РД"),     // Родители с детьми
    RDK("РДК"),     // Родители с детскими колясками
    OGD("ОГД"),     // Организованные группы детей
    OV("ОВ"),     // Временно маломобильные
    IU("ИУ"),;      // Люди с ментальной инвалидностью


    companion object {
        private val map = entries.associateBy(PassengerCategory::categoryCode)
        fun fromCode(code: String) = map[code]
    }
}