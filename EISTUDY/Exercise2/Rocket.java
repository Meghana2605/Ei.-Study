public class Rocket {
    import java.util.Scanner;

// Rocket.java (Simulates the state of the rocket)
class Rocket {
    private String stage;
    private int fuel;
    private int altitude;
    private int speed;
    private boolean preLaunchChecksCompleted;
    private boolean isLaunched;

    public Rocket() {
        this.stage = "Pre-Launch";
        this.fuel = 100;         // Initial fuel is 100%
        this.altitude = 0;       // Initial altitude
        this.speed = 0;          // Initial speed
        this.preLaunchChecksCompleted = false;
        this.isLaunched = false;
    }

    // Perform pre-launch checks
    public void performPreLaunchChecks() {
        if (!preLaunchChecksCompleted) {
            preLaunchChecksCompleted = true;
            System.out.println("Pre-Launch Checks: All systems are 'Go' for launch.");
        } else {
            System.out.println("Pre-Launch checks already completed.");
        }
    }

    // Launch the rocket
    public void launch() {
        if (!preLaunchChecksCompleted) {
            System.out.println("Error: Pre-Launch checks not completed.");
            return;
        }

        if (isLaunched) {
            System.out.println("Error: Rocket has already been launched.");
            return;
        }

        isLaunched = true;
        stage = "Stage 1";
        System.out.println("Rocket has launched!");
    }

    // Simulate one second of flight
    public void simulateSecond() {
        if (!isLaunched) {
            System.out.println("Error: Rocket has not been launched.");
            return;
        }

        if (fuel <= 0) {
            System.out.println("Mission Failure: Rocket has run out of fuel.");
            return;
        }

        if (stage.equals("Stage 1")) {
            fuel -= 10;
            altitude += 10;
            speed += 1000;

            System.out.println("Stage: 1, Fuel: " + fuel + "%, Altitude: " + altitude + " km, Speed: " + speed + " km/h");

            if (fuel <= 50) {
                stageSeparation();
            }
        } else if (stage.equals("Stage 2")) {
            fuel -= 5;
            altitude += 20;
            speed += 2000;

            System.out.println("Stage: 2, Fuel: " + fuel + "%, Altitude: " + altitude + " km, Speed: " + speed + " km/h");

            if (altitude >= 100) {
                orbitPlacement();
            }
        }
    }

    // Fast forward simulation by X seconds
    public void fastForward(int seconds) {
        for (int i = 0; i < seconds; i++) {
            simulateSecond();
        }
    }

    // Handle stage separation
    private void stageSeparation() {
        System.out.println("Stage Separation: Stage 1 complete. Entering Stage 2.");
        stage = "Stage 2";
    }

    // Handle orbit placement
    private void orbitPlacement() {
        System.out.println("Orbit Placement: Orbit achieved! Mission Successful.");
        stage = "Orbit";
    }

    // Status report
    public void statusReport() {
        System.out.println("\n=== Rocket Status Report ===");
        System.out.println("Stage: " + stage);
        System.out.println("Fuel: " + fuel + "%");
        System.out.println("Altitude: " + altitude + " km");
        System.out.println("Speed: " + speed + " km/h");
    }
}

// Main.java (Simulates the user interaction and rocket simulation)
public class Main {
    public static void main(String[] args) {
        Rocket rocket = new Rocket();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Rocket Launch Simulator!");
        System.out.println("Commands: start_checks, launch, fast_forward X, status, exit");

        while (true) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine();
            String[] command = input.split(" ");

            switch (command[0]) {
                case "start_checks":
                    rocket.performPreLaunchChecks();
                    break;

                case "launch":
                    rocket.launch();
                    break;

                case "fast_forward":
                    if (command.length == 2) {
                        try {
                            int seconds = Integer.parseInt(command[1]);
                            rocket.fastForward(seconds);
                        } catch (NumberFormatException e) {
                            System.out.println("Invalid input. Please enter a valid number of seconds.");
                        }
                    } else {
                        System.out.println("Invalid command format. Use: fast_forward X");
                    }
                    break;

                case "status":
                    rocket.statusReport();
                    break;

                case "exit":
                    System.out.println("Exiting the Rocket Launch Simulator.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid command. Please try again.");
                    break;
            }
        }
    }
}

}
