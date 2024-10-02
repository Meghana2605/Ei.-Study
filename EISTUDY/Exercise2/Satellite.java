public class Satellite {
    import java.util.Scanner;

// Satellite.java (Encapsulates satellite state)
class Satellite {
    private String orientation;
    private boolean solarPanelsActive;
    private int dataCollected;

    public Satellite() {
        this.orientation = "North";    // Default orientation
        this.solarPanelsActive = false;  // Solar panels start as inactive
        this.dataCollected = 0;          // No data collected initially
    }

    public void rotate(String direction) {
        this.orientation = direction;
        System.out.println("Satellite rotated to " + direction + ".");
    }

    public void activatePanels() {
        this.solarPanelsActive = true;
        System.out.println("Solar panels activated.");
    }

    public void deactivatePanels() {
        this.solarPanelsActive = false;
        System.out.println("Solar panels deactivated.");
    }

    public void collectData() {
        if (solarPanelsActive) {
            dataCollected += 10;
            System.out.println("Data collected. Total: " + dataCollected + " units.");
        } else {
            System.out.println("Cannot collect data. Solar panels are inactive.");
        }
    }

    public void statusReport() {
        System.out.println("=== Satellite Status Report ===");
        System.out.println("Orientation: " + orientation);
        System.out.println("Solar Panels: " + (solarPanelsActive ? "Active" : "Inactive"));
        System.out.println("Data Collected: " + dataCollected + " units");
    }
}

// Command.java (Command interface)
interface Command {
    void execute();
}

// RotateCommand.java
class RotateCommand implements Command {
    private Satellite satellite;
    private String direction;

    public RotateCommand(Satellite satellite, String direction) {
        this.satellite = satellite;
        this.direction = direction;
    }

    @Override
    public void execute() {
        satellite.rotate(direction);
    }
}

// ActivatePanelsCommand.java
class ActivatePanelsCommand implements Command {
    private Satellite satellite;

    public ActivatePanelsCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.activatePanels();
    }
}

// DeactivatePanelsCommand.java
class DeactivatePanelsCommand implements Command {
    private Satellite satellite;

    public DeactivatePanelsCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.deactivatePanels();
    }
}

// CollectDataCommand.java
class CollectDataCommand implements Command {
    private Satellite satellite;

    public CollectDataCommand(Satellite satellite) {
        this.satellite = satellite;
    }

    @Override
    public void execute() {
        satellite.collectData();
    }
}

// Main.java (Command simulation)
public class Main {
    public static void main(String[] args) {
        Satellite satellite = new Satellite();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\nEnter command (rotate, activatePanels, deactivatePanels, collectData, status, exit):");
            String commandInput = scanner.nextLine();

            Command command = null;

            switch (commandInput) {
                case "rotate":
                    System.out.println("Enter direction (North, South, East, West):");
                    String direction = scanner.nextLine();
                    command = new RotateCommand(satellite, direction);
                    break;
                case "activatePanels":
                    command = new ActivatePanelsCommand(satellite);
                    break;
                case "deactivatePanels":
                    command = new DeactivatePanelsCommand(satellite);
                    break;
                case "collectData":
                    command = new CollectDataCommand(satellite);
                    break;
                case "status":
                    satellite.statusReport();
                    continue;
                case "exit":
                    System.out.println("Exiting the simulation.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command.");
                    continue;
            }

            if (command != null) {
                command.execute();
            }
        }
    }
}

}
