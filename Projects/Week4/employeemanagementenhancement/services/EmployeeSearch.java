package Projects.Week4.employeemanagementenhancement.services;
import Projects.Week4.employeemanagementenhancement.database.EmployeeDatabase;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidDepartmentException;
import Projects.Week4.employeemanagementenhancement.model.Employee;

import java.util.List;
import java.util.stream.Collectors;
public class EmployeeSearch<T> {
    private EmployeeDatabase<T> database;

    public EmployeeSearch(EmployeeDatabase<T> database){
        this.database = database;
    }

    public List<Employee<T>> searchByDepartment(String department) throws InvalidDepartmentException {
        if (department == null || department.isBlank()){
            throw new InvalidDepartmentException("Department cannot be null or empty");
        }
        return database.getAllEmployees().values().stream().filter(employee -> employee.getDepartment().equalsIgnoreCase(department)).collect(Collectors.toList());
    }

    public List<Employee<T>> searchByPerformanceRating(double rating){
        if (rating < 0 || rating > 5){
            throw new IllegalArgumentException("Rating must be between 0 and 5");
        }

        return database.getAllEmployees().values().stream().filter(employee -> employee.getPerformanceRating() >= rating).collect(Collectors.toList());
    }
    public List<Employee<T>> searchBySalaryRange(double minSalary, double maxSalary){
        if(minSalary < 0 || minSalary > maxSalary){
            throw new IllegalArgumentException("Invalid salary range");
        }
        return database.getAllEmployees().values().stream().filter(employee -> employee.getSalary() >= minSalary && employee.getSalary() <= maxSalary).collect(Collectors.toList());
    }

    public List<Employee<T>> searchByName(String name) {
        if (name == null){
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        return database.getAllEmployees().values().stream().filter(employee -> employee.getName().toLowerCase().contains(name.toLowerCase())).collect(Collectors.toList());
        // Returns a list of matching employees
    }


}
