package Projects.Week4.employeemanagementenhancement;

import Projects.Week4.employeemanagementenhancement.exceptions.EmployeeNotFoundException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidDepartmentException;
import Projects.Week4.employeemanagementenhancement.exceptions.InvalidSalaryException;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.ArrayList;
import java.util.List;


//Main JavaFX Application for managing employees
public class EmployeeManagementApp extends Application {

    private EmployeeDatabase<String> database = new EmployeeDatabase<>();
    private EmployeeSearch<String> searchManager;
    private TableView<Employee<String>> employeeTable = new TableView<>();
    private EmployeeSalaryManager<String> salaryManager;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Employee Management System");

        searchManager = new EmployeeSearch<>(database);
        salaryManager = new EmployeeSalaryManager<>(database);

        //Layout Setup
        BorderPane root = new BorderPane();
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(10));

        //Input Fields
        HBox inputFields = new HBox(10);
        TextField nameField = new TextField();
        nameField.setPromptText("Name");
        TextField departmentField = new TextField();
        departmentField.setPromptText("Department");
        TextField salaryField = new TextField();
        salaryField.setPromptText("Salary");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Performance Rating (0-5)");
        TextField experienceField = new TextField();
        experienceField.setPromptText("Years of Experience");


        inputFields.getChildren().addAll(nameField, departmentField, salaryField, ratingField, experienceField);

        //Dropdown menu and run button
        ComboBox<String> actionDropdown = new ComboBox<>();
        actionDropdown.getItems().addAll(
                "Add Employee",
                "Remove Employee",
                "Search by Department",
                "Search by Name",
                "Sort by Salary",
                "Sort by Performance Rating",
                "Sort by Years of Experience",
                "Give Salary Raise",
                "Top 5 Highest-Paid",
                "Average Salary by Department"
        );
        actionDropdown.setPromptText("Select Action");

        Button runButton = new Button("Run");
        runButton.setStyle("-fx-background-color: #0078D7; -fx-text-fill: white; -fx-font-weight: bold;");


        // Real-Time Validation for Input Fields (New Changes)
        salaryField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) { // Allows numbers with decimals
                return change;
            }
            return null;
        }));

        ratingField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) { // Allows numbers with decimals
                return change;
            }
            return null;
        }));

        experienceField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) { // Allows integers only
                return change;
            }
            return null;
        }));

        nameField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) { // Allows letters and spaces only
                return change;
            }
            return null;
        }));

        departmentField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("[a-zA-Z\\s]*")) { // Allows letters and spaces only
                return change;
            }
            return null;
        }));



        //Event Handlers
        runButton.setOnAction(e -> {
                String selectedAction = actionDropdown.getValue();
                if(selectedAction == null){
                    showAlert("Error", "Please select an action from the dropdown");
                    return;
                }
                switch (selectedAction){
                    case "Add Employee":
                        try{
                            String name = nameField.getText().trim();
                            if (name.isEmpty()){
                                showAlert("Error", "Employee name cannot be empty or just spaces");
                                return;
                            }
                            String department = departmentField.getText().trim();
                            double performanceRating = Double.parseDouble(ratingField.getText());
                            double salary = Double.parseDouble(salaryField.getText());
                            int yearsOfExperience = Integer.parseInt(experienceField.getText());

                            if(performanceRating < 0 || performanceRating > 5){
                                showAlert("Error", "Performance rating must be between 0 and 5");
                                return;
                            }

                            if(salary < 0){
                                throw new InvalidSalaryException("Salary must be a positive number");
                            }

                            String newEmployeeId = String.format("%03d",database.getAllEmployees().size() +1);
                            Employee<String> employee = new Employee<>(newEmployeeId, name, department, salary, performanceRating, yearsOfExperience, true);
                            database.addEmployee(employee);
                            updateEmployeeTable();
                            showAlert("Success", "Employee added successfully");
                        } catch (NumberFormatException ex){
                            showAlert("Error", "Please enter valid numeric details");
                        } catch (InvalidSalaryException | IllegalArgumentException ex){
                            showAlert("Error", ex.getMessage());
                        }
                        break;

                    case "Search by Name":
                        try{
                            String name = nameField.getText().trim(); // Use nameField to input the name
                            List<Employee<String>> employeesByName = searchManager.searchByName(name);
                            employeeTable.getItems().clear();
                            employeeTable.getItems().addAll(employeesByName);
                            showAlert("Success", "Employees filtered by name.");
                        } catch (IllegalArgumentException ex){
                            showAlert("Error", ex.getMessage());
                        }
//                        if (database.getAllEmployees().isEmpty()) {
//                            showAlert("Error", "The database is empty. Please add employees before searching.");
//                            return;
//                        }

                        break;


                    case "Remove Employee":
                        try{
                            Employee<String> selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
                            if (selectedEmployee == null){
                                throw new EmployeeNotFoundException("No employee selected for removal.");
                            }
                            database.removeEmployee(selectedEmployee.getEmployeeId());
                            updateEmployeeTable();
                            showAlert("Success", "Employee removed successfully!");
                        } catch (EmployeeNotFoundException ex) {
                            showAlert("Error", ex.getMessage());
                        }
                        break;

                    case "Search by Department":
                        try{
                            String department = departmentField.getText().trim();

                            if(department.isEmpty()){
                                throw new InvalidDepartmentException("Department cannot be empty or invalid");
                            }
                            List<Employee<String>> employeesInDepartment = searchManager.searchByDepartment(department);
                            if (employeesInDepartment.isEmpty()){
                                throw new InvalidDepartmentException("No employees found for the specified department: " + department);
                            }
                            employeeTable.getItems().clear();
                            employeeTable.getItems().addAll(employeesInDepartment);
                            showAlert("Success", "Employees filtered by department");
                        } catch (InvalidDepartmentException ex){
                            // Log the exception message
                            System.out.println("Exception occurred: " + ex.getMessage());
                            showAlert("Error", ex.getMessage());
                        if (database.getAllEmployees().isEmpty()) {
                            showAlert("Error", "The database is empty. Please add employees before searching.");
                            return;
                        }

//                        if(department.isEmpty()){
//                            showAlert("Error", "Please enter a valid department name");
//                            return;
                        }
                        break;

                    case "Sort by Performance Rating":
                        if (database.getAllEmployees().isEmpty()) {
                            showAlert("Error", "The database is empty. Please add employees before sorting.");
                            return;
                        }
                        employeeTable.getItems().clear();
                        database.getAllEmployees().values().stream().sorted(new EmployeeComparators.EmployeePerformanceComparator<>()).forEach(employeeTable.getItems()::add);
                        showAlert("Success", "Employees sorted by performance rating!");
                        break;

                    case "Sort by Years of Experience":
                        if (database.getAllEmployees().isEmpty()) {
                            showAlert("Error", "The database is empty. Please add employees before sorting.");
                            return;
                        }
                        employeeTable.getItems().clear();
                        database.getAllEmployees().values().stream().sorted(new EmployeeComparators.EmployeeExperienceComparator<>()).forEach(employeeTable.getItems()::add);
                        showAlert("Success", "Employees sorted by years of experience!");
                        break;

                    case "Sort by Salary":
                        if (database.getAllEmployees().isEmpty()) {
                            showAlert("Error", "The database is empty. Please add employees before sorting.");
                            return;
                        }
                        List<Employee<String>> employeesBySalary = new ArrayList<>(database.getAllEmployees().values());
                        employeesBySalary.sort(new EmployeeComparators.EmployeeSalaryComparator<>());
                        employeeTable.getItems().clear();
                        employeeTable.getItems().addAll(employeesBySalary);
                        showAlert("Success", "Employees sorted by salary (highest first)!");
                        break;


                    case "Give Salary Raise":
                        try {
                            double raisePercentage = Double.parseDouble(ratingField.getText().trim()); // Input raise percentage
                            salaryManager.giveSalaryRaise(4.5, raisePercentage); // Apply raise to high performers (threshold 4.5)
                            updateEmployeeTable(); // Refresh TableView
                            showAlert("Success", "Salary raise applied to high performers!");
                        } catch (NumberFormatException ex) {
                            showAlert("Error", "Please enter a valid percentage for the salary raise.");
                        }
                        break;


                    case "Top 5 Highest-Paid":
                        if (database.getAllEmployees().isEmpty()) {
                            showAlert("Error", "The database is empty. Please add employees before retrieving the top 5.");
                            return;
                        }
                        List<Employee<String>> topPaidEmployees = salaryManager.getTopPaidEmployees(5);
                        employeeTable.getItems().clear();
                        employeeTable.getItems().addAll(topPaidEmployees);
                        showAlert("Success", "Displayed top 5 highest-paid employees!");
                        break;



                    case "Average Salary by Department":
                        String department = departmentField.getText().trim();
                        if (department.isEmpty()) {
                            showAlert("Error", "Please enter a valid department name.");
                            return;
                        }
                        double averageSalary = salaryManager.calculateAverageSalary(department);
                        if (averageSalary == 0.0) {
                            showAlert("Info", "No employees found in the specified department.");
                        } else {
                            showAlert("Success", "Average salary in " + department + ": $" + averageSalary);
                        }
                        break;


                    default:
                        showAlert("Error", "Invalid action selected");
                        break;
                }
        });


        //Align dropdown and button horizontally
        HBox actionBar = new HBox(10);
        actionBar.getChildren().addAll(actionDropdown, runButton);

        // Update Section (UI for Updating Employee Details)
        ComboBox<String> updateFieldDropdown = new ComboBox<>();
        updateFieldDropdown.getItems().addAll("Name", "Department", "Salary", "Performance Rating", "Years of Experience", "Active");
        updateFieldDropdown.setPromptText("Select Field to Update");

        TextField updateValueField = new TextField();
        updateValueField.setPromptText("Enter New Value");

        Button updateButton = new Button("Update");
        updateButton.setStyle("-fx-background-color: #28A745; -fx-text-fill: white; -fx-font-weight: bold;");

        HBox updateSection = new HBox(10);
        updateSection.getChildren().addAll(updateFieldDropdown, updateValueField, updateButton);


        // Dynamic Validation for Update Section
        updateFieldDropdown.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Remove any existing TextFormatter
            updateValueField.setTextFormatter(null);

            if ("Salary".equals(newValue) || "Performance Rating".equals(newValue) || "Years of Experience".equals(newValue)) {
                // Numbers only
                updateValueField.setTextFormatter(new TextFormatter<>(change -> {
                    if (change.getControlNewText().matches("\\d*(\\.\\d*)?")) {
                        return change;
                    }
                    return null;
                }));
            } else if ("Name".equals(newValue) || "Department".equals(newValue)) {
                // Text only
                updateValueField.setTextFormatter(new TextFormatter<>(change -> {
                    if (change.getControlNewText().matches("[a-zA-Z\\s]*")) {
                        return change;
                    }
                    return null;
                }));
            } else if ("Active".equals(newValue)) {
                // Boolean ("true" or "false")
                updateValueField.setTextFormatter(new TextFormatter<>(change -> {
                    if (change.getControlNewText().matches("true|false")) {
                        return change;
                    }
                    return null;
                }));
            }
        });

        //Add components to main layout
        mainLayout.getChildren().addAll(actionBar, inputFields, employeeTable, updateSection);

        //Setup TableView for employee data
        setupEmployeeTable();

        // Event Handler for the Update Button
        updateButton.setOnAction(e -> {
            Employee<String> selectedEmployee = employeeTable.getSelectionModel().getSelectedItem();
            if (selectedEmployee == null) {
                showAlert("Error", "Please select an employee to update.");
                return;
            }

            String field = updateFieldDropdown.getValue();
            if (field == null) {
                showAlert("Error", "Please select a field to update.");
                return;
            }

            String newValue = updateValueField.getText().trim();
            if (newValue.isEmpty()) {
                showAlert("Error", "Please enter a new value.");
                return;
            }

            try {
                switch (field) {
                    case "Name":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "name", newValue);
                        break;
                    case "Department":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "department", newValue);
                        break;
                    case "Salary":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "salary", Double.parseDouble(newValue));
                        break;
                    case "Performance Rating":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "performanceRating", Double.parseDouble(newValue));
                        break;
                    case "Years of Experience":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "yearsOfExperience", Integer.parseInt(newValue));
                        break;
                    case "Active":
                        database.updateEmployeeDetails(selectedEmployee.getEmployeeId(), "isActive", Boolean.parseBoolean(newValue));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field selected.");
                }

                updateEmployeeTable();
                showAlert("Success", "Employee details updated successfully!");

            } catch (NumberFormatException ex) {
                showAlert("Error", "Invalid data format for the selected field. Please try again.");
            } catch (IllegalArgumentException ex) {
                showAlert("Error", ex.getMessage());
            } catch (InvalidDepartmentException ex) {
                throw new RuntimeException(ex);
            }
        });


        //set main layout in the center of the root pane
        root.setCenter(mainLayout);

        //Set Scene
        primaryStage.setScene(new Scene(root, 800, 600));
        primaryStage.show();

    }

    //Set up the TableView with columns for employee attributes
    private void setupEmployeeTable() {
        TableColumn<Employee<String>, String> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getEmployeeId()));
        idColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee<String>, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getName()));

        TableColumn<Employee<String>, String> departmentColumn = new TableColumn<>("Department");
        departmentColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getDepartment()));

        TableColumn<Employee<String>, Double> salaryColumn = new TableColumn<>("Salary");
        salaryColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getSalary()));
        salaryColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee<String>, Double> performanceColumn = new TableColumn<>("Performance Rating");
        performanceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getPerformanceRating()));
        performanceColumn.setStyle("-fx-alignment: CENTER;");

        TableColumn<Employee<String>, Integer> experienceColumn = new TableColumn<>("Years of Experience");
        experienceColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleObjectProperty<>(data.getValue().getYearsOfExperience()));
        experienceColumn.setStyle("-fx-alignment: CENTER;");

        // Customize column widths
        idColumn.setPrefWidth(100);
        nameColumn.setPrefWidth(150);
        departmentColumn.setPrefWidth(100);
        salaryColumn.setPrefWidth(100);
        performanceColumn.setPrefWidth(150);
        experienceColumn.setPrefWidth(150);

        employeeTable.getColumns().addAll(idColumn, nameColumn, departmentColumn, salaryColumn, performanceColumn, experienceColumn);
    }

    //Update the employee List in the UI
    private void updateEmployeeTable(){
        employeeTable.getItems().clear();
        employeeTable.getItems().addAll(database.getAllEmployees().values());
    }


    //Display Alert dialog
    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
