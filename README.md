# NotificationAppRouting
Firebase Cloud Messaging version: 9.2.1

## Incoming Notification Handling
First, please remember how FCM/GCM handle the notification payloads. There are two types of payload:
1. Notification Payload
2. Data Payload

| App State  | Data Payload *ONLY* | Include Notification Payload              |
| ---------- |:-------------------:|:-----------------------------------------:|
| Foreground | onMessageReceived   | onMessageReceived                         |
| Background | onMessageReceived   | auto redirect to system notification tray |

