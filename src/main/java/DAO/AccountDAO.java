package DAO;

import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;





public class AccountDAO {
    public Account createAccount(String username, String password) {
        String sql = "INSERT INTO account (username, password) VALUES (?, ?)";
        try  {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Account(id, username, password);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
                 }
        return null;
    }

    public Account getAccountByUsername(String username)  {
        String sql = "SELECT * FROM account WHERE username = ?";
        try  {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }
        catch(Exception e){
e.printStackTrace();        }
        return null;
    }

    public Account getAccountByUsernameAndPassword(String username, String password)  {
        String sql = "SELECT * FROM account WHERE username = ? AND password = ?";
        try  {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
            }
        }
        catch(Exception e){
e.printStackTrace();        }
        return null;
    }
}
