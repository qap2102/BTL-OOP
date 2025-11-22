package Service;

import DataAccessObject.*;
import database.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import model.*;

public class TransactionService{

    private final AccountDAO a = new AccountDAO();
    private final TransactionDAO t = new TransactionDAO();

    // [NẠP TIỀN] Thực hiện nạp tiền (Sử dụng Transaction để đảm bảo an toàn dữ liệu)
    public synchronized void deposit(String accountNumber, BigDecimal amount, String PIN, String cont) throws SQLException{
        // Kiểm tra số tiền nạp hợp lệ
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        
        try(Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false); // Bắt đầu Transaction
            try{
                Account acc = a.findByAccountNumber(accountNumber);
                
                // Kiểm tra tài khoản tồn tại
                if(acc == null){
                    throw new IllegalArgumentException("Account does not exist.");
                }
                // Kiểm tra trạng thái tài khoản
                if(acc.getAccountStatus().equals("LOCKED")){
                    throw new IllegalArgumentException("Account has been locked.");
                }
                // Kiểm tra mã PIN
                if(!acc.getPinSmart().equals(PIN)){
                    throw new IllegalArgumentException("PIN code is incorrect.");
                }
                
                // Cập nhật số dư mới
                BigDecimal newBalance = acc.getBalance().add(amount);
                a.updateBalance_service(conn, acc.getAccountID(), newBalance);
                
                // Ghi lịch sử giao dịch
                t.createTransaction_service(conn, null, acc.getAccountID(), amount, "DEPOSIT", cont);
                
                conn.commit(); // Xác nhận Transaction thành công
            } catch(SQLException e){
                conn.rollback(); // Hoàn tác nếu có lỗi
                throw e;
            }
        }
    }

    // [RÚT TIỀN] Thực hiện rút tiền (Kiểm tra số dư, PIN, Trạng thái)
    public synchronized void withdraw(String accountNumber, BigDecimal amount, String PIN, String cont) throws SQLException{
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        try(Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false); // Bắt đầu Transaction
            try{
                Account acc = a.findByAccountNumber(accountNumber);
                
                if(acc == null){
                    throw new IllegalArgumentException("Account does not exist.");
                }
                if(acc.getAccountStatus().equals("LOCKED")){
                    throw new IllegalArgumentException("Account has been locked.");
                }
                if(!acc.getPinSmart().equals(PIN)){
                    throw new IllegalArgumentException("PIN code is incorrect.");
                }
                // Kiểm tra số dư có đủ không
                if(acc.getBalance().compareTo(amount) < 0)
                    throw new IllegalArgumentException("Insufficident balance.");
                
                // Trừ tiền và cập nhật số dư
                BigDecimal newBalance = acc.getBalance().subtract(amount);
                a.updateBalance_service(conn, acc.getAccountID(), newBalance);
                
                // Ghi lịch sử giao dịch
                t.createTransaction_service(conn, acc.getAccountID(), null, amount, "WITHDRAW", cont);
                
                conn.commit();
            } catch(SQLException e){
                conn.rollback();
                throw e;
            }
        }
    }

    // [CHUYỂN KHOẢN] Chuyển tiền giữa 2 tài khoản (Trừ người gửi, cộng người nhận)
    public synchronized void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount, String PIN, String cont) throws SQLException{
        if(fromAccountNumber.equals(toAccountNumber)){
            throw new IllegalArgumentException("Invalid account.");
        }
        if(amount.compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("Deposit amount must be greater than 0.");
        }
        
        try(Connection conn = DatabaseConnection.getConnection()){
            conn.setAutoCommit(false); // Bắt đầu Transaction
            try{
                Account from = a.findByAccountNumber(fromAccountNumber);
                Account to = a.findByAccountNumber(toAccountNumber);
                
                // Kiểm tra tài khoản gửi và nhận
                if(from == null || to == null){
                    throw new IllegalArgumentException("Account does not exist.");
                }
                if(from.getAccountStatus().equals("LOCKED") || to.getAccountStatus().equals("LOCKED")){
                    throw new IllegalArgumentException("Account has been locked.");
                }
                if(!from.getPinSmart().equals(PIN)){
                    throw new IllegalArgumentException("PIN code is incorrect.");
                }
                if(from.getBalance().compareTo(amount) < 0){
                    throw new IllegalArgumentException("Insufficient balance.");
                }
                
                // Tính toán số dư mới
                BigDecimal newFromBalance = from.getBalance().subtract(amount);
                BigDecimal newToBalance = to.getBalance().add(amount);
                
                // Cập nhật DB
                a.updateBalance_service(conn, from.getAccountID(), newFromBalance);
                a.updateBalance_service(conn, to.getAccountID(), newToBalance);
                
                // Ghi lịch sử giao dịch
                t.createTransaction_service(conn, from.getAccountID(), to.getAccountID(), amount, "TRANSFER", cont);
                
                conn.commit();
            } catch(SQLException e){
                conn.rollback();
                throw e;
            }
        }
    }
    
    // [LỊCH SỬ] Xem danh sách giao dịch của một tài khoản cụ thể
    public List<Transaction> viewTransactionHistory(int accountID) throws SQLException{
        return t.getTransactionsByAccount(accountID);
    }
    
    // [HELPER] Hàm phụ trợ để kiểm tra nhanh trạng thái tài khoản và PIN
    private void checkAccount(Account acc, String pin){
        if(acc == null){
            throw new IllegalArgumentException("Account does not exist.");
        }
        if("LOCKED".equalsIgnoreCase(acc.getAccountStatus())){
            throw new IllegalArgumentException("Account has been locked.");
        }
        if(!acc.getPinSmart().equals(pin)){
            throw new IllegalArgumentException("Incorrect PIN.");
        }
    }
}