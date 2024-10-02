public class Device {
    import java.util.*;

// Device.java (Abstract class for Smart Devices)
abstract class Device {
    protected int id;
    protected String type;
    protected String status;

    public Device(int id, String type) {
        this.id = id;
        this.type = type;
        this.status = "off";  // Default status
    }

    public int getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getStatus() {
        return status;
    }

    public abstract void turnOn();

    public abstract void turnOff();

    public abstract String statusReport();
}

// Light.java
class Light extends Device {
    public Light(int id) {
        super(id, "light");
    }

    @Override
    public void turnOn() {
        status = "on";
        System.out.println("Light " + id + " turned on.");
    }

    @Override
    public void turnOff() {
        status = "off";
        System.out.println("Light " + id + " turned off.");
    }

    @Override
    public String statusReport() {
        return "Light " + id + " is " + status;
    }
}

// Thermostat.java
class Thermostat extends Device {
    private int temperature;

    public Thermostat(int id) {
        super(id, "thermostat");
        this.temperature = 70;  // Default temperature
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
        System.out.println("Thermostat " + id + " temperature set to " + temperature);
    }

    public int getTemperature() {
        return temperature;
    }

    @Override
    public void turnOn() {
        status = "on";
        System.out.println("Thermostat " + id + " turned on.");
    }

    @Override
    public void turnOff() {
        status = "off";
        System.out.println("Thermostat " + id + " turned off.");
    }

    @Override
    public String statusReport() {
        return "Thermostat " + id + " is " + status + " at " + temperature + " degrees";
    }
}

// DoorLock.java
class DoorLock extends Device {
    public DoorLock(int id) {
        super(id, "door");
        this.status = "locked";  // Default status
    }

    @Override
    public void turnOn() {
        status = "locked";
        System.out.println("Door " + id + " locked.");
    }

    @Override
    public void turnOff() {
        status = "unlocked";
        System.out.println("Door " + id + " unlocked.");
    }

    @Override
    public String statusReport() {
        return "Door " + id + " is " + status;
    }
}

// DeviceFactory.java (Factory Method)
class DeviceFactory {
    public static Device createDevice(String type, int id) {
        switch (type.toLowerCase()) {
            case "light":
                return new Light(id);
            case "thermostat":
                return new Thermostat(id);
            case "door":
                return new DoorLock(id);
            default:
                throw new IllegalArgumentException("Unknown device type: " + type);
        }
    }
}

// Command.java (Abstract command class)
abstract class Command {
    protected Device device;

    public Command(Device device) {
        this.device = device;
    }

    public abstract void execute();
}

// TurnOnCommand.java
class TurnOnCommand extends Command {
    public TurnOnCommand(Device device) {
        super(device);
    }

    @Override
    public void execute() {
        device.turnOn();
    }
}

// TurnOffCommand.java
class TurnOffCommand extends Command {
    public TurnOffCommand(Device device) {
        super(device);
    }

    @Override
    public void execute() {
        device.turnOff();
    }
}

// Scheduler.java (For scheduling commands)
class Scheduler {
    private Map<String, List<Command>> scheduleMap = new HashMap<>();

    public void scheduleCommand(String time, Command command) {
        scheduleMap.putIfAbsent(time, new ArrayList<>());
        scheduleMap.get(time).add(command);
        System.out.println("Scheduled command for " + time);
    }

    public void runAt(String time) {
        if (scheduleMap.containsKey(time)) {
            for (Command command : scheduleMap.get(time)) {
                command.execute();
            }
        }
    }
}

// Trigger.java (Observer-like triggers)
class Trigger {
    private Thermostat thermostat;
    private Device targetDevice;
    private String action;
    private int triggerTemperature;

    public Trigger(Thermostat thermostat, Device targetDevice, int triggerTemperature, String action) {
        this.thermostat = thermostat;
        this.targetDevice = targetDevice;
        this.action = action;
        this.triggerTemperature = triggerTemperature;
    }

    public void checkCondition() {
        if (thermostat.getTemperature() > triggerTemperature && action.equals("turnOff")) {
            targetDevice.turnOff();
            System.out.println("Trigger activated: Turning off " + targetDevice.getType() + " because temperature > " + triggerTemperature);
        }
    }
}

// SmartHomeHub.java (Central controller)
class SmartHomeHub {
    private Map<Integer, Device> devices = new HashMap<>();
    private Scheduler scheduler = new Scheduler();

    public void addDevice(Device device) {
        devices.put(device.getId(), device);
    }

    public Device getDevice(int id) {
        return devices.get(id);
    }

    public void runSchedule(String time) {
        scheduler.runAt(time);
    }

    public void scheduleCommand(int deviceId, String time, Command command) {
        scheduler.scheduleCommand(time, command);
    }

    public void reportStatus() {
        for (Device device : devices.values()) {
            System.out.println(device.statusReport());
        }
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        SmartHomeHub hub = new SmartHomeHub();

        // Initialize devices
        Device light = DeviceFactory.createDevice("light", 1);
        Device thermostat = DeviceFactory.createDevice("thermostat", 2);
        Device doorLock = DeviceFactory.createDevice("door", 3);

        // Add devices to the hub
        hub.addDevice(light);
        hub.addDevice(thermostat);
        hub.addDevice(doorLock);

        // Create commands
        Command turnOnLight = new TurnOnCommand(light);
        Command turnOffLight = new TurnOffCommand(light);
        Command turnOnThermostat = new TurnOnCommand(thermostat);
        Command turnOffThermostat = new TurnOffCommand(thermostat);

        // Schedule turning on devices
        hub.scheduleCommand(1, "06:00", turnOnLight);
        hub.scheduleCommand(2, "07:00", turnOnThermostat);

        // Run schedule at a certain time
        hub.runSchedule("06:00");

        // Set up a trigger based on the temperature
        Trigger tempTrigger = new Trigger((Thermostat) thermostat, light, 75, "turnOff");
        ((Thermostat) thermostat).setTemperature(80);  // Trigger condition
        tempTrigger.checkCondition();

        // View the status of all devices
        hub.reportStatus();
    }
}

}
