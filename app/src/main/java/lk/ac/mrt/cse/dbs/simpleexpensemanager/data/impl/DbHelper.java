package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;


public class DbHelper extends SQLiteOpenHelper {
    public static final String db = "expenses.db";
    public static final int version = 4;

    //tables
    private static final String acc = "account";
    private static final String logs = "transactions";

    //account table cols
    private static final String accountNo = "accountNo";
    private static final String bankName = "bankName";
    private static final String accountHolderName = "accountHolderName";
    private static final String balance = "balance";

    //transaction table cols

    private static final String date = "date";
    private static final String accNo = "accountNo";
    private static final String expenseType = "expenseType";
    private static final String amount = "amount";

    // account create table statement


    private final Context context;


    public DbHelper(Context context) {

        super(context, db, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

         final String AccountTable= "CREATE TABLE " + acc
                + "(" + accountNo + " TEXT(50) PRIMARY KEY," + bankName + " TEXT(50),"
                + accountHolderName +" TEXT(30)," + balance + " REAL" + ")";

        // transaction create table statement

         final String LogTable= "CREATE TABLE " + logs
                + "(" + date + " DATE," + accNo + " TEXT(50),"
                + expenseType +" TEXT(30)," + amount + " REAL" + ", FOREIGN KEY (" + accNo +") REFERENCES "+ acc + "(" + accNo +"))";


        db.execSQL(AccountTable);
        db.execSQL(LogTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(" DROP TABLE IF EXISTS " + acc);
        db.execSQL(" DROP TABLE IF EXISTS " + logs);
        onCreate(db);

    }


// Account Table Handling

    public Account getSingleAccount(String acco){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ acc +" WHERE "+ accountNo +" =?",new String[]{acco});
        Account account = null;
        if (res.getCount() != 0) {
            while (res.moveToNext()) {
                String accountNo = res.getString(0);
                String bankName = res.getString(1);
                String accountHolderName = res.getString(2);
                double balance = res.getDouble(3);
                account = new Account(accountNo, bankName, accountHolderName, balance);
            }
        }
        return account;
    }


    public  List<Account> getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + acc, null);

        if (res.getCount() == 0) {
            return accounts;
        }
        while (res.moveToNext()) {
            String accountNo = res.getString(0);
            String bankName = res.getString(1);
            String accountHolderName = res.getString(2);
            double balance = res.getDouble(3);
            Account temp = new Account(accountNo, bankName, accountHolderName, balance);
            accounts.add(temp);

        }
        return accounts;

    }

    public  Boolean addAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(accountNo,account.getAccountNo());
        values.put(bankName,account.getBankName());
        values.put(accountHolderName,account.getAccountHolderName());
        values.put(balance,account.getBalance());

        long res = db.insert(acc,null , values);
        if(res==-1){
            return false;
        }
        return true;


    }

    public Boolean updateAccount(Account account){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(accountNo,account.getAccountNo());
        values.put(bankName,account.getBankName());
        values.put(accountHolderName,account.getAccountHolderName());
        values.put(balance,account.getBalance());

        long res = db.update(acc, values, accountNo +"=?",new String[]{account.getAccountNo()} );
        if(res==-1){
            return false;
        }
        return true;

    }





    public boolean deleteAccount(String accNO){
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(acc, accountNo+"= "+accNO,null);
        if (res> 0){
            return true;
        }
        return false;

    }


 // transaction table handling



    public  List<Transaction> getAllTransactions() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+logs,null);

        List<Transaction> transactions=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        if (res.getCount() >= 0) {

            while (res.moveToNext()) {

                try {
                    String accountNo = res.getString(1);
                    ExpenseType expenseType = ExpenseType.valueOf(res.getString(2));
                    Date date = format.parse(res.getString(0));

                    double amount = res.getDouble(3);
                    transactions.add(new Transaction(date, accountNo, expenseType, amount));
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }
        return transactions;


    }

    public  List<Transaction> getAllTransactionsLimited(int limit)  {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+ logs + " LIMIT " + limit,null);

        List<Transaction> transactions=new ArrayList<>();
        DateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        if (res.getCount() >= 0) {

            while (res.moveToNext()) {
                System.out.print(res);

                try {
                    String accountNo = res.getString(1);
                    ExpenseType expenseType = ExpenseType.valueOf(res.getString(2));
                    Date date = format.parse(res.getString(0));
                    double amount = res.getDouble(3);
                    transactions.add(new Transaction(date, accountNo, expenseType, amount));

                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }
        }
        return transactions;

    }

    public boolean enterTransaction(Transaction transaction){



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        System.out.println(transaction.getDate());
        values.put(date,transaction.getDate().toString());
        values.put(accNo,transaction.getAccountNo());
        values.put(expenseType,transaction.getExpenseType().toString());
        values.put(amount,transaction.getAmount());


        long res = db.insert(logs,null,values);
        if(res == -1){
            return false;
        }
        return true;
    }

}
