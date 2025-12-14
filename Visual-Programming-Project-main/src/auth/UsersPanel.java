package auth;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import db.DBConnection;
import javax.swing.table.TableCellRenderer;
public class UsersPanel extends JPanel {
    private JLabel titleLabel;
    private JTable usersTable;
    private DefaultTableModel tableModel;
    
    public UsersPanel() {
        setLayout(new BorderLayout());
        
        
        // Title label at top
        titleLabel = new JLabel("Users", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // Define columns
        String[] columnNames = {"User ID", "Username", "Email", "Role", "Gender", "Created At", "Updated At", "Action"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 7; // Make cells non-editable
            }
        };
        
        usersTable = new JTable(tableModel);
        usersTable.setRowHeight(30);
        usersTable.setFont(new Font("Arial", Font.PLAIN, 14));
        usersTable.setFillsViewportHeight(true);
        usersTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        
        // Customize header
        JTableHeader header = usersTable.getTableHeader();
        header.setFont(new Font("Arial", Font.BOLD, 14));
        header.setBackground(new Color(100, 100, 100));
        header.setForeground(Color.WHITE);
        header.setPreferredSize(new Dimension(header.getWidth(), 35));
        
        
        usersTable.getColumnModel().getColumn(7).setCellRenderer(new ButtonRenderer());
        usersTable.getColumnModel().getColumn(7).setCellEditor(new ButtonEditor(new JCheckBox()));
        
        // Hide User ID column
        usersTable.removeColumn(usersTable.getColumnModel().getColumn(0));
        // Load users data from the database
        loadUsersData();
        
        // Put the table in a scroll pane
        JScrollPane scrollPane = new JScrollPane(usersTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(scrollPane, BorderLayout.CENTER);
    }
    
    private void loadUsersData() {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT user_id, username, email, role, gender, created_at, updated_at FROM users";
            PreparedStatement stmt = conn.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            
            while (rs.next()) {
                int userId = rs.getInt("user_id");
                String username = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");
                String gender = rs.getString("gender");
                Timestamp createdTS = rs.getTimestamp("created_at");
                String createdAt = (createdTS != null) ? sdf.format(createdTS) : "";
                Timestamp updatedTS = rs.getTimestamp("updated_at");
                String updatedAt = (updatedTS != null) ? sdf.format(updatedTS) : "";
                
                tableModel.addRow(new Object[]{userId, username, email, role, gender, createdAt, updatedAt, "Change Role"});
                }
            
            rs.close();
            stmt.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error loading users from database:\n" + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
        }
    }
        private boolean updateUserRole(int userId, String newRole) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "UPDATE users SET role = ?, updated_at = NOW() WHERE user_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, newRole);
            stmt.setInt(2, userId);
            int rows = stmt.executeUpdate();
            stmt.close();
            return rows > 0;
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this,
                    "Error updating role: " + ex.getMessage(),
                    "Database Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
        
       class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
            setBackground(StyleUtils.ACCENT_COLOR);
            setForeground(Color.WHITE);
            setFont(StyleUtils.FONT_BOLD);
            setText("Change Role");
            setFocusPainted(false);
            setBorderPainted(false);
        }
        
        public Component getTableCellRendererComponent(JTable table, Object value,
                boolean isSelected, boolean hasFocus, int row, int column) {
            return this;
        }
    }
    
    // Button Editor
    class ButtonEditor extends DefaultCellEditor {
        private JButton button;
        private int selectedRow;
        
        public ButtonEditor(JCheckBox checkBox) {
            super(checkBox);
            button = new JButton("Change Role");
            button.setBackground(StyleUtils.ACCENT_COLOR);
            button.setForeground(Color.WHITE);
            button.setFont(StyleUtils.FONT_BOLD);
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            
            button.addActionListener(e -> {
                fireEditingStopped();
                
                int modelRow = usersTable.convertRowIndexToModel(selectedRow);
                int userId = (int) tableModel.getValueAt(modelRow, 0);
                String username = (String) tableModel.getValueAt(modelRow, 1);
                String currentRole = (String) tableModel.getValueAt(modelRow, 3);
                
                String[] roles = {"user", "organizer", "attendee"};
                String newRole = (String) JOptionPane.showInputDialog(
                    null,
                    "Select new role for " + username + ":",
                    "Change User Role",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    roles,
                    currentRole
                );
                
                if (newRole != null && !newRole.equals(currentRole)) {
                    int confirm = JOptionPane.showConfirmDialog(
                        null,
                        "Change role of '" + username + "' from '" + currentRole + "' to '" + newRole + "'?",
                        "Confirm Role Change",
                        JOptionPane.YES_NO_OPTION
                    );
                    
                    if (confirm == JOptionPane.YES_OPTION) {
                        if (updateUserRole(userId, newRole)) {
                            tableModel.setValueAt(newRole, modelRow, 3);
                            JOptionPane.showMessageDialog(null, 
                                "Role updated successfully!", 
                                "Success", 
                                JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                }
            });
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                boolean isSelected, int row, int column) {
            selectedRow = row;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            return "Change Role";
        }
    }
}
