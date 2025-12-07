package models;

import java.sql.Timestamp;

public class Notification {
    private int notificationId;
    private int userId;
    private int adminId;
    private int eventId;
    private String message;
    private String notificationType;
    private Timestamp sentTime;
    private boolean readStatus;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public Notification(int notificationId, int userId, int adminId, int eventId, String message,
                        String notificationType, Timestamp sentTime, boolean readStatus,
                        Timestamp createdAt, Timestamp updatedAt) {
        this.notificationId = notificationId;
        this.userId = userId;
        this.adminId = adminId;
        this.eventId = eventId;
        this.message = message;
        this.notificationType = notificationType;
        this.sentTime = sentTime;
        this.readStatus = readStatus;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getNotificationId() { return notificationId; }
    public int getUserId() { return userId; }
    public int getAdminId() { return adminId; }
    public int getEventId() { return eventId; }
    public String getMessage() { return message; }
    public String getNotificationType() { return notificationType; }
    public Timestamp getSentTime() { return sentTime; }
    public boolean isReadStatus() { return readStatus; }
    public Timestamp getCreatedAt() { return createdAt; }
    public Timestamp getUpdatedAt() { return updatedAt; }
} 
