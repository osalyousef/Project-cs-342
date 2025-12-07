package models;

public class Event {
    private int event_id;
    private String name;
    private String location;
    private String date;
    private String category;
    private int totalSeats; 

    public Event(int event_id, String name, String location, String date, String category , int totalSeats) {
        this.event_id = event_id;
        this.name = name;
        this.location = location;
        this.date = date;
        this.category = category;
        this.totalSeats = totalSeats;
    }
    public Event(String name, String location, String date) {
        this.name = name;
        this.location = location;
        this.date = date;
    }

    public int getEventId() { return event_id; }
    public String getName() { return name; }
    public String getLocation() { return location; }
    public String getDate() { return date; }
    public String getCategory() { return category; }
    // get total seats
    public int getTotalSeats() { return totalSeats; }
}
