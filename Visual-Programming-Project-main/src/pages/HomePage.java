package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
// import models.Event;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
// import java.text.SimpleDateFormat;
// import java.util.Date;
// import java.util.Random;
import java.util.ArrayList;
import java.util.List;

import auth.Events;
import auth.EventsDataBase;
// import db.DBConnection;
// import sectiomns
import sections.SideMenuPanel;
import sections.HomeContentPanel;
import sections.ProfilePanel;
import sections.TicketsPanel;
import services.EventService;
import sections.EventManagementPanel;
// import sections.ProfilePanel;
import sections.NotificationPanel;

// import session
import auth.Session;
import db.DBConnection;
// services
import services.EventService;

public class HomePage extends JFrame implements ActionListener {
    // Menu and main components
    private JButton Home_Button, Tickets_Button, Event_Management_Button, Profile_Button, Notification_Button,
            Text_Search_Button; // menu
    private JButton Create_Event_Button, Combo_Search_Button;
    private JPanel menuPanel, Tickets_Panel, contentPanel, Event_Management_Panel, Profile_Panel, Notification_Panel,
            mainPanel, eventsPanel;
    private JComboBox Date, Category, Location;
    private JLabel Password_Label, User_Name_Label, Email_Label, Title_Label, Tickets_Label, Event_Management_Label,
            Reg_Events_Label, Your_Events_Label, Profile_Label, Notification_Label;
    private JTextField Search_Field;

    HomeContentPanel content;

    int simpleEventY = 285; // where to place next event label

    // get all user data from seission and assign them to the variables
    // private int userId = Session.getUserId();
    private String username = Session.getUsername();
    private String email = Session.getEmail();
    // private String password = Session.getPassword();
    // private String role = Session.getRole();

    // private static final String[] DATES = { "date" };
    // private static String[] getCategoriesFromDB() {
    // List<String> categories = new ArrayList<>();
    // categories.add("All"); // Default option

    // try (Connection conn = DBConnection.getConnection();
    // PreparedStatement stmt = conn.prepareStatement("SELECT category_name FROM
    // categories");
    // ResultSet rs = stmt.executeQuery()) {

    // while (rs.next()) {
    // categories.add(rs.getString("category_name"));
    // }
    // } catch (Exception e) {
    // e.printStackTrace();
    // }

    // return categories.toArray(new String[0]);
    // }
    // private static final String[] LOCATIONS = { "Location" };
    private static final String[] DATES = EventService.getAllDates();
    private static final String[] CATEGORIES = EventService.getAllCategories();
    private static final String[] LOCATIONS = EventService.getAllLocations();

