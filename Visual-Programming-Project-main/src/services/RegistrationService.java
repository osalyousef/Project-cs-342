package services;

import db.DBConnection;
import java.util.ArrayList;
import java.util.List;
import java.sql.*;

public class RegistrationService {

    public static class RegisteredEvent {
        private String eventName;
        private String location;
        private String date;

        public RegisteredEvent(String eventName, String location, String date) {
            this.eventName = eventName;
            this.location = location;
            this.date = date;
        }

        public String getEventName() {
            return eventName;
        }

        public String getLocation() {
            return location;
        }

        public String getFormattedDate() {
            return date;
        }
    }

    public static List<RegisteredEvent> getRegisteredEvents(int userId) {
        List<RegisteredEvent> list = new ArrayList<>();
        String sql = "SELECT e.event_name, e.location, e.date FROM events e JOIN registrations r ON e.event_id = r.event_id WHERE r.attendee_id = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                list.add(new RegisteredEvent(
                        rs.getString("event_name"),
                        rs.getString("location"),
                        rs.getString("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
