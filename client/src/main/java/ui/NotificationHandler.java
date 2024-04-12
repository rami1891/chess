package ui;
import webSocketMessages.serverMessages.Notification;

public interface NotificationHandler {
    void notify(String notification);
}
