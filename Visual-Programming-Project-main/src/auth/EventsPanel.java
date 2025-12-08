package auth;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.border.EmptyBorder; // FIXES ERROR
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import db.DBConnection;
import services.AdminService;

public class EventsPanel extends JPanel {
    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public EventsPanel() {
        setLayout(new BorderLayout());
        setBackground(StyleUtils.BG_COLOR);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("Events Management");
        title.setFont(StyleUtils.FONT_HEADER);
        title.setForeground(StyleUtils.PRIMARY_COLOR);
        title.setBorder(new EmptyBorder(0, 0, 20, 0));
        add(title, BorderLayout.NORTH);

        String[] cols = { "ID", "Name", "Date", "Location", "Description", "Seats", "Active", "Action" };
        tableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int row, int column) { return column == 7; }
        };

        eventsTable = new JTable(tableModel);
        StyleUtils.styleTable(eventsTable);
        
        // Column Configuration
        eventsTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        eventsTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        eventsTable.removeColumn(eventsTable.getColumnModel().getColumn(0)); // Hide ID

        loadEventsData();

        JScrollPane scroll = new JScrollPane(eventsTable);
        scroll.getViewport().setBackground(Color.WHITE);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        add(scroll, BorderLayout.CENTER);
    }

    private void loadEventsData() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT event_id, event_name, date, location, description, total_seats, availablity FROM events";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            while (rs.next()) {
                int id = rs.getInt("event_id");
                String name = rs.getString("event_name");
                Timestamp dateTS = rs.getTimestamp("date");
                String dateStr = (dateTS != null) ? sdf.format(dateTS) : "";
                String location = rs.getString("location");
                String description = rs.getString("description");
                int totalSeats = rs.getInt("total_seats");
                boolean available = rs.getBoolean("availablity");
                String availableStr = available ? "Yes" : "No";

                tableModel.addRow(new Object[] { id, name, dateStr, location, description, totalSeats, availableStr, "Delete" });
            }
            rs.close(); stmt.close();
        } catch (SQLException ex) { ex.printStackTrace(); }
    }

    class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(StyleUtils.DANGER_COLOR);
            setForeground(Color.WHITE);
            setText("Delete");
        }
        public Component getTableCellRendererComponent(JTable t, Object v, boolean s, boolean f, int r, int c) { return this; }
    }

    class ButtonEditor extends DefaultCellEditor {
        JButton btn;
        int selectedRow;
        public ButtonEditor(JCheckBox cb) {
            super(cb);
            btn = new JButton("Delete");
            btn.setBackground(StyleUtils.DANGER_COLOR);
            btn.setForeground(Color.WHITE);
            btn.addActionListener(e -> {
                fireEditingStopped();
                int modelRow = eventsTable.convertRowIndexToModel(selectedRow);
                int eventId = (int) tableModel.getValueAt(modelRow, 0); 
                String eventTitle = (String) tableModel.getValueAt(modelRow, 1);
                
                int confirm = JOptionPane.showConfirmDialog(null, "Delete \"" + eventTitle + "\"?", "Confirm", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    if (AdminService.deleteEventById(eventId)) {
                        tableModel.removeRow(modelRow);
                    }
                }
            });
        }
        public Component getTableCellEditorComponent(JTable t, Object v, boolean s, int r, int c) {
            selectedRow = r; return btn;
        }
        public Object getCellEditorValue() { return "Delete"; }
    }
}