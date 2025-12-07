package auth;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;

import db.DBConnection;
import services.AdminService;

public class EventsPanel extends JPanel {

    private JTable eventsTable;
    private DefaultTableModel tableModel;

    public EventsPanel() {
        // Set a preferred size so the panel is big enough for all columns
        setPreferredSize(new Dimension(1200, 700));
        setLayout(new BorderLayout());

        // Title label at the top
        JLabel titleLabel = new JLabel("Events Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        titleLabel.setBackground(Color.GRAY);
        add(titleLabel, BorderLayout.NORTH);

        String[] columnNames = { "ID", "Name", "Date", "Location", "Description", "Total Seats", "Availability",
                "Action" };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // only "Action" column editable (button)
            }
        };

        // Create the table and apply the model
        eventsTable = new JTable(tableModel);
        eventsTable.setRowHeight(60); // Increase row height for multi-line description
        eventsTable.setFont(new Font("Arial", Font.PLAIN, 14));
        eventsTable.setFillsViewportHeight(true);

        // Automatically resize columns to fit the available width (no horizontal
        // scroll)
        eventsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

        // Customize table header
        JTableHeader header = eventsTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 100, 100));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Center-align non-description columns
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        // Apply renderer to every column except "Description" (index 3)
        for (int i = 0; i < eventsTable.getColumnCount(); i++) {
            if (i != 3) {
                eventsTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Use a custom renderer for the "Description" column to allow multi-line text
        eventsTable.getColumnModel().getColumn(3).setCellRenderer(new TextAreaRenderer());

        eventsTable.removeColumn(eventsTable.getColumnModel().getColumn(0)); // hides event_id

        eventsTable.getColumn("Action").setCellRenderer(new ButtonRenderer());
        eventsTable.getColumn("Action").setCellEditor(new DefaultCellEditor(new JCheckBox()) {
            JButton button = new JButton("Delete");
            int selectedRow;

            {
                button.setForeground(Color.RED);
                button.setFocusPainted(false);
                button.addActionListener(e -> {
                    int modelRow = eventsTable.convertRowIndexToModel(selectedRow);
                    int eventId = (int) tableModel.getValueAt(modelRow, 0); // hidden ID
                    String eventTitle = (String) tableModel.getValueAt(modelRow, 1); // column 1 is Name

                    int confirm = JOptionPane.showConfirmDialog(null,
                            "Are you sure you want to delete the event \"" + eventTitle + "\"?",
                            "Confirm Deletion", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        boolean deleted = AdminService.deleteEventById(eventId);
                        if (deleted) {
                            tableModel.removeRow(modelRow);
                            JOptionPane.showMessageDialog(null, "Event deleted.");
                        } else {
                            JOptionPane.showMessageDialog(null, "Failed to delete event.");
                        }
                    }
                });
            }

            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row,
                    int column) {
                selectedRow = row;
                return button;
            }

            public Object getCellEditorValue() {
                return "Delete";
            }
        });

        // Populate the table with data from the database
        loadEventsData();

        // Put the table in a scroll pane with custom border
        JScrollPane scrollPane = new JScrollPane(eventsTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }

    private void loadEventsData() {
        try (Connection conn = DBConnection.getConnection()) {
            // String sql = "SELECT event_name, date, location, description, total_seats,
            // availablity FROM events";
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

                tableModel.addRow(new Object[] {
                        id, name, dateStr, location, description, totalSeats, availableStr, "Delete"
                });
            }

            // eventsTable.removeColumn(eventsTable.getColumnModel().getColumn(0)); // hides
            // event_id

            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading events from database:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Custom renderer for multi-line text in the Description column.
     */
    private static class TextAreaRenderer extends JTextArea implements TableCellRenderer {
        public TextAreaRenderer() {
            setLineWrap(true);
            setWrapStyleWord(true);
            setOpaque(true);
            setFont(new Font("Arial", Font.PLAIN, 14));
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {

            setText(value != null ? value.toString() : "");
            setSize(table.getColumnModel().getColumn(column).getWidth(), getPreferredSize().height);

            // Apply selection colors if needed
            if (isSelected) {
                setBackground(table.getSelectionBackground());
                setForeground(table.getSelectionForeground());
            } else {
                setBackground(table.getBackground());
                setForeground(table.getForeground());
            }

            return this;
        }
    }

    private static class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setForeground(Color.RED);
            setBackground(Color.WHITE);
            setFont(new Font("Arial", Font.BOLD, 12));
        }

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            setText(value == null ? "Delete" : value.toString());
            return this;
        }
    }

}
