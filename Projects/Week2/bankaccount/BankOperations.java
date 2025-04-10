package Projects.Week2.bankaccount;

public interface BankOperations {
    void deposit(double amount);

    void withdraw(double amount);

    double checkBalance();
}
