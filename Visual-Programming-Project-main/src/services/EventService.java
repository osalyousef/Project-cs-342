package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import models.Event;
import db.DBConnection;

import exceptions.MessageBox;
import auth.Session;

public class EventService {
    public static ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        // String sql = "SELECT e.event_name, e.event_id, e.location, e.date,
        // c.category_name " +
        // "FROM events e JOIN categories c ON e.category_id = c.category_id";
        String sql = "SELECT e.event_name, e.event_id, e.location, e.date, c.category_name, e.total_seats " +
                "FROM events e JOIN categories c ON e.category_id = c.category_id";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Event e = new Event(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getString("category_name"),
                        rs.getInt("total_seats"));
                events.add(e);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return events;
    }

    public static void registerUserForEvent(int userId, int eventId, String userName) {
        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement regStmt = null;
        PreparedStatement notifStmt = null;

        try {
            conn = DBConnection.getConnection();
            Timestamp now = new Timestamp(System.currentTimeMillis());

            // 1. Check if the user is already registered
            String checkSql = "SELECT COUNT(*) FROM registrations WHERE attendee_id = ? AND event_id = ?";
            checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, eventId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "You are already registered for this event.");
                return; // 🚫 Stop the registration
            }

            // 2. Insert into registrations
            String regSql = "INSERT INTO registrations (event_id, attendee_id, registration_time, status, created_at) VALUES (?, ?, ?, ?, ?)";
            regStmt = conn.prepareStatement(regSql);
            regStmt.setInt(1, eventId);
            regStmt.setInt(2, userId);
            regStmt.setTimestamp(3, now);
            regStmt.setString(4, "pending");
            regStmt.setTimestamp(5, now);
            regStmt.executeUpdate();

            // 3. Insert into notifications
            String eventName = "";
            String eventQuery = "SELECT event_name FROM events WHERE event_id = ?";
            try (PreparedStatement eventStmt = conn.prepareStatement(eventQuery)) {
                eventStmt.setInt(1, eventId);
                ResultSet eventRs = eventStmt.executeQuery();
                if (eventRs.next()) {
                    eventName = eventRs.getString("event_name");
                }
            }

            String notifSql = "INSERT INTO notifications (user_id, event_id, message, sent_time, created_at, notification_type) VALUES (?, ?, ?, ?, ?, ?)";
            String message = "Hello  \"" + userName + "\" You have successfully registered for \"" + eventName + "\". We're excited to see you there!";
            notifStmt = conn.prepareStatement(notifSql);
            notifStmt.setInt(1, userId);
            notifStmt.setInt(2, eventId);
            // notifStmt.setString(3, eventName + " You are attending " + "  we are waiting for you.");
            notifStmt.setString(3, message);
            notifStmt.setTimestamp(4, now);
            notifStmt.setTimestamp(5, now);
            notifStmt.setString(6, "register");
            notifStmt.executeUpdate();

    
            
            // MessageBox.showSuccess(eventName + " You are attending " + "  we are waiting for you.");
            MessageBox.showSuccess(message);


    
        } catch (SQLException ex) {
            ex.printStackTrace();
            MessageBox.showError("Error saving registration: " + ex.getMessage());

        } finally {
            try {
                if (checkStmt != null)
                    checkStmt.close();
                if (regStmt != null)
                    regStmt.close();
                if (notifStmt != null)
                    notifStmt.close();
                if (conn != null)
                    conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static boolean createEvent(String name, String location, Timestamp dateTime, int totalSeats) {
        String sql = "INSERT INTO events (event_name, location, date, description, total_seats, availablity, organizer_id, category_id, created_at, updated_at) VALUES (?, ?, ?, '', ?, true, ?, 1, NOW(), NOW())";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setTimestamp(3, dateTime);
            stmt.setInt(4, totalSeats);
            stmt.setInt(5, Session.getUserId());
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static class CreatedEvent {
        private int eventId;
        private String name;
        private String location;
        private String date;
        private int totalSeats;

        public CreatedEvent(int eventId, String name, String location, String date, int totalSeats) {
            this.eventId = eventId;
            this.name = name;
            this.location = location;
            this.date = date;
            this.totalSeats = totalSeats;
        }

        public CreatedEvent(int eventId, String name, String location, String date) {
            this.eventId = eventId;
            this.name = name;
            this.location = location;
            this.date = date;
        }

        public int getEventId() {
            return eventId;
        }

        public String getName() {
            return name;
        }

        public String getLocation() {
            return location;
        }

        public String getDate() {
            return date;
        }

        public int getTotalSeats() {
            return totalSeats;
        }
    }

    public static List<CreatedEvent> getCreatedEvents(int organizerId) {
        List<CreatedEvent> list = new ArrayList<>();
        String sql = "SELECT event_id, event_name, location, date ,total_seats FROM events WHERE organizer_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new CreatedEvent(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getInt("total_seats")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteEvent(int eventId) {
        String deleteRegistrations = "DELETE FROM registrations WHERE event_id = ?";
        String deleteNotifications = "DELETE FROM notifications WHERE event_id = ?";
        String deleteEvent = "DELETE FROM events WHERE event_id = ?";

        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt1 = conn.prepareStatement(deleteRegistrations);
                    PreparedStatement stmt2 = conn.prepareStatement(deleteNotifications);
                    PreparedStatement stmt3 = conn.prepareStatement(deleteEvent)) {

                stmt1.setInt(1, eventId);
                stmt1.executeUpdate();

                stmt2.setInt(1, eventId);
                stmt2.executeUpdate();

                stmt3.setInt(1, eventId);
                int rows = stmt3.executeUpdate();

                conn.commit();
                return rows > 0;
            } catch (SQLException e) {
                conn.rollback();
                e.printStackTrace();
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateEvent(int eventId, String name, String location, Timestamp dateTime, int totalSeats) {
        String sql = "UPDATE events SET event_name = ?, location = ?, date = ?, total_seats = ?, updated_at = NOW() WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setTimestamp(3, dateTime);
            stmt.setInt(4, totalSeats);
            stmt.setInt(5, eventId);

            int rows = stmt.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public static String[] getAllCategories() {
        List<String> categories = new ArrayList<>();
        categories.add("All");
        String sql = "SELECT category_name FROM categories";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                categories.add(rs.getString("category_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categories.toArray(new String[0]);
    }
    
    public static String[] getAllLocations() {
        List<String> locations = new ArrayList<>();
        locations.add("All");
        String sql = "SELECT DISTINCT location FROM events";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locations.add(rs.getString("location"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations.toArray(new String[0]);
    }
    
    public static String[] getAllDates() {
        List<String> dates = new ArrayList<>();
        dates.add("All");
        String sql = "SELECT DISTINCT DATE(date) as event_date FROM events ORDER BY event_date";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                dates.add(rs.getString("event_date"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return dates.toArray(new String[0]);
    }
    
}
