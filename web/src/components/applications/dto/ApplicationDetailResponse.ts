type ApplicationDetailResponse = {
    idPas: string;
    fio: string;
    datetime: string; // Формат dd.MM.yyyy HH:mm:ss
    time3: string; // Формат HH:mm:ss
    time4: string; // Формат HH:mm:ss
    catPas: string;
    status?: string;
    tpz: string; // Формат dd.MM.yyyy HH:mm:ss
    inspSexM: number;
    inspSexF: number;
    timeOver: string; // Формат HH:mm:ss
    stationFrom: MetroStationData;
    stationTo: MetroStationData;
    duration: string;
    transplants: number
}