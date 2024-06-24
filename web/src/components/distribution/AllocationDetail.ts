type AllocationDetail = {
    id: number;
    type: 'APPLICATION'|'LUNCH_BREAK'|'TRAVEL';
    datetime: string;
    from: string;
    to: string;
    applicationId: number;
    application: {id: number, idSt1: string, idSt2: string};
}

type Application = {
    id: string;
}