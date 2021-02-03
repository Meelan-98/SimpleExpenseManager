package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.DbHelper;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersTransactionDAO;

public class PersistentExpenseManager extends  ExpenseManager{
    private final Context context;
    public PersistentExpenseManager(Context con)  {
        this.context= con;
        try {
            setup();
        } catch (ExpenseManagerException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void setup() throws ExpenseManagerException {
        DbHelper dbHelper = new DbHelper(context);

        TransactionDAO transDAO = new PersTransactionDAO(dbHelper) ;
        setTransactionsDAO(transDAO);

        AccountDAO perAccountDAO = new PersAccountDAO(dbHelper);
        setAccountsDAO(perAccountDAO);


    }
}
