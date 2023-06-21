package org.campusmolndal;

import org.bson.Document;

public class Menu {
    static MongoDBFacade mongoDBFacade;
    static int counter = 0;

    public static void showMenu() {

        mongoDBFacade = new MongoDBFacade("TODO", "Todos");

        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Create Todo");
            System.out.println("2. Read Todos");
            System.out.println("3. Update Todo");
            System.out.println("4. Delete Todo");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = ScanResource.scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createTask();
                case 2 -> readTask();
                case 3 -> updateTask();
                case 4 -> deleteTask();
                case 5 -> {
                    mongoDBFacade.close();
                    System.out.println("Exiting...");
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 5);
    }

    private static void createTask() {
        counter++; // Increment the counter
        int id = counter; // Use the current counter value as the ID

        System.out.print("Enter task text: ");
        String text = scanner.nextLine();
        System.out.print("Is the task done? (true/false): ");
        boolean done = scanner.nextBoolean();
        scanner.nextLine();
        System.out.print("Enter assigned to: ");
        String assignedTo = scanner.nextLine();

        mongoDBFacade.create(id, text, done, assignedTo);
        System.out.println("Task created successfully.");
    }

    private static void readTask() {
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();

        Document task = mongoDBFacade.read(Integer.parseInt(id));
        if (task != null) {
            System.out.println("Task details:");
            System.out.println("ID: " + task.getInteger("id"));
            System.out.println("Text: " + task.getString("text"));
            System.out.println("Done: " + task.getBoolean("done"));
            System.out.println("Assigned to: " + task.getString("assignedTo"));
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void updateTask() {
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();

        Document task = mongoDBFacade.read(Integer.parseInt(id));
        if (task != null) {
            System.out.print("Enter updated task text: ");
            String text = scanner.nextLine();
            System.out.print("Is the task done? (true/false): ");
            boolean done = scanner.nextBoolean();
            scanner.nextLine();
            System.out.print("Enter updated assigned to: ");
            String assignedTo = scanner.nextLine();

            Document updatedDocument = new Document("text", text)
                    .append("done", done)
                    .append("assignedTo", assignedTo);

            mongoDBFacade.update(Integer.parseInt(id), updatedDocument);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask() {
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();

        mongoDBFacade.delete(Integer.parseInt(id));
        System.out.println("Task deleted successfully.");
    }
}