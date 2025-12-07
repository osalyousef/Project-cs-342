package services;

import db.DBConnection;
import services.EventService.CreatedEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    public static int getUsersCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getEventsCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM events")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getRegistrationsCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM registrations")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getCategoriesCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM categories")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getOrganizersCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'organizer'")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static int getAttendeesCount() {
        int count = 0;
        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE role = 'attendee'")) {
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                count = rs.getInt(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return count;
    }

    public static List<CreatedEvent> getAllCreatedEvents() {
        List<CreatedEvent> list = new ArrayList<>();
        String sql = "SELECT event_id, event_name, location, date, total_seats FROM events";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                list.add(new CreatedEvent(
                        rs.getInt("event_id"),
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date"),
                        rs.getInt("total_seats")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public static boolean deleteEventById(int eventId) {
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

}
