package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MessageDAO {
    public Message createMessage(int postedBy, String messageText, long timePostedEpoch)  {
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        try  {
            Connection connection = ConnectionUtil.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, postedBy);
            stmt.setString(2, messageText);
            stmt.setLong(3, timePostedEpoch);
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Message(id, postedBy, messageText, timePostedEpoch);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message";
        try  {
            Connection connection = ConnectionUtil.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        catch(Exception e) {e.printStackTrace();}
        return messages;
    }

    public Message getMessageById(int messageId)  {
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        try  {Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, messageId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch"));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public Message deleteMessage(int messageId)  {
        String sqlSelect = "SELECT * FROM Message WHERE message_id = ?";
        String sqlDelete = "DELETE FROM Message WHERE message_id = ?";
        try  {
            Connection connection = ConnectionUtil.getConnection();
                PreparedStatement stmtSelect = connection.prepareStatement(sqlSelect);
                PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete);

            stmtSelect.setInt(1, messageId);
            ResultSet rs = stmtSelect.executeQuery();



            if (rs.next()) {
                Message message = new Message(
                        rs.getInt("message_id"),
                        rs.getInt("posted_by"),
                        rs.getString("message_text"),
                        rs.getLong("time_posted_epoch")
                        );
                stmtDelete.setInt(1, messageId);
                stmtDelete.executeUpdate();
                return message;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public Message updateMessage(int message_id, String message_text){
        String updateSql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        String selectSql = "SELECT message_id, posted_by, message_text, time_posted_epoch FROM Message WHERE message_id = ?";

        try {Connection connection = ConnectionUtil.getConnection();
                PreparedStatement updateStmt = connection.prepareStatement(updateSql);
                PreparedStatement selectStmt = connection.prepareStatement(selectSql);

            updateStmt.setString(1, message_text);
            updateStmt.setInt(2, message_id);
            int rowsAffected = updateStmt.executeUpdate();

            if (rowsAffected > 0) {
                selectStmt.setInt(1, message_id);
                try (ResultSet rs = selectStmt.executeQuery()) {
                    if (rs.next()) {
                        return new Message(
                                rs.getInt("message_id"),
                                rs.getInt("posted_by"),
                                rs.getString("message_text"),
                                rs.getLong("time_posted_epoch"));
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public List<Message> getMessagesByUserId(int userId) {
        List<Message> messages = new ArrayList<>();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        try  {
            Connection connection = ConnectionUtil.getConnection();
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                messages.add(new Message(rs.getInt("message_id"), rs.getInt("posted_by"), rs.getString("message_text"), rs.getLong("time_posted_epoch")));
            }
        }
        catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return messages;
    }
}