    public HomePage() {

        setTitle("Event Management - Home Page");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Component Definition
        Home_Button = new JButton("Home");
        Tickets_Button = new JButton("Tickets");
        Event_Management_Button = new JButton("Event Management");
        Profile_Button = new JButton("Profile");
        Create_Event_Button = new JButton("Create");
        Notification_Button = new JButton("Notifications");

        Text_Search_Button = new JButton("Search");
        Combo_Search_Button = new JButton("Filter");
        Search_Field = new JTextField();
        Tickets_Label = new JLabel("Tickets", SwingConstants.CENTER);
        Event_Management_Label = new JLabel("Event Management", SwingConstants.CENTER);
        Title_Label = new JLabel("Current Available Events", SwingConstants.CENTER);
        Reg_Events_Label = new JLabel("Events (registered in):");
        Your_Events_Label = new JLabel("Your events (created by you):");
        Profile_Label = new JLabel("Profile", SwingConstants.CENTER);
        Password_Label = new JLabel("Password: ********");
        Notification_Label = new JLabel("Notifications", SwingConstants.CENTER);

        // Side menu panel
        SideMenuPanel sideMenu = new SideMenuPanel(this);

        Home_Button = sideMenu.homeButton;
        Tickets_Button = sideMenu.ticketsButton;
        Event_Management_Button = sideMenu.eventMgmtButton;
        Profile_Button = sideMenu.profileButton;
        Notification_Button = sideMenu.notificationsButton;

        menuPanel = sideMenu;

        // ===== MAIN PANEL =====
        mainPanel = new JPanel(new BorderLayout());

        // === Main content panel (Home) ===
        content = new HomeContentPanel(DATES, CATEGORIES, LOCATIONS);

        contentPanel = content;
        Search_Field = content.searchField;
        // Text_Search_Button = content.textSearchButton;
        // Combo_Search_Button = content.comboSearchButton;
        Combo_Search_Button.addActionListener(e -> applyCategoryFilter());
        content.categoryBox.addActionListener(e -> applyCategoryFilter());
        content.dateBox.addActionListener(e -> applyCategoryFilter());
        content.locationBox.addActionListener(e -> applyCategoryFilter());

        Date = content.dateBox;
        Category = content.categoryBox;
        Location = content.locationBox;
        Title_Label = content.titleLabel;
        eventsPanel = content.eventsPanel;

        Search_Field.setBounds(10, 110, 200, 30);
        Search_Field.setFont(new Font("Arial", Font.PLAIN, 14));
        Search_Field.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, Color.GRAY));
        Search_Field.setBackground(Color.WHITE);
        Search_Field.setForeground(Color.DARK_GRAY);

        Text_Search_Button.setBounds(220, 110, 80, 30);
        Text_Search_Button.setBackground(Color.GRAY);
        Text_Search_Button.setForeground(Color.WHITE);
        Text_Search_Button.setFocusPainted(false);
        Text_Search_Button.setBorderPainted(false);

        Date.setBounds(320, 110, 100, 30);
        Date.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Date.setBackground(Color.WHITE);
        Date.setForeground(Color.DARK_GRAY);
        Date.setFont(new Font("Arial", Font.PLAIN, 14));

        Category.setBounds(430, 110, 100, 30);
        Category.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Category.setBackground(Color.WHITE);
        Category.setForeground(Color.DARK_GRAY);
        Category.setFont(new Font("Arial", Font.PLAIN, 14));

        Location.setBounds(540, 110, 100, 30);
        Location.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        Location.setBackground(Color.WHITE);
        Location.setForeground(Color.DARK_GRAY);
        Location.setFont(new Font("Arial", Font.PLAIN, 14));

        Combo_Search_Button.setBounds(650, 110, 80, 30);
        Combo_Search_Button.setBackground(Color.GRAY);
        Combo_Search_Button.setForeground(Color.WHITE);
        Combo_Search_Button.setFocusPainted(false);
        Combo_Search_Button.setBorderPainted(false);

        // ======= Panel contains Available events in (Home Page)========

        eventsPanel = new JPanel();
        eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        eventsPanel.setBounds(0, 140, 800, 400);
        eventsPanel.setBackground(Color.WHITE);

        // ========== Tickets Panel and ITS DESIGN ============

        TicketsPanel ticketsSection = new TicketsPanel();
        Tickets_Panel = ticketsSection;
        Tickets_Label = ticketsSection.ticketsLabel;

        // ========= Event Management Panel and ITS DESIGN ============

        EventManagementPanel eventMgmtSection = new EventManagementPanel();
        Event_Management_Panel = eventMgmtSection;
        Event_Management_Label = eventMgmtSection.titleLabel;
        Create_Event_Button = eventMgmtSection.createEventButton;
        Create_Event_Button.addActionListener(this);
        Reg_Events_Label = eventMgmtSection.registeredLabel;
        Your_Events_Label = eventMgmtSection.yourEventsLabel;

        // =========== Profile Panel and ITS DESIGN ===========

        ProfilePanel profileSection = new ProfilePanel();
        Profile_Panel = profileSection;
        Profile_Label = profileSection.titleLabel;
        User_Name_Label = profileSection.usernameLabel;
        Email_Label = profileSection.emailLabel;
        // Password_Label = profileSection.passwordLabel; profile jump issue sovled

        // ============ Notification Panel and ITS DESIGN ============

        NotificationPanel notifSection = new NotificationPanel();
        Notification_Panel = notifSection;
        Notification_Label = notifSection.titleLabel;

        // =============== menu and main JPanels ADDED TO JFRAME =========
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        add(menuPanel, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
        setVisible(true);

        // load events from database
        loadEventsFromDatabase();

        // load event in home

        for (models.Event e : EventService.getAllEvents()) {
            content.addEventCard(e.getEventId(), e.getName(), e.getLocation(), e.getDate());
        }

        content.searchField.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                content.searchEvents(content.searchField.getText());
            }

            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                content.searchEvents(content.searchField.getText());
            }

            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                content.searchEvents(content.searchField.getText());
            }
        });

    }

    public void actionPerformed(ActionEvent e) {
        mainPanel.removeAll();
        if (e.getSource() == Home_Button) {
            content.eventsPanel.removeAll();

            for (models.Event ev : EventService.getAllEvents()) {
                content.addEventCard(ev.getEventId(), ev.getName(), ev.getLocation(), ev.getDate());
            }

            content.eventsPanel.revalidate();
            content.eventsPanel.repaint();

            mainPanel.add(contentPanel, BorderLayout.CENTER);
        } else if (e.getSource() == Tickets_Button) { // ==== GOES TO TICKETS PANEL ===
            TicketsPanel newTicketsPanel = new TicketsPanel(); // create fresh instance (refresh)
            Tickets_Panel = newTicketsPanel;
            mainPanel.add(Tickets_Panel, BorderLayout.CENTER);
            // mainPanel.add(Tickets_Panel, BorderLayout.CENTER);
        } else if (e.getSource() == Event_Management_Button) { // === GOES TO EVENT MANAGEMENT PANEL ===
            mainPanel.add(Event_Management_Panel, BorderLayout.CENTER);
        } else if (e.getSource() == Profile_Button) { // === GOES TO PROFILE PANEL ===
            mainPanel.add(Profile_Panel, BorderLayout.CENTER);
            User_Name_Label.setText("User Name: " + username);
            Email_Label.setText("User Email: " + email);
            Password_Label.setText("User Password: *******");
        } else if (e.getSource() == Notification_Button) {
            NotificationPanel newNotifPanel = new NotificationPanel();
            Notification_Panel = newNotifPanel;
            mainPanel.add(Notification_Panel, BorderLayout.CENTER);
        }

        else if (e.getSource() == Create_Event_Button) { // === FOR THE CREATE EVENT BUTTON IN EVENT MANAGEMENT PANEL
            // TO CREATE EVENT ===
        }

        mainPanel.revalidate();
        mainPanel.repaint();

    }

    private void addEventToUI(Events event) {
        // Management panel button
        JButton newEventButton = new JButton(event.name + " @ " + event.location + " - " + event.date);
        newEventButton.setBounds(20, simpleEventY, 700, 40);
        newEventButton.setBackground(Color.GRAY);
        newEventButton.setForeground(Color.WHITE);
        newEventButton.setBorderPainted(false);
        newEventButton.setFocusPainted(false);

        // Edit on click
        newEventButton.addActionListener(ev -> {
            JTextField nameField = new JTextField(event.name);
            JTextField dateField = new JTextField(event.date);
            JTextField locationField = new JTextField(event.location);

            JPanel editPanel = new JPanel(new GridLayout(0, 1));
            editPanel.add(new JLabel("Event Name:"));
            editPanel.add(nameField);
            editPanel.add(new JLabel("Date:"));
            editPanel.add(dateField);
            editPanel.add(new JLabel("Location:"));
            editPanel.add(locationField);

            int result = JOptionPane.showConfirmDialog(null, editPanel, "Edit Event", JOptionPane.OK_CANCEL_OPTION);
            if (result == JOptionPane.OK_OPTION) {
                event.name = nameField.getText().trim();
                event.date = dateField.getText().trim();
                event.location = locationField.getText().trim();
                newEventButton.setText(event.name + " @ " + event.location + " - " + event.date);
            }
        });

        // Refresh events on home
        eventsPanel.removeAll();
        for (int i = 0; i < EventsDataBase.ne; i++) {
            Events e = EventsDataBase.Available_Events[i];
            JButton eventBox = new JButton("<html><div style='text-align:center;'>"
                    + "Name: " + e.name + "<br>"
                    + "Location: " + e.location + "<br>"
                    + "Date: " + e.date + "</div></html>");
            eventBox.setPreferredSize(new Dimension(200, 100));
            eventBox.setBackground(Color.LIGHT_GRAY);
            eventBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            eventBox.setFocusPainted(false);
            eventBox.setFont(new Font("Arial", Font.PLAIN, 12));
            eventsPanel.add(eventBox);
        }
        contentPanel.add(eventsPanel);
    }

    private void loadEventsFromDatabase() {
        try {
            Connection conn = db.DBConnection.getConnection();
            String query = "SELECT event_id, event_name, location, date FROM events";
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            eventsPanel.removeAll(); // Clear old UI

            while (rs.next()) {
                String name = rs.getString("event_name");
                String location = rs.getString("location");
                String date = rs.getString("date");

                Events event = new Events(name, location, date);
                // Events event = new Event(name, location, date, category, totalSeats);
                addEventToUI(event);
            }

            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error loading events: " + ex.getMessage(), "DB Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void applyCategoryFilter() {
        String selectedDate = (String) Date.getSelectedItem();
        String selectedCategory = (String) Category.getSelectedItem();
        String selectedLocation = (String) Location.getSelectedItem();

        content.eventsPanel.removeAll();

        for (models.Event ev : EventService.getAllEvents()) {
            boolean matchDate = selectedDate.equals("All") || ev.getDate().startsWith(selectedDate);
            boolean matchCategory = selectedCategory.equals("All") || ev.getCategory().equals(selectedCategory);
            boolean matchLocation = selectedLocation.equals("All") || ev.getLocation().equals(selectedLocation);

            if (matchDate && matchCategory && matchLocation) {
                content.addEventCard(ev.getEventId(), ev.getName(), ev.getLocation(), ev.getDate());
            }
        }

        content.eventsPanel.revalidate();
        content.eventsPanel.repaint();
    }

}
