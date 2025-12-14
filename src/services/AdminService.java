package services;

import db.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import services.EventService.CreatedEvent;

public class AdminService {

    // get total users count
    public static int getUsersCount() {
        return getCount("SELECT COUNT(*) FROM users");
    }

    // get total events count
    public static int getEventsCount() {
        return getCount("SELECT COUNT(*) FROM events");
    }

    // get total registrations count
    public static int getRegistrationsCount() {
        return getCount("SELECT COUNT(*) FROM registrations");
    }

    // method for counts
    private static int getCount(String sql) {
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // get all events for admin
    public static List<CreatedEvent> getAllEvents() {
        List<CreatedEvent> list = new ArrayList<>();
        
        String sql = "SELECT e.event_id, e.event_name, e.location, e.date, e.total_seats, c.category_name " +
                     "FROM events e LEFT JOIN categories c ON e.category_id = c.category_id";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new CreatedEvent(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getInt("total_seats"),
                        rs.getString("category_name") 
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteEventById(int eventId) {
        return EventService.deleteEvent(eventId);
    }
}