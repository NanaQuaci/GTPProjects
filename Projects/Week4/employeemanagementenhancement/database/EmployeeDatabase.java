package Projects.Week4.employeemanagementenhancement.database;
//import Projects.Week4.employeemanagementenhancement.exceptions;
import Projects.Week4.employeemanagementenhancement.model.Employee;
import Projects.Week4.employeemanagementenhancement.exceptions.EmployeeNotFoundException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidDepartmentException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidSalaryException;

import java.util.HashMap;
import java.util.Map;

public class EmployeeDatabase<T> {
    private Map<T, Employee<T>> employees = new HashMap<>();

    //add a new employee to the database
    public void addEmployee(Employee<T> employee) throws InvalidSalaryException {
        if (employee.getSalary() < 0){
            throw new InvalidSalaryException("Salary cannot be negative");
        }
        employees.put(employee.getEmployeeId(), employee);
    }

    //retrieve an employee by ID
    public Employee<T> getEmployeeByID(T employeeId) throws EmployeeNotFoundException {
        Employee<T> employee = employees.get(employeeId);
        if(employee == null){
            throw new EmployeeNotFoundException("Employee with ID " + employeeId + " not found.");
        }
        return employee;
    }


    //Remove an employee from the database by ID
    public void removeEmployee(T employeeId){
        employees.remove(employeeId);
    }

    public void updateEmployeeDetails(T employeeId, String field, Object newValue) throws InvalidDepartmentException {
        Employee<T> employee = employees.get(employeeId);
        if (employee != null){
            switch (field) {
                case "name":
                    employee.setName((String) newValue);
                    break;
                case "department":
                    if (newValue == null || ((String) newValue).isBlank()){
                        throw new InvalidDepartmentException("Department cannot be null or empty.");
                    }
                    employee.setDepartment((String) newValue);
                    break;
                case "salary":
                    employee.setSalary((Double) newValue);
                    break;
                case "performanceRating":
                    employee.setPerformanceRating((Double) newValue);
                    break;
                case "yearsOfExperience":
                    employee.setYearsOfExperience((Integer) newValue);
                    break;
                case "isActive":
                    employee.setActive((Boolean) newValue);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid field: " + field);
            }
        }
    }
    public Map<T, Employee<T>> getAllEmployees(){
        return employees;
    }

    public Employee<T> getEmployeeById(String id) throws EmployeeNotFoundException {
        if (!employees.containsKey(id)){
            throw new EmployeeNotFoundException("EMployee with ID " + id + " not found.");
        }
        return employees.get(id);
    }
}
