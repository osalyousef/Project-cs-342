package auth;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import db.DBConnection;

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
        String[] columnNames = {"User ID", "Username", "Email", "Role", "Gender", "Created At", "Updated At"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make cells non-editable
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
                
                tableModel.addRow(new Object[]{userId, username, email, role, gender, createdAt, updatedAt});
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
}
