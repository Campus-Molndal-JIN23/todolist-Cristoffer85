package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.Scanner;

public class TodoApplication {
    private final TodoManager todoManager;
    private final UserManager userManager;
    private final Scanner scanner;         //1 scanner

    public TodoApplication() {
        Connection connection = new Connection();

        MongoCollection<Document> todoCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Todos");
        MongoCollection<Document> userCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Users");

        todoManager = new TodoManager(todoCollection, userCollection);
        userManager = new UserManager(userCollection, todoCollection);

        scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            displayMenu();

            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> userManager.createUser(scanner);
                case 2 -> userManager.readUser(scanner);
                case 3 -> userManager.updateUser(scanner);
                case 4 -> userManager.deleteUser(scanner);
                case 5 -> todoManager.createTodo(scanner);
                case 6 -> todoManager.readOneTodo(scanner);
                case 7 -> todoManager.readAllTodos();
                case 8 -> todoManager.updateTodo(scanner);
                case 9 -> todoManager.deleteTodo(scanner);
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void displayMenu() {
        System.out.println(

                   "\n             ## TODO APPLICATION ##"
                 + "\n----------------------------------------------------"
                 + "\n1. Create new User      |        5. Create Todo"
                 + "\n2. Read User            |        6. Read Single Todo"
                 + "\n3. Update User          |        7. Read All Todos"
                 + "\n4. Delete User          |        8. Update Todo"
                 + "\n                        |        9. Delete Todo"
                 + "\n----------------------------------------------------"
                 + "\n                                 0. Exit");
    }
}
