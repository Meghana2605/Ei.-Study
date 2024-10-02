public class Message {
    import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

// Message.java
class Message {
    private String sender;
    private String content;

    public Message(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return sender + ": " + content;
    }
}

// User.java
class User {
    private String name;
    private ChatRoom chatRoom;

    public User(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void joinRoom(ChatRoom room) {
        if (chatRoom != null) {
            chatRoom.removeUser(this);
        }
        this.chatRoom = room;
        chatRoom.addUser(this);
    }

    public void sendMessage(String messageContent) {
        if (chatRoom != null) {
            chatRoom.broadcastMessage(new Message(name, messageContent));
        } else {
            System.out.println("You are not in a chat room.");
        }
    }

    public void receiveMessage(Message message) {
        System.out.println("[" + chatRoom.getRoomID() + "] " + message);
    }
}

// ChatRoom.java (Observer Pattern)
class ChatRoom {
    private String roomID;
    private List<User> users;
    private List<Message> messageHistory;

    public ChatRoom(String roomID) {
        this.roomID = roomID;
        this.users = new ArrayList<>();
        this.messageHistory = new ArrayList<>();
    }

    public String getRoomID() {
        return roomID;
    }

    public void addUser(User user) {
        users.add(user);
        broadcastMessage(new Message("System", user.getName() + " has joined the room."));
    }

    public void removeUser(User user) {
        users.remove(user);
        broadcastMessage(new Message("System", user.getName() + " has left the room."));
    }

    public void broadcastMessage(Message message) {
        messageHistory.add(message);
        for (User user : users) {
            user.receiveMessage(message);
        }
    }

    public void displayActiveUsers() {
        System.out.println("Active users in " + roomID + ":");
        for (User user : users) {
            System.out.println("- " + user.getName());
        }
    }

    public List<Message> getMessageHistory() {
        return messageHistory;
    }
}

// ChatRoomManager.java (Singleton Pattern)
class ChatRoomManager {
    private static ChatRoomManager instance;
    private Map<String, ChatRoom> chatRooms;

    private ChatRoomManager() {
        chatRooms = new HashMap<>();
    }

    public static ChatRoomManager getInstance() {
        if (instance == null) {
            instance = new ChatRoomManager();
        }
        return instance;
    }

    public ChatRoom createOrJoinRoom(String roomID) {
        ChatRoom room = chatRooms.get(roomID);
        if (room == null) {
            room = new ChatRoom(roomID);
            chatRooms.put(roomID, room);
            System.out.println("Chat room '" + roomID + "' created.");
        } else {
            System.out.println("Joined existing chat room '" + roomID + "'.");
        }
        return room;
    }

    public void displayAllRooms() {
        System.out.println("Available chat rooms:");
        for (String roomID : chatRooms.keySet()) {
            System.out.println("- " + roomID);
        }
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        ChatRoomManager manager = ChatRoomManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Real-time Chat Application!");
        System.out.print("Enter your username: ");
        String username = scanner.nextLine();
        User user = new User(username);

        while (true) {
            System.out.println("\nCommands: ");
            System.out.println("1. Create/Join Chat Room");
            System.out.println("2. Send Message");
            System.out.println("3. Display Active Users in Room");
            System.out.println("4. View Chat Rooms");
            System.out.println("5. View Message History");
            System.out.println("6. Exit");

            System.out.print("Choose an option: ");
            int option = Integer.parseInt(scanner.nextLine());

            switch (option) {
                case 1:
                    System.out.print("Enter chat room ID: ");
                    String roomID = scanner.nextLine();
                    ChatRoom room = manager.createOrJoinRoom(roomID);
                    user.joinRoom(room);
                    break;

                case 2:
                    System.out.print("Enter message: ");
                    String messageContent = scanner.nextLine();
                    user.sendMessage(messageContent);
                    break;

                case 3:
                    if (user.getChatRoom() != null) {
                        user.getChatRoom().displayActiveUsers();
                    } else {
                        System.out.println("You are not in a chat room.");
                    }
                    break;

                case 4:
                    manager.displayAllRooms();
                    break;

                case 5:
                    if (user.getChatRoom() != null) {
                        List<Message> history = user.getChatRoom().getMessageHistory();
                        for (Message message : history) {
                            System.out.println(message);
                        }
                    } else {
                        System.out.println("You are not in a chat room.");
                    }
                    break;

                case 6:
                    System.out.println("Exiting the chat application.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }
    }
}

}
