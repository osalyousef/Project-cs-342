package auth;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import db.DBConnection;

public class ReportsPanel extends JPanel {

    public ReportsPanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_COLOR);

        JLabel title = new JLabel("System Reports & Analytics", SwingConstants.CENTER);
        title.setFont(StyleUtils.FONT_HEADER);
        title.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        JTabbedPane tabs = new JTabbedPane();
        
        tabs.addTab("Registration Stats", createTablePanel(getRegistrationStatsQuery(), new String[]{"Event Name", "Total Attendees"}));
        tabs.addTab("Top Categories", createTablePanel(getTopCategoriesQuery(), new String[]{"Category", "Event Count", "Total Registrations"}));
        tabs.addTab("Capacity Utilization", createTablePanel(getCapacityQuery(), new String[]{"Event", "Total Seats", "Booked", "Utilization %"}));

        add(tabs, BorderLayout.CENTER);
    }

    // method to create table panel
    private JPanel createTablePanel(String sql, String[] headers) {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel model = new DefaultTableModel(headers, 0);
        JTable table = new JTable(model);
        StyleUtils.styleTable(table);

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Object[] row = new Object[headers.length];
                for (int i = 0; i < headers.length; i++) {
                    row[i] = rs.getObject(i + 1);
                }
                model.addRow(row);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        panel.add(new JScrollPane(table), BorderLayout.CENTER);
        return panel;
    }

    // sql queries for reports
    private String getRegistrationStatsQuery() {
        return "SELECT e.event_name, COUNT(r.registration_id) as total " +
               "FROM events e LEFT JOIN registrations r ON e.event_id = r.event_id " +
               "GROUP BY e.event_id ORDER BY total DESC";
    }

    private String getTopCategoriesQuery() {
        return "SELECT c.category_name, COUNT(DISTINCT e.event_id) as events_count, COUNT(r.registration_id) as total_regs " +
               "FROM categories c " +
               "LEFT JOIN events e ON c.category_id = e.category_id " +
               "LEFT JOIN registrations r ON e.event_id = r.event_id " +
               "GROUP BY c.category_id ORDER BY total_regs DESC";
    }

    private String getCapacityQuery() {
        return "SELECT e.event_name, e.total_seats, COUNT(r.registration_id) as booked, " +
               "CONCAT(ROUND((COUNT(r.registration_id) / e.total_seats * 100), 1), '%') as util " +
               "FROM events e LEFT JOIN registrations r ON e.event_id = r.event_id " +
               "GROUP BY e.event_id";
    }
}