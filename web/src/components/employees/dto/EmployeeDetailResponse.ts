type EmployeeDetailResponse = {
    id: string;
    date: string;
    timeWork: string,
    fio: string;
    uchastok: string;
    smena: string;
    rank: string;
    sex: string;
    sickLeaves: string[] | null;
    vacations: string[] | null;
    daysOff: string[] | null;
}