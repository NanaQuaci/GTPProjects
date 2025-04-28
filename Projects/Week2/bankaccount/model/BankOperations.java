package Projects.Week2.bankaccount.model;

public interface BankOperations {
    void deposit(double amount);

    void withdraw(double amount);

    double checkBalance();
}
