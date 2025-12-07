package services;

import db.DBConnection;
import models.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import exceptions.MessageBox;

public class NotificationService {
    public static List<Notification> getNotificationsForUser(int userId) {
        List<Notification> list = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY sent_time DESC";
        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
             
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
    
            while (rs.next()) {
                Notification n = new Notification(
                    rs.getInt("notification_id"),
                    rs.getInt("user_id"),
                    rs.getInt("admin_id"),
                    rs.getInt("event_id"),
                    rs.getString("message"),
                    rs.getString("notification_type"),
                    rs.getTimestamp("sent_time"),
                    rs.getBoolean("read_status"),
                    rs.getTimestamp("created_at"),
                    rs.getTimestamp("updated_at")
                );
                list.add(n);
            }
        } catch (SQLException e) {
            MessageBox.showError("Error fetching notifications: " + e.getMessage());
        }
        return list;
    }
    
}
