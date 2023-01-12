package banking;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.sql.DriverManager;


public class Main {

    private static AccountRepository accountRepository;
    static List<Account> accountList = new ArrayList<>();
    static Account currentAccount = null;


    public static void main(String[] args) {

        String fileName = null;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-fileName")) {
                fileName = args[i+1];
            }
        }
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:" + fileName);
            String sql = "CREATE TABLE IF NOT EXISTS card (\n"
                    + " id INTEGER PRIMARY KEY,\n"
                    + " number TEXT NOT NULL,\n"
                    + " pin TEXT NOT NULL,\n"
                    + " balance INTEGER  DEFAULT 0\n"
                    + ");";
            try (Statement stmt = conn.createStatement()) {
                ((Statement) stmt).execute(sql);
            } catch (SQLException e) {
                System.out.println(e.getMessage());
            }
            System.out.println("Connection to SQLite database established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        AccountRepository repository = new AccountRepository(conn);


        Scanner sc = new Scanner(System.in);


        int choice = -1;

        while (choice != 0) {
            System.out.println("1. Create an account\n" + "2. Log into account\n" + "0. Exit");
            choice = sc.nextInt();

            switch (choice) {
                case 1:
                    Account random = new Account();
                    random.createCardNumber();
                    random.createPin();
                    accountList.add(random);
                    repository.createAccount(random);
                    printCardAndPin(random);
                    break;
                case 2:
                    System.out.println("Enter your card number:");
                    String cardNum = sc.next();
                    System.out.println("Enter your PIN:");
                    String pin = sc.next();
                    currentAccount = findAccount(accountList, cardNum, pin);
                    //System.out.println("Current account: " + currentAccount);
                    if (currentAccount != null) {
                        System.out.println("You have successfully logged in!");
                        int option = -1;

                        while (option != 0) {
                            System.out.println("1. Balance\n" + "2. Log out \n" + "0. Exit");
                            option = sc.nextInt();
                            if (option == 1) {
                                System.out.println("Balance: " + currentAccount.getBalance());
                            } else if (option == 2) {
                                option = 0;
                                currentAccount = null;
                                System.out.println("You have successfully logged out!");
                            } else if (option == 0) {
                                choice = 0;
                                System.out.println("Bye");
                                break;

                            }
                        }
                    } else {
                        System.out.println("Wrong card number or PIN");
                        //System.out.println("Lista accounta:" + accountList);
                    }
                    break;

                case 0:
                    System.out.println("Bye");
                    break;

            }
        }
    }

    public static void printCardAndPin(Account random) {
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(random.getCardNumber());
        System.out.println("Your card PIN:");
        System.out.println(random.getPin());
    }
    public static Account findAccount(List<Account> accountList, String cardNum, String pin) {
        for (Account a : accountList){
            if (cardNum.equals(a.getCardNumber()) && pin.equals(a.getPin())){
                return a;
            }
        }
        return null;
    }
}