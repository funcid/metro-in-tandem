type Allocation = {
    employee: Employee;
    allocations: AllocationDetail[];
    applications: {id: number, idSt1: string, idSt2: string}[];
    lunchTime: string
}