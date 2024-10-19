package com.flywire.exercise.controller;

import com.flywire.exercise.model.Employee;
import com.flywire.exercise.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

// Controller to expose REST endpoints for employee management
@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService; // Inject EmployeeService to handle business logic

    // Endpoint to get all employees
    @CrossOrigin
    @GetMapping("/")
    public List<Employee> getEmployees() {
        return employeeService.getEmployees();
    }

    // Endpoint to get all active employees sorted by last name
    @CrossOrigin
    @GetMapping("/active")
    public List<Employee> getActiveEmployees() {
        return employeeService.getActiveEmployeesSortedByLastName();
    }

    // Endpoint to get an employee by ID, including their direct reports
    @CrossOrigin
    @GetMapping("/{id}")
    public HashMap<String, Object> getEmployeeById(@PathVariable int id) throws Exception {
        Optional<Employee> employee = employeeService.getEmployeeById(id);
        if (employee.isPresent()) {
            Employee emp = employee.get();
            List<Employee> directReports = emp.getDirectReports().stream()
                    .map(reportId -> employeeService.getEmployeeById(reportId).orElse(null))
                    .filter(Objects::nonNull)
                    .toList();
            List<String> directReportsList = (directReports.stream().map(Employee::getName).toList());
            HashMap<String, Object> employeeDetails = new HashMap<>();
            employeeDetails.put("employee", emp);
            employeeDetails.put("directReports", directReportsList);
            return employeeDetails;
        } else {
            throw new Exception("Employee not found"); // Handle invalid ID case
        }
    }

    // Endpoint to get employees hired within a specific date range
    @CrossOrigin
    @GetMapping("/hired")
    public List<Employee> getEmployeesHiredInRange(
            @RequestParam String startDate,
            @RequestParam String endDate) throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date start = dateFormat.parse(startDate); // Parse start date
        Date end = dateFormat.parse(endDate); // Parse end date
        return employeeService.getEmployeesHiredWithinRange(start, end);
    }

    // Endpoint to add a new employee
    @CrossOrigin
    @PostMapping
    public String addEmployee(@RequestBody Employee employee) {
        try {
            employeeService.addEmployee(employee); // Add employee to the list
            return "Employee added successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage(); // Handle validation errors
        }
    }

    // Endpoint to deactivate an employee by ID
    @CrossOrigin
    @PutMapping("/deactivate/{id}")
    public String deactivateEmployee(@PathVariable int id) {
        try {
            employeeService.deactivateEmployee(id); // Deactivate employee
            return "Employee deactivated successfully";
        } catch (Exception e) {
            return "Error: " + e.getMessage(); // Handle errors
        }
    }
}
