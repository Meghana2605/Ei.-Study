public class Room {
    import java.util.*;

// Room.java
class Room {
    private int roomNumber;
    private boolean isBooked;
    private boolean isOccupied;
    private boolean isLightsOn;
    private boolean isAcOn;

    public Room(int roomNumber) {
        this.roomNumber = roomNumber;
        this.isBooked = false;
        this.isOccupied = false;
        this.isLightsOn = false;
        this.isAcOn = false;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void bookRoom() {
        this.isBooked = true;
        System.out.println("Room " + roomNumber + " has been booked.");
    }

    public void cancelBooking() {
        this.isBooked = false;
        System.out.println("Booking for Room " + roomNumber + " has been canceled.");
    }

    public void setOccupied(boolean occupied) {
        this.isOccupied = occupied;
    }

    public boolean isOccupied() {
        return isOccupied;
    }

    public void turnLightsOn() {
        this.isLightsOn = true;
        System.out.println("Lights turned on in Room " + roomNumber);
    }

    public void turnLightsOff() {
        this.isLightsOn = false;
        System.out.println("Lights turned off in Room " + roomNumber);
    }

    public void turnAcOn() {
        this.isAcOn = true;
        System.out.println("AC turned on in Room " + roomNumber);
    }

    public void turnAcOff() {
        this.isAcOn = false;
        System.out.println("AC turned off in Room " + roomNumber);
    }

    public boolean isLightsOn() {
        return isLightsOn;
    }

    public boolean isAcOn() {
        return isAcOn;
    }
}

// OfficeConfiguration.java (Singleton Pattern)
class OfficeConfiguration {
    private static OfficeConfiguration instance;
    private Map<Integer, Room> rooms;

    private OfficeConfiguration() {
        rooms = new HashMap<>();
    }

    public static OfficeConfiguration getInstance() {
        if (instance == null) {
            instance = new OfficeConfiguration();
        }
        return instance;
    }

    public void configureRooms(int numberOfRooms) {
        for (int i = 1; i <= numberOfRooms; i++) {
            rooms.put(i, new Room(i));
        }
        System.out.println(numberOfRooms + " rooms have been configured.");
    }

    public Room getRoom(int roomNumber) {
        return rooms.get(roomNumber);
    }

    public Collection<Room> getAllRooms() {
        return rooms.values();
    }
}

// BookingCommand.java (Command Pattern)
class BookingCommand {
    private OfficeConfiguration officeConfig;

    public BookingCommand(OfficeConfiguration officeConfig) {
        this.officeConfig = officeConfig;
    }

    public void bookRoom(int roomNumber) {
        Room room = officeConfig.getRoom(roomNumber);
        if (room != null && !room.isBooked()) {
            room.bookRoom();
        } else {
            System.out.println("Room " + roomNumber + " is either already booked or does not exist.");
        }
    }
}

// CancellationCommand.java (Command Pattern)
class CancellationCommand {
    private OfficeConfiguration officeConfig;

    public CancellationCommand(OfficeConfiguration officeConfig) {
        this.officeConfig = officeConfig;
    }

    public void cancelBooking(int roomNumber) {
        Room room = officeConfig.getRoom(roomNumber);
        if (room != null && room.isBooked()) {
            room.cancelBooking();
        } else {
            System.out.println("Room " + roomNumber + " is not booked or does not exist.");
        }
    }
}

// RoomStatusObserver.java (Observer Pattern)
interface RoomStatusObserver {
    void update(Room room);
}

// OccupancySensor.java
class OccupancySensor implements RoomStatusObserver {
    @Override
    public void update(Room room) {
        if (room.isOccupied()) {
            room.turnLightsOn();
            room.turnAcOn();
        } else {
            room.turnLightsOff();
            room.turnAcOff();
        }
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        OfficeConfiguration officeConfig = OfficeConfiguration.getInstance();
        OccupancySensor occupancySensor = new OccupancySensor();

        System.out.println("Enter the number of rooms to configure: ");
        int numberOfRooms = scanner.nextInt();
        officeConfig.configureRooms(numberOfRooms);

        BookingCommand bookingCommand = new BookingCommand(officeConfig);
        CancellationCommand cancellationCommand = new CancellationCommand(officeConfig);

        while (true) {
            System.out.println("\n1. Book Room");
            System.out.println("2. Cancel Booking");
            System.out.println("3. Check Occupancy and Control Lights/AC");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    System.out.print("Enter room number to book: ");
                    int roomToBook = scanner.nextInt();
                    bookingCommand.bookRoom(roomToBook);
                    break;

                case 2:
                    System.out.print("Enter room number to cancel booking: ");
                    int roomToCancel = scanner.nextInt();
                    cancellationCommand.cancelBooking(roomToCancel);
                    break;

                case 3:
                    System.out.print("Enter room number to check occupancy (2 people to mark as occupied): ");
                    int roomNumber = scanner.nextInt();
                    Room room = officeConfig.getRoom(roomNumber);
                    if (room != null) {
                        System.out.print("Enter number of people in the room: ");
                        int occupants = scanner.nextInt();
                        if (occupants >= 2) {
                            room.setOccupied(true);
                        } else {
                            room.setOccupied(false);
                        }
                        occupancySensor.update(room);
                    } else {
                        System.out.println("Invalid room number.");
                    }
                    break;

                case 4:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}

}
