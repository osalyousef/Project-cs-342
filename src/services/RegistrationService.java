package services;

import db.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;
import java.util.UUID;

public class RegistrationService {

    // inner class to represent registered event data
    public static class RegisteredEvent {
        private int eventId;
        private String eventName;
        private String location;
        private String date;

        public RegisteredEvent(int eventId, String eventName, String location, String date) {
            this.eventId = eventId;
            this.eventName = eventName;
            this.location = location;
            this.date = date;
        }

        public int getEventId() { return eventId; }
        public String getEventName() { return eventName; }
        public String getLocation() { return location; }
        public String getFormattedDate() { return date; }
    }

    // get registered events for specific user
    public static List<RegisteredEvent> getRegisteredEvents(int userId) {
        List<RegisteredEvent> list = new ArrayList<>();
        String sql = "SELECT e.event_id, e.event_name, e.location, e.date FROM events e JOIN registrations r ON e.event_id = r.event_id WHERE r.attendee_id = ?";

        
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new RegisteredEvent(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // method to cancel registration
    public static boolean cancelRegistration(int userId, int eventId) {
        String sql = "DELETE FROM registrations WHERE event_id = ? AND attendee_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, eventId);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // method to register attendee
  public static String registerAttendee(int userId, int eventId, String userName) {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            
            // get current timestamp
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // check if user is already registered
            String checkUserSql = "SELECT COUNT(*) FROM registrations WHERE event_id = ? AND attendee_id = ?";
            try (PreparedStatement checkUserStmt = conn.prepareStatement(checkUserSql)) {
                checkUserStmt.setInt(1, eventId);
                checkUserStmt.setInt(2, userId);
                ResultSet rsUser = checkUserStmt.executeQuery();
                if (rsUser.next() && rsUser.getInt(1) > 0) {
                    return "Already registered";
                }
            }

            // check event capacity
            String eventName = "";
            String checkCapacitySql = "SELECT e.event_name, e.total_seats, (SELECT COUNT(*) FROM registrations WHERE event_id = ?) as booked_count FROM events e WHERE e.event_id = ?";
            try (PreparedStatement capStmt = conn.prepareStatement(checkCapacitySql)) {
                capStmt.setInt(1, eventId);
                capStmt.setInt(2, eventId);
                ResultSet rsCap = capStmt.executeQuery();
                
                if (rsCap.next()) {
                    eventName = rsCap.getString("event_name");
                    int totalSeats = rsCap.getInt("total_seats");
                    int bookedCount = rsCap.getInt("booked_count");
                    
                    if (bookedCount >= totalSeats) {
                        return "Full";
                    }
                }
            }

            // insert registration record
            String ticketCode = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            String insertSql = "INSERT INTO registrations (event_id, attendee_id, registration_time, confirmation_code, status, created_at) VALUES (?, ?, ?, ?, 'confirmed', ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setInt(1, eventId);
                insertStmt.setInt(2, userId);
                insertStmt.setTimestamp(3, now); 
                insertStmt.setString(4, ticketCode);
                insertStmt.setTimestamp(5, now);
                insertStmt.executeUpdate();
            }

            // insert notification record
            String notifSql = "INSERT INTO notifications (user_id, event_id, message, sent_time, notification_type, created_at) VALUES (?, ?, ?, ?, 'register', ?)";
            try (PreparedStatement notifStmt = conn.prepareStatement(notifSql)) {
                String message = "Hello \"" + userName + "\" You have successfully registered for \"" + eventName + "\". We're excited to see you there!";
                
                notifStmt.setInt(1, userId);
                notifStmt.setInt(2, eventId);
                notifStmt.setString(3, message);
                notifStmt.setTimestamp(4, now);
                notifStmt.setTimestamp(5, now);
                notifStmt.executeUpdate();
            }

            return "Success:" + ticketCode;

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error";
        }
    }
}