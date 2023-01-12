package banking;
import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountRepository {
    private Connection conn;

    public AccountRepository(Connection conn) {
        this.conn = conn;
    }

    public void createAccount(Account account) {
        String sql = "INSERT INTO card(number,pin) VALUES(?,?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, account.getCardNumber());
            pstmt.setString(2, account.getPin());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Account findAccount(String cardNumber, String pin) {
        String sql = "SELECT id, balance FROM card WHERE number = ? AND pin = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            pstmt.setString(2, pin);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int id = rs.getInt("id");
                double balance = rs.getDouble("balance");
                return new Account(id, cardNumber, pin, balance);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public void addIncome(Account account, double income) {
        String sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, income);
            pstmt.setString(2, account.getCardNumber());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean doTransfer(Account account, String targetCardNumber, double amount) {
        String sql = "UPDATE card SET balance = balance - ? WHERE number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setString(2, account.getCardNumber());
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                sql = "UPDATE card SET balance = balance + ? WHERE number = ?";
                try (PreparedStatement pstmt2 = conn.prepareStatement(sql)) {
                    pstmt2.setDouble(1, amount);
                    pstmt2.setString(2, targetCardNumber);
                    int affectedRows2 = pstmt2.executeUpdate();
                    if (affectedRows2 > 0) {
                    }
                    return Boolean.parseBoolean(null);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}

