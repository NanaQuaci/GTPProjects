package Projects.Week2.bankaccount.app;

import Projects.Week2.bankaccount.model.BankAccount;
import Projects.Week2.bankaccount.model.Transaction;
import Projects.Week2.bankaccount.services.CurrentAccount;
import Projects.Week2.bankaccount.services.FixedDepositAccount;
import Projects.Week2.bankaccount.services.SavingsAccount;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.ObservableList;
import java.util.Date;


public class BankAccountManagementSystem extends Application {

    private Map<String, BankAccount> accounts = new HashMap<>();

    private void updateTransactionTable(BankAccount account, TableView<Transaction> transactionTable){
        ObservableList<Transaction> items = FXCollections.observableArrayList(account.getLastNTransactions(10));
        transactionTable.setItems(items);
    }

    @Override
    public void start(Stage primaryStage) {
        //UI Components
        TextField accountNumberField = new TextField(); //field for account number input
        accountNumberField.setPromptText("Account Number");
        accountNumberField.setPrefWidth(200);

        TextField amountField = new TextField(); //field for amount input
        amountField.setPromptText("Amount");
        amountField.setPrefWidth(200);

        TextField minBalanceField = new TextField();
        minBalanceField.setPromptText("Minimum Balance (Savings)");
        minBalanceField.setPrefWidth(200);

        TextField overdraftLimitField = new TextField();
        overdraftLimitField.setPromptText("Overdraft Limit (Current)");
        overdraftLimitField.setPrefWidth(200);

        DatePicker maturityDatePicker = new DatePicker();
        maturityDatePicker.setPromptText("Maturity Date (Fixed Deposit)");

        Button createSavingsButton = new Button("Create Savings Account");
        Button createCurrentButton = new Button("Create Current Account");
        Button createFixedButton = new Button("Create Fixed Deposit Account");
        Button depositButton = new Button("Deposit");
        Button withdrawButton = new Button("Withdraw");
        TableView<Transaction> transactionTable = new TableView<>();
        TableColumn<Transaction, String> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty().asString());
        TableColumn<Transaction, String> typeColumn = new TableColumn<>("Type");
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        TableColumn<Transaction, Double> amountColumn = new TableColumn<>("Amount");
        amountColumn.setCellValueFactory(cellData -> cellData.getValue().amountProperty().asObject());
        transactionTable.getColumns().addAll(dateColumn, typeColumn, amountColumn);


        createSavingsButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            double balance = parseDouble(amountField.getText());
            double minBalance = parseDouble(minBalanceField.getText());
            if(accountNumber.isEmpty() || balance < 0 || minBalance < 0){
                showAlert("Error", "Invalid input. Please enter valid values.");
                return;
            }
            accounts.put(accountNumber, new SavingsAccount(accountNumber, balance, minBalance));
            showAlert("Account created", "Savings Account created successfully");
        });

        createCurrentButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            double balance = parseDouble(amountField.getText());
            double overdraftLimit = parseDouble(overdraftLimitField.getText());
            if(accountNumber.isEmpty() || balance < 0 || overdraftLimit < 0){
                showAlert("Error", "Invalid input. Please enter valid values.");
                return;
            }
            accounts.put(accountNumber, new CurrentAccount(accountNumber, balance, overdraftLimit));
            showAlert("Account created", "Current account created successfully");
        });

        createFixedButton.setOnAction(e ->{
            String accountNumber = accountNumberField.getText();
            double balance = parseDouble(amountField.getText());
            Date maturityDate = java.sql.Date.valueOf(maturityDatePicker.getValue());
            if(accountNumber.isEmpty() || balance < 0 || maturityDate == null){
                showAlert("Error", "Invalid Input. Please enter valid values.");
                return;
            }
            accounts.put(accountNumber, new FixedDepositAccount(accountNumber, balance, maturityDate));
            showAlert("Account created", "Fixed Deposit Account created Successfully");
        });


        depositButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            double amount = parseDouble(amountField.getText());
            if(accountNumber.isEmpty() || amount < 0){
                showAlert("Error", "Invalid input. Please enter valid values");
                return;
            }

            //Find Account and perform deposit
            BankAccount account = findAccount(accountNumber);
            if (account != null){
                account.deposit(amount);
                account.addTransaction("Deposit", amount);
                updateTransactionTable(account, transactionTable);
                showAlert("Transaction Successful", "Deposit Successful");
            } else {
                showAlert("Error", "Account not found");
            }
        });

        withdrawButton.setOnAction(e -> {
            String accountNumber = accountNumberField.getText();
            double amount = parseDouble(amountField.getText());
            if(accountNumber.isEmpty() || amount < 0){
                showAlert("Error", "Invalid input. Please enter valid values.");
                return;
            }
            BankAccount account = findAccount(accountNumber);
            if(account != null){
                account.withdraw(amount);
                account.addTransaction("Withdrawal", amount);
                updateTransactionTable(account, transactionTable);
                showAlert("Transaction Successful", "Withdrawal successful");
            } else {
                showAlert("Error", "Account not found!");
            }
        });





        //Layout for the UI components

        GridPane layout = new GridPane();
        layout.setHgap(10);
        layout.setVgap(10);
        layout.setPadding(new Insets(20));

 layout.add(new Label("Account Number"), 0, 0);
 layout.add(accountNumberField, 1, 0);
 layout.add(new Label("Amount"), 0, 1);
 layout.add(amountField, 1, 1);
 layout.add(new Label("Minimum Balance (Savings)"), 0, 2);
 layout.add(minBalanceField, 1, 2);
 layout.add(new Label("Overdraft Limit (Current)"), 0, 3);
 layout.add(overdraftLimitField, 1, 3);
 layout.add(new Label("Maturity Date (Fixed Deposit)"), 0, 4);
 layout.add(maturityDatePicker, 1, 4);

        VBox buttonBox = new VBox(10, createSavingsButton, createCurrentButton, createFixedButton, depositButton, withdrawButton);
        buttonBox.setAlignment(Pos.CENTER);
        layout.add(buttonBox, 0, 5, 2, 1);

        layout.add(transactionTable, 0, 6, 2, 1);



        Scene scene = new Scene(layout);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Bank Account Management");
        primaryStage.sizeToScene();
        primaryStage.show();

    }

    private BankAccount findAccount(String accountNumber){
        return accounts.get(accountNumber);
    }

    private void showAlert(String title, String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private double parseDouble(String text){
        try{
            return Double.parseDouble(text);
        } catch (NumberFormatException e){
            return -1;
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}
