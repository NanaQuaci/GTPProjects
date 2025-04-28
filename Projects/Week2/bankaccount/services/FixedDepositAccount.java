package Projects.Week2.bankaccount.services;
import Projects.Week2.bankaccount.model.BankAccount;
import Projects.Week2.bankaccount.model.BankOperations;

import java.util.Date;

public class FixedDepositAccount extends BankAccount implements BankOperations {
    private Date maturityDate;

    public FixedDepositAccount(String accountNumber, double balance, Date maturityDate){
        super(accountNumber, balance);
        this.maturityDate = maturityDate;
    }

    @Override
    public void deposit(double amount){
        System.out.println("Cannot deposit into a fixed deposit account");
    }

    @Override
    public void withdraw(double amount){
        Date currentDate = new Date();
        if(currentDate.after(maturityDate)){
            balance -= amount;
        } else {
            System.out.println("Cannot withdraw before maturity date");
        }
    }

    public double checkBalance(){
        return balance;
    }
}
