<script context="module" lang="ts">
    export const sections = [
        "ЦУ-1",
        "ЦУ-2",
        "ЦУ-3",
        "ЦУ-3 (Н)",
        "ЦУ-4",
        "ЦУ-4 (Н)",
        "ЦУ-5",
        "ЦУ-8",
    ];
    export const shifts = ["1", "2", "1(Н)", "2(Н)", "5"];
    export const positions = ["ЦСИ", "ЦИ"];
    export const workTimes = [
        "07:00-19:00",
        "08:00-20:00",
        "20:00-08:00",
        "08:00-17:00",
    ];
    export const categories = [
        { value: "IZ", label: "ИЗ (Инвалид по зрению, тотальный)" },
        { value: "IZT", label: "ИЗТ (Инвалид по зрению, слабовидящий)" },
        { value: "IS", label: "ИС (Инвалид по слуху)" },
        { value: "IK", label: "ИК (Инвалид колясочник)" },
        { value: "IO", label: "ИО (Инвалид опорник)" },
        { value: "DI", label: "ДИ (Ребенок инвалид)" },
        { value: "PL", label: "ПЛ (Пожилой человек)" },
        { value: "RD", label: "РД (Родители с детьми)" },
        { value: "RDK", label: "РДК (Родители с детскими колясками)" },
        { value: "OGD", label: "ОГД (Организованные группы детей)" },
        { value: "OV", label: "ОВ (Временно маломобильные)" },
        { value: "IU", label: "ИУ (Люди с ментальной инвалидностью)" },
    ];
    export const statusOptions = [
        { value: "REQUEST_COMPLETED", label: "Заявка закончена" },
        { value: "NOT_APPROVED", label: "Не подтверждена" },
        {
            value: "CANCELLED_BY_PASSENGER",
            label: "Отмена заявки по просьбе пассажира",
        },
        {
            value: "NOT_VISITED_BY_PASSENGER",
            label: "Отмена заявки по неявке пассажира",
        },
        { value: "ACCEPTED", label: "Принята" },
        { value: "INSPECTOR_ON_THE_WAY", label: "Инспектор выехал" },
        { value: "INSPECTOR_ARRIVED", label: "Инспектор на месте" },
        { value: "TRIP", label: "Поездка" },
        { value: "PASSENGER_DELAYED", label: "Пассажир опаздывает" },
        { value: "INSPECTOR_DELAYED", label: "Инспектор опаздывает" },
    ];
</script>
