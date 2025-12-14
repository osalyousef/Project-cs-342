package pages;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

import models.Event;

import sections.SideMenuPanel;
import sections.HomeContentPanel;
import sections.ProfilePanel;
import sections.TicketsPanel;
import sections.EventManagementPanel;
import sections.NotificationPanel;

import auth.Session;
import services.EventService;

public class HomePage extends JFrame implements ActionListener {
    // Menu and main components
    private JButton Home_Button, Tickets_Button, Event_Management_Button, Profile_Button, Notification_Button,
            Text_Search_Button; // menu
    private JButton Create_Event_Button, Combo_Search_Button;
    private JPanel menuPanel, Tickets_Panel, contentPanel, Event_Management_Panel, Profile_Panel, Notification_Panel,
            mainPanel, eventsPanel;
            
    private JComboBox DateDropdown, Category, Location;
    
    private JLabel Password_Label, User_Name_Label, Email_Label, Title_Label, Tickets_Label, Event_Management_Label,
            Reg_Events_Label, Your_Events_Label, Profile_Label, Notification_Label;
    private JTextField Search_Field;

    HomeContentPanel content;

    int simpleEventY = 285; // where to place next event label

    // get all user data from seission and assign them to the variables
    private String username = Session.getUsername();
    private String email = Session.getEmail();

    // load filter data from service
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

        // ===MAIN PANEL=== 
        mainPanel = new JPanel(new BorderLayout());

        // ===Main content panel=== 
        content = new HomeContentPanel(DATES, CATEGORIES, LOCATIONS);

        contentPanel = content;
        Search_Field = content.searchField;
        
        // assign buttons and listeners for filters
        Combo_Search_Button = content.comboSearchButton;
        Combo_Search_Button.addActionListener(e -> applyCategoryFilter());
        
        content.categoryBox.addActionListener(e -> applyCategoryFilter());
        content.dateBox.addActionListener(e -> applyCategoryFilter());
        content.locationBox.addActionListener(e -> applyCategoryFilter());

        // assign variables (DateDropdown instead of Date)
        DateDropdown = content.dateBox;
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

        DateDropdown.setBounds(320, 110, 100, 30);
        DateDropdown.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        DateDropdown.setBackground(Color.WHITE);
        DateDropdown.setForeground(Color.DARK_GRAY);
        DateDropdown.setFont(new Font("Arial", Font.PLAIN, 14));

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

        //Panel contains Available events 
        eventsPanel = new JPanel();
        eventsPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 20));
        eventsPanel.setBounds(0, 140, 800, 400);
        eventsPanel.setBackground(Color.WHITE);

        // =====Tickets Panel and ITS DESIGN=====
        TicketsPanel ticketsSection = new TicketsPanel();
        Tickets_Panel = ticketsSection;
        Tickets_Label = ticketsSection.ticketsLabel;

        // ====Event Management Panel and ITS DESIGN===
        EventManagementPanel eventMgmtSection = new EventManagementPanel();
        Event_Management_Panel = eventMgmtSection;
        Event_Management_Label = eventMgmtSection.titleLabel;
        Create_Event_Button = eventMgmtSection.createEventButton;
        
        if(Create_Event_Button != null) {
            Create_Event_Button.addActionListener(this);
        }
        

        // ====Profile Panel and ITS DESIGN=====
        ProfilePanel profileSection = new ProfilePanel();
        Profile_Panel = profileSection;
        Profile_Label = profileSection.titleLabel;
        User_Name_Label = profileSection.usernameLabel;
        Email_Label = profileSection.emailLabel;

        // ====Notification Panel and ITS DESIGN====
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

        // search listener
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
            loadEventsFromDatabase();
            mainPanel.add(contentPanel, BorderLayout.CENTER);
            
        } else if (e.getSource() == Tickets_Button) { // ==== GOES TO TICKETS PANEL ===
            TicketsPanel newTicketsPanel = new TicketsPanel(); 
            Tickets_Panel = newTicketsPanel;
            mainPanel.add(Tickets_Panel, BorderLayout.CENTER);
            
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

    // load events from database
    private void loadEventsFromDatabase() {
        // use home content panel card system
        content.eventsPanel.removeAll();
        for (models.Event e : EventService.getAllEvents()) {
            content.addEventCard(e.getEventId(), e.getName(), e.getLocation(), e.getDate());
        }
        content.eventsPanel.revalidate();
        content.eventsPanel.repaint();
    }

    // apply category filter
    private void applyCategoryFilter() {
        String selectedDate = (String) DateDropdown.getSelectedItem();
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