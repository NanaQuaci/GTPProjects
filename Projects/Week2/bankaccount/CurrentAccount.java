package Projects.Week2.bankaccount;

public class CurrentAccount extends BankAccount implements BankOperations{
    private double overdraftLimit;

    public CurrentAccount(String accountNumber, double balance, double overdraftLimit){
        super(accountNumber, balance);
        this.overdraftLimit = overdraftLimit;
    }

    @Override
    public void deposit(double amount){
        balance += amount;
    }

    @Override
    public void withdraw(double amount){
        if(balance - amount >= -overdraftLimit){
            balance -= amount;
        } else {
            System.out.println("Overdraft limit exceeded");
        }
    }

    @Override
    public double checkBalance(){
        return balance;
    }
}
