package com.flywire.exercise.service;

import com.flywire.exercise.model.Employee;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

// Service class that contains the business logic for employee management
@Service
public class EmployeeService {

    @Autowired
    private ResourceLoader resourceLoader;

    private List<Employee> employees; // List to hold all employees
    private final ObjectMapper mapper = new ObjectMapper(); // JSON object mapper for reading/writing JSON data
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy"); // Date formatter

    // Constructor loads data from the JSON file when the service is initialized
    public EmployeeService() {
        loadData();
    }

    // Loads employee data from data.json into the employees list
    private void loadData() {
        try {
            this.resourceLoader = new DefaultResourceLoader();
            Resource resource = this.resourceLoader.getResource("/json/data.json");
            employees = mapper.readValue(resource.getInputStream(), new TypeReference<List<Employee>>() {});
        } catch (Exception e) {
            e.printStackTrace(); // Handle loading errors
        }
    }

    //Returns all employees
    public List<Employee> getEmployees() {
        return employees;
    }

    // Returns a list of active employees sorted by last name
    public List<Employee> getActiveEmployeesSortedByLastName() {
        return employees.stream()
                .filter(Employee::isActive) // Filter only active employees
                .sorted(Comparator.comparing(Employee::getLastName)) // Sort by last name
                .collect(Collectors.toList()); // Collect results into a list
    }

    // Finds and returns an employee by their ID
    public Optional<Employee> getEmployeeById(int id) {
        return employees.stream().filter(e -> e.getId() == id).findFirst();
    }

    // Returns a list of employees hired within a given date range, sorted by descending hire date
    public List<Employee> getEmployeesHiredWithinRange(Date start, Date end) {
        return employees.stream()
                .filter(e -> {
                    try {
                        Date hireDate = dateFormat.parse(e.getHireDate());
                        return hireDate.after(start) && hireDate.before(end); // Check if within range
                    } catch (ParseException ex) {
                        ex.printStackTrace(); // Handle date parsing errors
                        return false;
                    }
                })
                .sorted(Comparator.comparing(e -> {
                    try {
                        return dateFormat.parse(e.toString()); // Sort by hire date
                    } catch (ParseException ex) {
                        return new Date(0); // Default to epoch if parsing fails
                    }
                })).toList();
    }

    // Adds a new employee to the list and saves it to the JSON file
    public void addEmployee(Employee employee) throws Exception {
        if (employees.stream().anyMatch(e -> e.getId() == employee.getId())) {
            throw new Exception("Employee with this ID already exists"); // Validate unique ID
        }
        employees.add(employee); // Add the new employee
        saveData(); // Save the updated list to JSON
    }

    // Deactivates an employee by their ID
    public void deactivateEmployee(int id) throws Exception {
        Employee employee = getEmployeeById(id).orElseThrow(() -> new Exception("Employee not found"));
        employee.setActive(false); // Set active status to false
        saveData(); // Save the updated data to JSON
    }

    // Saves the current employee list to the data.json file
    private void saveData() {
        try {
            mapper.writeValue(new File("src/main/resources/json/data.json"), employees);
        } catch (Exception e) {
            e.printStackTrace(); // Handle file writing errors
        }
    }
}
