package org.campusmolndal;

import org.bson.Document;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private static Scanner scanner = new Scanner(System.in);
    static MongoDBFacade mongoDBFacade;
    static int counter = 0;

    public static void showMenu() {

        mongoDBFacade = new MongoDBFacade("TODO", "Todos");

        int choice;
        do {
            System.out.print(
                    "\nMenu:"
                            + "\n 1. Create Todo"
                            + "\n 2. Read Todos"
                            + "\n 3. Update Todo"
                            + "\n 4. Delete Todo"
                            + "\n 5. Exit");

            System.out.println("\nEnter your choice: ");
            System.out.println("-------------------");

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
        counter++; // Increment the counter
        int id = counter; // Use the current counter value as the ID

        System.out.print("Enter Todo text: ");
        String text = scanner.nextLine();

        System.out.print("Is the Todo done? (true/false): ");
        boolean done = scanner.nextBoolean();
        scanner.nextLine();

        System.out.print("Todo assigned to: ");
        String assignedTo = scanner.nextLine();

        mongoDBFacade.create(id, text, done, assignedTo);
        System.out.println("Todo created successfully.");
        System.out.println("-------------------");
    }

    private static void readTask() {
        int subChoice;
        do {
            System.out.print(
                    "\n 1. Read one specific todo"
                            + "\n 2. Read all todos"
                            + "\n 3. Back to main menu");

            System.out.println("\nEnter your choice: ");
            System.out.println("-------------------");

            subChoice = scanner.nextInt();
            scanner.nextLine();

            switch (subChoice) {
                case 1:
                    System.out.print("Enter Todo ID: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    Document taskDocument = mongoDBFacade.read(id).stream().findFirst().orElse(null);
                    if (taskDocument != null) {
                        System.out.println("Todo details:");
                        System.out.println("ID: " + taskDocument.getInteger("id"));
                        System.out.println("Text: " + taskDocument.getString("text"));
                        System.out.println("Done: " + taskDocument.getBoolean("done"));
                        System.out.println("Assigned to: " + taskDocument.getString("assignedTo"));
                        System.out.println("-------------------------");
                    } else {
                        System.out.println("Todo not found.");
                        System.out.println("-------------------");
                    }
                    break;

                case 2:
                    List<Document> todos = mongoDBFacade.read(-1);
                    if (!todos.isEmpty()) {
                        System.out.println("All Todos:");
                        for (Document task : todos) {
                            System.out.println("ID: " + task.getInteger("id"));
                            System.out.println("Text: " + task.getString("text"));
                            System.out.println("Done: " + task.getBoolean("done"));
                            System.out.println("Assigned to: " + task.getString("assignedTo"));
                            System.out.println("-------------------------");
                        }
                    } else {
                        System.out.println("No Todos found.");
                        System.out.println("-------------------------");
                    }
                    break;

                case 3:
                    System.out.println("Returning to the main menu...");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        } while (subChoice != 3);
    }




    private static void updateTask() {
        System.out.print("Enter Todo ID: ");
        String id = scanner.nextLine();

        List<Document> tasks = mongoDBFacade.read(Integer.parseInt(id));
        if (!tasks.isEmpty()) {
            Document task = tasks.get(0); // Get the first document from the list

            System.out.print("Enter updated Todo text: ");
            String text = scanner.nextLine();

            System.out.print("Is the Todo done? (true/false): ");
            boolean done = scanner.nextBoolean();
            scanner.nextLine();

            System.out.print("Enter updated assigned to: ");
            String assignedTo = scanner.nextLine();

            Document updatedDocument = new Document("text", text)
                    .append("done", done)
                    .append("assignedTo", assignedTo);

            mongoDBFacade.update(Integer.parseInt(id), updatedDocument);
            System.out.println("Todo updated successfully.");
        } else {
            System.out.println("Todo not found.");
        }
    }


    private static void deleteTask() {
        System.out.print("Enter Todo ID: ");
        String id = scanner.nextLine();

        mongoDBFacade.delete(Integer.parseInt(id));
        System.out.println("Todo deleted successfully.");
    }
}