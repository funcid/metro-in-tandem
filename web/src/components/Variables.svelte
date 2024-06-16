<script context="module" lang="ts">
    import { JWT } from "./login/Login.svelte";
    import { PUBLIC_API_HOST } from "$env/static/public";

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
        { value: "IZT", label: "ИЗТ (Инвалид по зрению, тотальный)" },
        { value: "IZ", label: "ИЗ (Инвалид по зрению, слабовидящий)" },
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

    export let metroStations: MetroStationResponse[];

    async function fetchMetroStations() {
        try {
            const response = await fetch(
                PUBLIC_API_HOST + `api/v1/metro`,
                {
                    method: "GET",
                    headers: {
                        Authorization: `Bearer ${JWT}`,
                        "Content-Type": "application/json",
                    }
                },
            );
            if (!response.ok) {
                throw new Error("Failed to create escort request");
            }
            metroStations = await response.json()
            metroStations.sort((station1, station2) => station2.nameStation.localeCompare(station1.nameStation))
        } catch (err) {
            console.error(err);
        }
    };

    export const findMetroStationByName = (name: string) => {
        return metroStations.find((station) => station.nameStation === name)
    }
    export const findMetroStationById = (id: number) => {
        return metroStations.find((station) => station.id === id)
    }

    export const dateToTimestamp = (date: Date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, "0");
        const day = String(date.getDate()).padStart(2, "0");
        const hours = String(date.getHours()).padStart(2, "0");
        const minutes = String(date.getMinutes()).padStart(2, "0");
        const seconds = String(date.getSeconds()).padStart(2, "0");
        return `${day}.${month}.${year} ${hours}:${minutes}:${seconds}`;
    }

    await fetchMetroStations();
</script>
