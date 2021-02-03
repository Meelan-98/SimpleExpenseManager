package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersTransactionDAO implements TransactionDAO {
    private final DbHelper dbHelper;

    public PersTransactionDAO(DbHelper dbHelper) {
        this.dbHelper = dbHelper;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {

        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
        if(accountNo != null){
            dbHelper.enterTransaction(transaction);
        }



    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dbHelper.getAllTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        return dbHelper.getAllTransactionsLimited(limit);
    }
}
