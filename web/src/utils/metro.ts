import { JWT } from "../utils/auth";
import { PUBLIC_API_HOST } from "$env/static/public";

export let metroStations: MetroStation[];

export async function fetchMetroStations() {
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
        let stations: MetroStationData[] = await response.json()
        metroStations = stations.map((station) => createMetroStation(station))
        metroStations.sort((station1, station2) => station2.nameStation.localeCompare(station1.nameStation))
    } catch (err) {
        console.error(err);
    }
};

function isOld(lineId: number): boolean {
    return lineId <= 15
}

export function getMetroLineIconUrl(lineId: number): string {
    if (isOld(lineId)) {
        return `https://apps3proxy.mosmetro.tech/webapp-mosmetro/station-icons/line_${lineId}.svg?raw`;
    } else {
        return `https://apps3proxy.mosmetro.tech/webapp-mosmetro/station-icons/d${lineId - 15}.svg?raw`;
    }
}

export function createMetroStation(station: MetroStationData | undefined): MetroStation {
    if (!station) return {
        id: 0,
        nameStation: 'Не указано',
        nameLine: 'Не указана',
        idLine: 0,
        iconUrl: '',
        iconHtml: ''
    }

    let url = getMetroLineIconUrl(station.idLine);
    let old = isOld(station.idLine);
    
    return {
        ...station,
        iconUrl: url,
        iconHtml: `<img width="${old ? 20 : 32}rem" style='max-height: 26rem; transform: translateY(-2rem);' src=${url} alt=${station.nameStation}/>`,
    };
}

export const findMetroStationByName = (name: string) => {
    let find = metroStations.find((station) => station.nameStation === name);
    if (find) return createMetroStation(find)
}

export const findMetroStationById = (id: any) => {
    let find = metroStations.find((station) => station.id === id);
    if (find) return createMetroStation(find)
}