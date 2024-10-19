import React, { useEffect, useState } from 'react';
import axios from 'axios';

const EmployeeList = () => {
    const [employees, setEmployees] = useState([]);
    const [filterActive, setFilterActive] = useState(false);

    useEffect(() => {
        const fetchEmployees = async () => {
            const headers = {
                "Content-Type": "application/json",
            };
            const response = await axios.get(
                filterActive
                    ? 'http://localhost:9090/api/employees/active'
                    : 'http://localhost:9090/api/employees/'
            , headers);
            setEmployees(response.data);
        };
        fetchEmployees();
    }, [filterActive]);

    return (
        <div>
            <h1>Employee List</h1>
            <button onClick={() => setFilterActive(!filterActive)}>
                {filterActive ? 'Show All' : 'Show Active Only'}
            </button>
            <ul>
                {employees.map(employee => (
                    <li key={employee.id}>
                        {employee.name} - {employee.position} ({employee.active ? 'Active' : 'Inactive'})
                    </li>
                ))}
            </ul>
        </div>
    );
};

export default EmployeeList;
