type PassengerResponse = {
    id: string;
    fullName: string;
    contactNumbers: {number: string, description: string | null }[];
    gender: string;
    category: string;
    additionalInfo?: string;
    hasPacemaker: boolean;
}