package Projects.Week4.employeemanagementenhancement.exceptions;

public class InvalidSalaryException extends Exception {
    public InvalidSalaryException(String message){
        super(message); //Pass the exception message to the parent class
    }
}
