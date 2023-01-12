package banking;
import java.util.List;
import java.util.Random;

public class Account {
    Random ra = new Random();
    private String cardNumber;
    private String pin;
    private double balance;

    public Account(int id, String cardNumber, String pin, double balance) {
    }

    public Account() {

    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }
    public String createCardNumber() {
        Random random = new Random();
        String accountIdentifier = String.format("%09d", (long) (Math.random() * 999999999L));
        String bankIdentificationNumber = "400000";
        String cardNumberForCheckSum =  bankIdentificationNumber + accountIdentifier;
        String cardNumber = bankIdentificationNumber + accountIdentifier + generateCheckSum(cardNumberForCheckSum);
        this.cardNumber = cardNumber;
        return cardNumber;
    }
    public String createPin() {
        String pinNumber = String.format("%04d", (long) (Math.random() * 9999L));
        this.pin = pinNumber;
        return pin;

    }
    public static Account findAccount (List<Account> accountList, String cardNum, String pin) {
        System.out.println("Card number: " + cardNum + ", PIN: " + pin);
        for (Account a : accountList){
            if (cardNum.equals(a.getCardNumber()) && pin.equals(a.getPin())){
                return a;
            }
        }
        return null;
    }
    public boolean isCardNumberValid(String cardNumber) {
        int checksum = generateCheckSum(cardNumber);
        return checksum == 0;
    }
    public int generateCheckSum(String cardNumberForCheckSum) {
        int sum = 0;
        int remainder = (cardNumberForCheckSum.length() + 1) % 2;
        for (int i = 0; i < cardNumberForCheckSum.length(); i++) {
            int digit = Integer.parseInt(cardNumberForCheckSum.substring(i, (i + 1)));
            if ((i % 2) == remainder) {
                digit = digit * 2;
                if (digit > 9) {
                    digit = (digit - 9);
                }
            }
            sum += digit;
        }
        int mod = sum % 10;
        int checkSumDigit = ((mod == 0) ? 0 : 10 - mod);
        return checkSumDigit;
    }
}