package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.InputMismatchException;
import java.util.Scanner;

public class TodoApplication {
    TodoManager todoManager;
    UserManager userManager;
    Scanner scanner;

    public TodoApplication() {
        Connection connection = new Connection();

        MongoCollection<Document> todoCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Todos");
        MongoCollection<Document> userCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Users");

        todoManager = new TodoManager(todoCollection, userCollection);
        userManager = new UserManager(userCollection, todoCollection);

        scanner = new Scanner(System.in);         //Instantiates only one (1) scanner that is used in connecting classes
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            displayMenu();

            int choice = -1;
            boolean validChoice = false;

            while (!validChoice) {
                System.out.print("Enter your choice: ");

                String input = scanner.nextLine().trim();

                if (input.isEmpty()) {
                    System.out.println("Invalid input! Please enter a valid choice.");
                    continue;
                }

                try {
                    choice = Integer.parseInt(input);
                    validChoice = true;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input! Please enter a valid choice.");
                }
            }

            switch (choice) {
                case 1:
                    userManager.createUser(scanner);
                    break;
                case 2:
                    userManager.readUser(scanner);
                    break;
                case 3:
                    userManager.updateUser(scanner);
                    break;
                case 4:
                    userManager.deleteUser(scanner);
                    break;
                case 5:
                    todoManager.createTodo(scanner);
                    break;
                case 6:
                    todoManager.readOneTodo(scanner);
                    break;
                case 7:
                    todoManager.readAllTodos();
                    break;
                case 8:
                    todoManager.updateTodo(scanner);
                    break;
                case 9:
                    todoManager.deleteTodo(scanner);
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }

            if (exit) {
                break;
            }
        }

        scanner.close();
    }



    void displayMenu() {
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
