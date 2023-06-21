package org.campusmolndal;

import org.bson.Document;

import java.util.Scanner;

public class Menu {
    private static Scanner scanner = new Scanner(System.in);
    private static MongoDBFacade mongoDBFacade;

    public static void showMenu() {
        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Create Todo");
            System.out.println("2. Read Todos");
            System.out.println("3. Update Todo");
            System.out.println("4. Delete Todo");
            System.out.println("5. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
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
        System.out.print("Enter task ID: ");
        int id = scanner.nextInt();
        scanner.nextLine();
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

        Document task = mongoDBFacade.read(id);
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

        Document task = mongoDBFacade.read(id);
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

            mongoDBFacade.update(id, updatedDocument);
            System.out.println("Task updated successfully.");
        } else {
            System.out.println("Task not found.");
        }
    }

    private static void deleteTask() {
        System.out.print("Enter task ID: ");
        String id = scanner.nextLine();

        mongoDBFacade.delete(id);
        System.out.println("Task deleted successfully.");
    }
}