import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

// Task.java
class Task {
    private String description;
    private String startTime;
    private String endTime;
    private String priority;
    private boolean isCompleted;

    public Task(String description, String startTime, String endTime, String priority) {
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
        this.priority = priority;
        this.isCompleted = false;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getPriority() {
        return priority;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void markAsCompleted() {
        isCompleted = true;
    }

    @Override
    public String toString() {
        return startTime + " - " + endTime + ": " + description + " [" + priority + "]" + (isCompleted ? " (Completed)" : "");
    }
}

// TaskFactory.java
class TaskFactory {
    public static Task createTask(String description, String startTime, String endTime, String priority) {
        return new Task(description, startTime, endTime, priority);
    }
}

// TaskObserver.java
interface TaskObserver {
    void notifyConflict(String message);
}

// ScheduleManager.java
class ScheduleManager {
    private static ScheduleManager instance;
    private TreeMap<String, Task> taskMap;  // TreeMap keeps tasks sorted by start time
    private List<TaskObserver> observers;

    private ScheduleManager() {
        taskMap = new TreeMap<>();
        observers = new ArrayList<>();
    }

    public static ScheduleManager getInstance() {
        if (instance == null) {
            instance = new ScheduleManager();
        }
        return instance;
    }

    public void addTaskObserver(TaskObserver observer) {
        observers.add(observer);
    }

    public void removeTaskObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers(String message) {
        for (TaskObserver observer : observers) {
            observer.notifyConflict(message);
        }
    }

    // Add a task and check for conflicts
    public void addTask(Task task) {
        if (isConflict(task)) {
            notifyObservers("Conflict detected: " + task.getDescription() + " overlaps with an existing task.");
        } else {
            taskMap.put(task.getStartTime(), task);
            System.out.println("Task added successfully: " + task.getDescription());
        }
    }

    // Remove task by description
    public void removeTask(String description) {
        boolean taskFound = false;
        for (Task task : taskMap.values()) {
            if (task.getDescription().equals(description)) {
                taskMap.remove(task.getStartTime());
                System.out.println("Task removed successfully: " + description);
                taskFound = true;
                break;
            }
        }
        if (!taskFound) {
            System.out.println("Error: Task not found.");
        }
    }

    // View all tasks
    public void viewTasks() {
        if (taskMap.isEmpty()) {
            System.out.println("No tasks scheduled for the day.");
        } else {
            for (Task task : taskMap.values()) {
                System.out.println(task);
            }
        }
    }

    // Mark a task as completed
    public void markTaskCompleted(String description) {
        for (Task task : taskMap.values()) {
            if (task.getDescription().equals(description)) {
                task.markAsCompleted();
                System.out.println("Task marked as completed: " + description);
                return;
            }
        }
        System.out.println("Error: Task not found.");
    }

    // Check if the new task conflicts with existing tasks
    private boolean isConflict(Task newTask) {
        for (Task task : taskMap.values()) {
            if (!(newTask.getEndTime().compareTo(task.getStartTime()) <= 0 || newTask.getStartTime().compareTo(task.getEndTime()) >= 0)) {
                return true;
            }
        }
        return false;
    }
}

// ConflictNotifier.java
class ConflictNotifier implements TaskObserver {
    @Override
    public void notifyConflict(String message) {
        System.out.println(message);
    }
}

// Main.java
public class Main {
    public static void main(String[] args) {
        ScheduleManager scheduleManager = ScheduleManager.getInstance();
        scheduleManager.addTaskObserver(new ConflictNotifier());
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n1. Add Task");
            System.out.println("2. Remove Task");
            System.out.println("3. View Tasks");
            System.out.println("4. Mark Task as Completed");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume the newline

            switch (choice) {
                case 1:
                    System.out.print("Enter task description: ");
                    String description = scanner.nextLine();
                    System.out.print("Enter start time (HH:MM): ");
                    String startTime = scanner.nextLine();
                    System.out.print("Enter end time (HH:MM): ");
                    String endTime = scanner.nextLine();
                    System.out.print("Enter priority (High/Medium/Low): ");
                    String priority = scanner.nextLine();

                    Task task = TaskFactory.createTask(description, startTime, endTime, priority);
                    scheduleManager.addTask(task);
                    break;

                case 2:
                    System.out.print("Enter task description to remove: ");
                    String taskToRemove = scanner.nextLine();
                    scheduleManager.removeTask(taskToRemove);
                    break;

                case 3:
                    scheduleManager.viewTasks();
                    break;

                case 4:
                    System.out.print("Enter task description to mark as completed: ");
                    String completedTask = scanner.nextLine();
                    scheduleManager.markTaskCompleted(completedTask);
                    break;

                case 5:
                    System.out.println("Exiting the application.");
                    scanner.close();
                    return;

                default:
                    System.out.println("Invalid option. Try again.");
                    break;
            }
        }
    }
}
