package services;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import db.DBConnection;
import auth.Session;
import javax.swing.JOptionPane;
import models.Event;

public class EventService {
    
    // get all events
    public static ArrayList<Event> getAllEvents() {
        ArrayList<Event> events = new ArrayList<>();
        String sql = "SELECT e.event_name, e.event_id, e.location, e.date, c.category_name, e.total_seats FROM events e JOIN categories c ON e.category_id = c.category_id";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                events.add(new Event(rs.getInt("event_id"), rs.getString("event_name"), rs.getString("location"), rs.getString("date"), rs.getString("category_name"), rs.getInt("total_seats")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return events;
    }

    // register user logic
    public static void registerUserForEvent(int userId, int eventId, String userName) {
        String result = RegistrationService.registerAttendee(userId, eventId, userName);
        
        if (result.equals("Full")) {
            JOptionPane.showMessageDialog(null, "Sorry, this event is fully booked!", "Event Full", JOptionPane.ERROR_MESSAGE);
        } else if (result.equals("Already registered")) {
            JOptionPane.showMessageDialog(null, "You are already registered for this event.", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else if (result.startsWith("Success")) {
            String code = result.split(":")[1]; 
            String message = "Booking Confirmed!\n\n" +
                             "Your Ticket Code: " + code + "\n" +
                             "Check your 'Tickets' tab for details.";
            JOptionPane.showMessageDialog(null, message, "Registration Successful", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "An error occurred.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // create event 
    public static boolean createEvent(String name, String location, Timestamp dateTime, int totalSeats, String categoryName) {
        String sql = "INSERT INTO events (event_name, location, date, description, total_seats, availablity, organizer_id, category_id, created_at, updated_at) " +
                     "VALUES (?, ?, ?, '', ?, true, ?, (SELECT category_id FROM categories WHERE category_name = ? LIMIT 1), NOW(), NOW())";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, location);
            stmt.setTimestamp(3, dateTime);
            stmt.setInt(4, totalSeats);
            stmt.setInt(5, Session.getUserId());
            stmt.setString(6, categoryName); 
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // inner class for created events 
    public static class CreatedEvent {
        private int eventId; private String name; private String location; private String date; private int totalSeats; private String category;
        public CreatedEvent(int eventId, String name, String location, String date, int totalSeats, String category) {
            this.eventId = eventId; this.name = name; this.location = location; this.date = date; this.totalSeats = totalSeats; this.category = category;
        }
        public int getEventId() { return eventId; }
        public String getName() { return name; }
        public String getLocation() { return location; }
        public String getDate() { return date; }
        public int getTotalSeats() { return totalSeats; }
        public String getCategory() { return category; }
    }

    // get events created by organizer 
    public static List<CreatedEvent> getCreatedEvents(int organizerId) {
        List<CreatedEvent> list = new ArrayList<>();
        
        String sql = "SELECT e.event_id, e.event_name, e.location, e.date, e.total_seats, c.category_name " +
                     "FROM events e LEFT JOIN categories c ON e.category_id = c.category_id " +
                     "WHERE e.organizer_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, organizerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) list.add(new CreatedEvent(
                rs.getInt("event_id"), 
                rs.getString("event_name"), 
                rs.getString("location"), 
                rs.getString("date"), 
                rs.getInt("total_seats"),
                rs.getString("category_name")
            ));
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // delete event
    public static boolean deleteEvent(int eventId) {
         String deleteRegistrations = "DELETE FROM registrations WHERE event_id = ?";
         String deleteNotifications = "DELETE FROM notifications WHERE event_id = ?";
         String deleteEvent = "DELETE FROM events WHERE event_id = ?";
         try (Connection conn = DBConnection.getConnection()) {
             conn.setAutoCommit(false);
             try (PreparedStatement s1 = conn.prepareStatement(deleteRegistrations); PreparedStatement s2 = conn.prepareStatement(deleteNotifications); PreparedStatement s3 = conn.prepareStatement(deleteEvent)) {
                 s1.setInt(1, eventId); s1.executeUpdate();
                 s2.setInt(1, eventId); s2.executeUpdate();
                 s3.setInt(1, eventId); int rows = s3.executeUpdate();
                 conn.commit(); return rows > 0;
             } catch (SQLException e) { conn.rollback(); return false; }
         } catch (SQLException e) { return false; }
    }

    // update event 
    public static boolean updateEvent(int eventId, String name, String location, Timestamp dateTime, int totalSeats, String categoryName) {
        String sql = "UPDATE events SET event_name = ?, location = ?, date = ?, total_seats = ?, category_id = (SELECT category_id FROM categories WHERE category_name = ? LIMIT 1), updated_at = NOW() WHERE event_id = ?";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name); 
            stmt.setString(2, location); 
            stmt.setTimestamp(3, dateTime); 
            stmt.setInt(4, totalSeats); 
            stmt.setString(5, categoryName);
            stmt.setInt(6, eventId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }

    // get categories
    public static String[] getAllCategories() {
        List<String> categories = new ArrayList<>();
        String sql = "SELECT category_name FROM categories";
        try (Connection conn = DBConnection.getConnection(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) categories.add(rs.getString("category_name"));
        } catch (SQLException e) { e.printStackTrace(); }
        return categories.toArray(new String[0]);
    }

    // get locations
    public static String[] getAllLocations() {
        List<String> locs = new ArrayList<>(); locs.add("All");
        try (Connection conn = DBConnection.getConnection(); ResultSet rs = conn.prepareStatement("SELECT DISTINCT location FROM events").executeQuery()) {
            while (rs.next()) locs.add(rs.getString("location"));
        } catch (SQLException e) {} return locs.toArray(new String[0]);
    }
    
    // get dates
    public static String[] getAllDates() {
        List<String> dates = new ArrayList<>(); dates.add("All");
        try (Connection conn = DBConnection.getConnection(); ResultSet rs = conn.prepareStatement("SELECT DISTINCT DATE(date) as d FROM events").executeQuery()) {
            while (rs.next()) dates.add(rs.getString("d"));
        } catch (SQLException e) {} return dates.toArray(new String[0]);
    }
}