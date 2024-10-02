public class Assignment {
    import java.util.*;

// Assignment.java (Represents an assignment)
class Assignment {
    private String details;

    public Assignment(String details) {
        this.details = details;
    }

    public String getDetails() {
        return details;
    }
}

// Student.java (Represents a student)
class Student {
    private String id;
    private String name;

    public Student(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}

// Classroom.java (Represents a classroom)
class Classroom {
    private String name;
    private List<Student> students;
    private List<Assignment> assignments;

    public Classroom(String name) {
        this.name = name;
        this.students = new ArrayList<>();
        this.assignments = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void addStudent(Student student) {
        students.add(student);
        System.out.println("Student " + student.getId() + " has been enrolled in " + name + ".");
    }

    public void scheduleAssignment(String details) {
        Assignment assignment = new Assignment(details);
        assignments.add(assignment);
        System.out.println("Assignment for " + name + " has been scheduled: " + details);
    }

    public void submitAssignment(String studentId, String details) {
        System.out.println("Assignment submitted by Student " + studentId + " in " + name + ": " + details);
    }

    public List<Student> getStudents() {
        return students;
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void listStudents() {
        System.out.println("Students in " + name + ":");
        for (Student student : students) {
            System.out.println("- " + student.getName() + " (ID: " + student.getId() + ")");
        }
    }
}

// ClassroomManager.java (Singleton for managing classrooms)
class ClassroomManager {
    private static ClassroomManager instance;
    private Map<String, Classroom> classrooms;

    private ClassroomManager() {
        classrooms = new HashMap<>();
    }

    public static ClassroomManager getInstance() {
        if (instance == null) {
            instance = new ClassroomManager();
        }
        return instance;
    }

    public void addClassroom(String name) {
        classrooms.put(name, new Classroom(name));
        System.out.println("Classroom " + name + " has been created.");
    }

    public Classroom getClassroom(String name) {
        return classrooms.get(name);
    }

    public void listClassrooms() {
        System.out.println("Available Classrooms:");
        for (String name : classrooms.keySet()) {
            System.out.println("- " + name);
        }
    }
}

// Main.java (User interaction and command processing)
public class Main {
    public static void main(String[] args) {
        ClassroomManager manager = ClassroomManager.getInstance();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Virtual Classroom Manager!");
        System.out.println("Commands: add_classroom, add_student, schedule_assignment, submit_assignment, list_students, list_classrooms, exit");

        while (true) {
            System.out.print("\nEnter command: ");
            String input = scanner.nextLine();
            String[] command = input.split(" ");

            switch (command[0]) {
                case "add_classroom":
                    if (command.length == 2) {
                        manager.addClassroom(command[1]);
                    } else {
                        System.out.println("Usage: add_classroom [class_name]");
                    }
                    break;

                case "add_student":
                    if (command.length == 3) {
                        String studentId = command[1];
                        String classroomName = command[2];
                        Classroom classroom = manager.getClassroom(classroomName);
                        if (classroom != null) {
                            classroom.addStudent(new Student(studentId, "Student " + studentId));
                        } else {
                            System.out.println("Classroom " + classroomName + " does not exist.");
                        }
                    } else {
                        System.out.println("Usage: add_student [student_id] [class_name]");
                    }
                    break;

                case "schedule_assignment":
                    if (command.length >= 3) {
                        String classroomName = command[1];
                        StringBuilder details = new StringBuilder();
                        for (int i = 2; i < command.length; i++) {
                            details.append(command[i]).append(" ");
                        }
                        Classroom classroom = manager.getClassroom(classroomName);
                        if (classroom != null) {
                            classroom.scheduleAssignment(details.toString().trim());
                        } else {
                            System.out.println("Classroom " + classroomName + " does not exist.");
                        }
                    } else {
                        System.out.println("Usage: schedule_assignment [class_name] [assignment_details]");
                    }
                    break;

                case "submit_assignment":
                    if (command.length >= 4) {
                        String studentId = command[1];
                        String classroomName = command[2];
                        StringBuilder details = new StringBuilder();
                        for (int i = 3; i < command.length; i++) {
                            details.append(command[i]).append(" ");
                        }
                        Classroom classroom = manager.getClassroom(classroomName);
                        if (classroom != null) {
                            classroom.submitAssignment(studentId, details.toString().trim());
                        } else {
                            System.out.println("Classroom " + classroomName + " does not exist.");
                        }
                    } else {
                        System.out.println("Usage: submit_assignment [student_id] [class_name] [assignment_details]");
                    }
                    break;

                case "list_students":
                    if (command.length == 2) {
                        String classroomName = command[1];
                        Classroom classroom = manager.getClassroom(classroomName);
                        if (classroom != null) {
                            classroom.listStudents();
                        } else {
                            System.out.println("Classroom " + classroomName + " does not exist.");
                        }
                    } else {
                        System.out.println("Usage: list_students [class_name]");
                    }
                    break;

                case "list_classrooms":
                    manager.listClassrooms();
                    break;

                case "exit":
                    System.out.println("Exiting the Virtual Classroom Manager.");
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
