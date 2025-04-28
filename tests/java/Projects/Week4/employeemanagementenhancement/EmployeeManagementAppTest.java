package Projects.Week4.employeemanagementenhancement;
import static org.junit.jupiter.api.Assertions.*;

import Projects.Week4.employeemanagementenhancement.database.EmployeeDatabase;
import Projects.Week4.employeemanagementenhancement.exceptions.EmployeeNotFoundException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidDepartmentException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidSalaryException;
import Projects.Week4.employeemanagementenhancement.model.Employee;
import Projects.Week4.employeemanagementenhancement.services.EmployeeSearch;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;

public class EmployeeManagementAppTest {
    private EmployeeDatabase<String> database;
    private EmployeeSearch<String> searchManager;

    @BeforeEach
    void setUp() {
        database = new EmployeeDatabase<>();
        searchManager = new EmployeeSearch<>(database);
    }

    @Test
    void testAddEmployee() throws InvalidSalaryException, EmployeeNotFoundException {
        Employee<String> employee = new Employee<>("001", "John Doe", "Engineering", 60000.0, 4.8, 5, true);
        database.addEmployee(employee);

        Employee<String> retrieved = database.getEmployeeById("001");
        assertNotNull(retrieved, "Employee should not be null.");
        assertEquals("John Doe", retrieved.getName());
        assertEquals("Engineering", retrieved.getDepartment());
    }

    @Test
    void testSearchByDepartment() throws InvalidSalaryException, InvalidDepartmentException {
        database.addEmployee(new Employee<>("001", "John Doe", "Engineering", 60000.0, 4.8, 5, true));
        database.addEmployee(new Employee<>("002", "Jane Smith", "Engineering", 65000.0, 4.6, 6, true));

        List<Employee<String>> engineeringEmployees = searchManager.searchByDepartment("Engineering");
        assertEquals(2, engineeringEmployees.size(), "Expected two employees in Engineering.");
    }

    @Test
    void testRemoveEmployee() throws InvalidSalaryException {
        Employee<String> employee = new Employee<>("001", "John Doe", "Engineering", 60000.0, 4.8, 5, true);
        database.addEmployee(employee);
        database.removeEmployee("001");

        assertThrows(EmployeeNotFoundException.class, () -> database.getEmployeeById("001"));
    }


}
