type PassengerDetailResponse = {
    id: number;
    fullName: string;
    contactNumbers: Array<ContactNumber>;
    gender: string;
    category: string;
    additionalInfo?: string;
    hasPacemaker: boolean;
}

type ContactNumber = {
    number: string;
    description?: string;
}