package auth;

import javax.swing.*;
import javax.swing.border.EmptyBorder; // FIXES ERROR
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class StyleUtils {
    // --- COLOR PALETTE ---
    public static final Color PRIMARY_COLOR = new Color(33, 41, 54);     // Dark Sidebar
    public static final Color ACCENT_COLOR = new Color(67, 97, 238);     // Blue Buttons
    public static final Color BG_COLOR = new Color(245, 247, 250);       // Light Grey Background
    public static final Color WHITE = Color.WHITE;
    public static final Color TEXT_DARK = new Color(43, 43, 43);
    public static final Color TEXT_LIGHT = new Color(100, 116, 139);
    public static final Color DANGER_COLOR = new Color(239, 68, 68);     // Red

    // --- FONTS ---
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_REGULAR = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 12); // FIXES ERROR

    // --- STYLING METHODS ---
    public static void styleButton(JButton btn) {
        btn.setBackground(ACCENT_COLOR);
        btn.setForeground(WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 20, 10, 20));
    }
    
    public static void styleTextField(JTextField tf) {
        tf.setFont(FONT_REGULAR);
        tf.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
    }

    public static void styleTable(JTable table) {
        table.setRowHeight(40);
        table.setFont(FONT_REGULAR);
        table.setShowVerticalLines(false);
        table.setSelectionBackground(new Color(220, 230, 255));
        table.setSelectionForeground(TEXT_DARK);
        
        table.getTableHeader().setFont(FONT_BOLD);
        table.getTableHeader().setBackground(WHITE);
        table.getTableHeader().setBorder(BorderFactory.createMatteBorder(0,0,2,0, new Color(200,200,200)));
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(230,230,230), 1),
            new EmptyBorder(15, 15, 15, 15)
        ));
        return card;
    }
}