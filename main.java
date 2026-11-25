import java.util.*;
import java.io.*;

class Student {
    String id;
    String name;

    Student(String id, String name) {
        this.id = id;
        this.name = name;
    }
}

public class main {

    // 2D array: 4 rows and 3 seats
    // Rows 0-1 = Room 1
    // Rows 2-3 = Room 2
    static Student[][] seats = new Student[4][3];

    // Waiting queue
    static Queue<Student> waitingQueue = new LinkedList<>();

    // Room connectivity graph using adjacency matrix
    static int[][] roomGraph = {
        {0, 1},
        {1, 0}
    };

    static int nextId = 101;

    static Scanner sc = new Scanner(System.in);

    // Generate student ID
    static String generateId() {
        return String.format("E%03d", nextId++);
    }

    // Find room number
    static int getRoom(int row) {
        return (row / 2) + 1;
    }

    // Find seat label
    static String getSeat(int col, int row) {
        char r = (row % 2 == 0) ? 'A' : 'B';
        return r + String.valueOf(col + 1);
    }

    // Allocate student
    static void allocateStudent() {

        System.out.print("Enter student name: ");
        String name = sc.nextLine();

        Student s = new Student(generateId(), name);

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {

                if (seats[i][j] == null) {

                    seats[i][j] = s;

                    System.out.println("Allocated: "
                            + s.id + " | " + s.name
                            + " | Room " + getRoom(i)
                            + " | Seat " + getSeat(j, i));

                    return;
                }
            }
        }

        waitingQueue.add(s);

        System.out.println("No seats available. Student added to waiting queue.");
        System.out.println("Student ID: " + s.id);
    }

    // Display seating arrangement
    static void displaySeating() {

        for (int room = 1; room <= 2; room++) {

            System.out.println("\nRoom " + room);

            for (int i = (room - 1) * 2;
                 i < room * 2; i++) {

                for (int j = 0; j < 3; j++) {

                    System.out.print(getSeat(j, i) + ": ");

                    if (seats[i][j] == null) {
                        System.out.print("Empty");
                    } else {
                        System.out.print(
                            seats[i][j].id + " "
                            + seats[i][j].name
                        );
                    }

                    System.out.print("   ");
                }

                System.out.println();
            }
        }
    }

    // Search student
    static void searchStudent() {

        System.out.print("Enter student ID: ");
        String id = sc.nextLine();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 3; j++) {

                Student s = seats[i][j];

                if (s != null && s.id.equalsIgnoreCase(id)) {

                    System.out.println("Student Found!");
                    System.out.println("Name: " + s.name);
                    System.out.println("ID: " + s.id);
                    System.out.println("Room: " + getRoom(i));
                    System.out.println("Seat: " + getSeat(j, i));

                    return;
                }
            }
        }

        for (Student s : waitingQueue) {

            if (s.id.equalsIgnoreCase(id)) {
                System.out.println("Student found in waiting queue.");
                System.out.println("Name: " + s.name);
                System.out.println("ID: " + s.id);
                return;
            }
        }

        System.out.println("Student " + id + " not found.");
    }

    // Display waiting queue
    static void displayWaitingQueue() {

        if (waitingQueue.isEmpty()) {
            System.out.println("Waiting queue is empty.");
            return;
        }

        System.out.println("\nWaiting Students:");

        for (Student s : waitingQueue) {
            System.out.println(s.id + " - " + s.name);
        }
    }

    // Display room connectivity graph
    static void displayRoomGraph() {

        System.out.println("\nRoom Connectivity Graph:");

        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                System.out.print(roomGraph[i][j] + " ");
            }
            System.out.println();
        }

        System.out.println("Room 1 is connected to Room 2.");
    }

    // Save data to file
    static void saveData() {

        try (PrintWriter pw =
             new PrintWriter(new FileWriter("exam_seating.txt"))) {

            pw.println("NEXT|" + nextId);

            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 3; j++) {

                    Student s = seats[i][j];

                    if (s != null) {
                        pw.println("S|" + s.id + "|" + s.name
                                + "|" + i + "|" + j);
                    }
                }
            }

            for (Student s : waitingQueue) {
                pw.println("W|" + s.id + "|" + s.name);
            }

            System.out.println("Data saved successfully.");

        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }

    // Load data from file
    static void loadData() {

        File file = new File("exam_seating.txt");

        if (!file.exists()) {
            return;
        }

        try (BufferedReader br =
             new BufferedReader(new FileReader(file))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] data = line.split("\\|", -1);

                if (data[0].equals("NEXT")) {
                    nextId = Integer.parseInt(data[1]);
                }

                else if (data[0].equals("S")) {

                    Student s = new Student(data[1], data[2]);

                    int row = Integer.parseInt(data[3]);
                    int col = Integer.parseInt(data[4]);

                    if (row >= 0 && row < 4 &&
                        col >= 0 && col < 3) {
                        seats[row][col] = s;
                    }
                }

                else if (data[0].equals("W")) {

                    Student s = new Student(data[1], data[2]);
                    waitingQueue.add(s);
                }
            }

            System.out.println("Previous data loaded successfully.");

        } catch (IOException | NumberFormatException e) {
            System.out.println("Error loading data.");
        }
    }

    // Main method
    public static void main(String[] args) {

        loadData();

        while (true) {

            System.out.println("\n===== EXAM SEATING SYSTEM =====");

            System.out.println("1. Allocate Student");
            System.out.println("2. Display Seating");
            System.out.println("3. Search Student");
            System.out.println("4. Display Waiting Queue");
            System.out.println("5. Display Room Graph");
            System.out.println("6. Save Data");
            System.out.println("7. Exit");

            System.out.print("Enter your choice: ");

            int choice;

            try {
                choice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    allocateStudent();
                    break;

                case 2:
                    displaySeating();
                    break;

                case 3:
                    searchStudent();
                    break;

                case 4:
                    displayWaitingQueue();
                    break;

                case 5:
                    displayRoomGraph();
                    break;

                case 6:
                    saveData();
                    break;

                case 7:
                    saveData();
                    System.out.println("Thank you!");
                    return;

                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}

