public class Direction {
    import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

// Direction.java (Enum for directions)
enum Direction {
    N, S, E, W;

    public Direction left() {
        switch (this) {
            case N:
                return W;
            case W:
                return S;
            case S:
                return E;
            case E:
                return N;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }

    public Direction right() {
        switch (this) {
            case N:
                return E;
            case E:
                return S;
            case S:
                return W;
            case W:
                return N;
            default:
                throw new IllegalArgumentException("Invalid direction");
        }
    }
}

// Position.java (Class representing coordinates)
class Position {
    int x, y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Position position = (Position) obj;
        return x == position.x && y == position.y;
    }

    @Override
    public int hashCode() {
        return 31 * x + y;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}

// Rover.java (Rover class encapsulating position and direction)
class Rover {
    private Position position;
    private Direction direction;
    private Grid grid;

    public Rover(int startX, int startY, Direction startDirection, Grid grid) {
        this.position = new Position(startX, startY);
        this.direction = startDirection;
        this.grid = grid;
    }

    public Position getPosition() {
        return position;
    }

    public Direction getDirection() {
        return direction;
    }

    public void moveForward() {
        Position newPosition = calculateNextPosition();
        if (grid.isObstacle(newPosition)) {
            System.out.println("Obstacle detected at " + newPosition + ". Cannot move forward.");
        } else if (!grid.isValidPosition(newPosition)) {
            System.out.println("Rover cannot move outside the grid.");
        } else {
            this.position = newPosition;
        }
    }

    public void turnLeft() {
        this.direction = direction.left();
    }

    public void turnRight() {
        this.direction = direction.right();
    }

    private Position calculateNextPosition() {
        switch (direction) {
            case N:
                return new Position(position.x, position.y + 1);
            case S:
                return new Position(position.x, position.y - 1);
            case E:
                return new Position(position.x + 1, position.y);
            case W:
                return new Position(position.x - 1, position.y);
            default:
                throw new IllegalStateException("Unexpected value: " + direction);
        }
    }

    public void statusReport() {
        System.out.println("Rover is at position " + position + " facing " + direction + ".");
    }
}

// Command.java (Abstract Command class)
abstract class Command {
    protected Rover rover;

    public Command(Rover rover) {
        this.rover = rover;
    }

    public abstract void execute();
}

// MoveCommand.java (Command for moving forward)
class MoveCommand extends Command {
    public MoveCommand(Rover rover) {
        super(rover);
    }

    @Override
    public void execute() {
        rover.moveForward();
    }
}

// TurnLeftCommand.java (Command for turning left)
class TurnLeftCommand extends Command {
    public TurnLeftCommand(Rover rover) {
        super(rover);
    }

    @Override
    public void execute() {
        rover.turnLeft();
    }
}

// TurnRightCommand.java (Command for turning right)
class TurnRightCommand extends Command {
    public TurnRightCommand(Rover rover) {
        super(rover);
    }

    @Override
    public void execute() {
        rover.turnRight();
    }
}

// Grid.java (Composite pattern representing the grid and obstacles)
class Grid {
    private int width, height;
    private Set<Position> obstacles;

    public Grid(int width, int height) {
        this.width = width;
        this.height = height;
        this.obstacles = new HashSet<>();
    }

    public void addObstacle(int x, int y) {
        obstacles.add(new Position(x, y));
    }

    public boolean isObstacle(Position position) {
        return obstacles.contains(position);
    }

    public boolean isValidPosition(Position position) {
        return position.x >= 0 && position.x < width && position.y >= 0 && position.y < height;
    }
}

// Main.java (Main class to run the simulation)
public class Main {
    public static void main(String[] args) {
        // Initialize grid and rover
        Grid grid = new Grid(10, 10);
        grid.addObstacle(2, 2);
        grid.addObstacle(3, 5);

        Rover rover = new Rover(0, 0, Direction.N, grid);

        // Command pattern for controlling the rover
        Command move = new MoveCommand(rover);
        Command turnLeft = new TurnLeftCommand(rover);
        Command turnRight = new TurnRightCommand(rover);

        Scanner scanner = new Scanner(System.in);

        // Input command simulation
        System.out.println("Enter commands (M: Move, L: Turn Left, R: Turn Right, Q: Quit):");
        while (true) {
            String command = scanner.nextLine().toUpperCase();
            switch (command) {
                case "M":
                    move.execute();
                    break;
                case "L":
                    turnLeft.execute();
                    break;
                case "R":
                    turnRight.execute();
                    break;
                case "Q":
                    rover.statusReport();
                    System.out.println("Exiting the simulation.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid command! Use M, L, R, or Q.");
                    break;
            }
            rover.statusReport(); // Output the status of the rover after every command
        }
    }
}

}
