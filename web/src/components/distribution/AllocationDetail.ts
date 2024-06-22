type AllocationDetail = {
    id: number;
    type: 'APPLICATION'|'LUNCH_BREAK'|'TRAVEL';
    datetime: string;
    from: string;
    to: string;
    application: Application;
}

type Application = {
    id: string;
}