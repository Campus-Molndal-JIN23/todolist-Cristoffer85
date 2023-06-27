package org.campusmolndal;

import java.util.NoSuchElementException;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.*;

public class TodoManager {
    MongoCollection<Document> todoCollection;
    MongoCollection<Document> userCollection;

    public TodoManager(MongoCollection<Document> todoCollection, MongoCollection<Document> userCollection) {
        this.todoCollection = todoCollection;
        this.userCollection = userCollection;
    }

    //############################ CRUDOPERATIONS ##############################

    public void createTodo(Scanner scanner) {
        System.out.println("-------------------");
        String text = null;

        while (true) {
            System.out.print("Enter the text of the Todo: ");
            text = scanner.nextLine().trim();

            if (!text.isEmpty()) {
                break;
            }

            System.out.println("Invalid input! Please enter a valid text for the Todo.");
        }

        List<Integer> userIds = new ArrayList<>();

        while (true) {
            try {
                System.out.print("Enter the ID(s) of the User(s) to assign the Todo (comma-separated): ");
                String userIdsInput = scanner.nextLine();

                userIds = Arrays.stream(userIdsInput.split(","))
                        .map(String::trim)
                        .map(Integer::parseInt)
                        .toList();

                if (!validateUserIds(userIds)) {
                    System.out.println("Invalid user ID(s)! Please enter existing user ID(s).");
                    continue;
                }

                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter valid user ID(s).");
            }
        }

        int todoId = generateUniqueId(todoCollection);

        Document todoDocument = new Document("_id", todoId)
                .append("text", text)
                .append("done", false)
                .append("assignedTo", userIds);

        todoCollection.insertOne(todoDocument);

        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", todoId));
            userCollection.updateOne(userFilter, userUpdate);
        }

        System.out.println("Todo created successfully with ID " + todoId);
    }

    private boolean isUserExists(int userId) {
        Document userFilter = new Document("_id", userId);
        return userCollection.find(userFilter).first() != null;
    }


    private boolean userExists(int userId) {
        Document userFilter = new Document("_id", userId);
        return userCollection.find(userFilter).first() != null;
    }



    public void readOneTodo(Scanner scanner) {
        System.out.println("-------------------");

        int todoId = 0;

        while (true) {
            try {
                System.out.print("Enter the Todo ID: ");
                todoId = scanner.nextInt();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid Todo ID.");
                scanner.nextLine();
            }
        }

        Document todoFilter = new Document("_id", todoId);
        Document todoDocument = todoCollection.find(todoFilter).first();

        if (todoDocument != null) {
            printTodoDetails(todoDocument);
        } else {
            System.out.println("Todo not found.");
        }
    }

    public void readAllTodos() {
        System.out.println("------------------------------");
        System.out.println("All Todos:");
        List<Document> todoDocuments = todoCollection.find().into(new ArrayList<>());

        for (Document todoDocument : todoDocuments) {
            printTodoDetails(todoDocument);
            System.out.println();
        }
    }

    public void updateTodo(Scanner scanner) {
        System.out.println("------------------");

        int todoId = 0;

        while (true) {
            try {
                System.out.print("Enter the ID of the Todo: ");
                todoId = scanner.nextInt();
                scanner.nextLine(); // Clear the newline character from the scanner's buffer
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid Todo ID.");
                scanner.nextLine(); // Clear the invalid input
            }
        }

        Document todoFilter = new Document("_id", todoId);
        Document todoDocument = todoCollection.find(todoFilter).first();

        if (todoDocument == null) {
            System.out.println("Todo not found with ID " + todoId);
            return;
        }

        System.out.print("Enter the updated text of the Todo: ");
        String updatedText = scanner.nextLine();

        boolean updatedStatus = false;

        while (true) {
            try {
                System.out.print("Enter the updated status of the Todo (true/false): ");
                updatedStatus = scanner.nextBoolean();
                scanner.nextLine();
                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter 'true' or 'false'.");
                scanner.nextLine();
            }
        }

        Document updateDocument = new Document("$set", new Document("text", updatedText).append("done", updatedStatus));
        todoCollection.updateOne(todoFilter, updateDocument);

        System.out.println("Todo updated successfully.");
    }


    public void deleteTodo(Scanner scanner) {
        System.out.println("-------------------");

        int todoId;

        while (true) {
            try {
                System.out.print("Enter the ID of the Todo: ");
                todoId = scanner.nextInt();
                scanner.nextLine();

                break;
            } catch (InputMismatchException e) {
                System.out.println("Invalid input! Please enter a valid Todo ID.");
                scanner.nextLine();
            }
        }

        Document todoFilter = new Document("_id", todoId);
        todoCollection.deleteOne(todoFilter);

        Document userFilter = new Document("todos", todoId);
        Document userUpdate = new Document("$pull", new Document("todos", todoId));
        userCollection.updateMany(userFilter, userUpdate);

        System.out.println("Todo deleted successfully.");
    }

    //################ MISC (Generate Unique ID här, printTodoDetail (refaktorering för få det lite snyggare+enklare lättläst) + getUsersByID från den andra klassen) etc... ##############################

    private int generateUniqueId(MongoCollection<Document> collection) {
        Document lastDocument = collection.find().sort(new Document("_id", -1)).limit(1).first();

        if (lastDocument != null) {
            int lastId = lastDocument.getInteger("_id");
            return lastId + 1;
        } else {
            return 1;
        }
    }

    private void printTodoDetails(Document todoDocument) {
        System.out.println("------------------------------");
        System.out.println("Todo ID: " + todoDocument.getInteger("_id"));
        System.out.println("Text: " + todoDocument.getString("text"));
        System.out.println("Done: " + todoDocument.getBoolean("done"));

        List<Integer> assignedToIds = todoDocument.get("assignedTo", List.class);
        if (assignedToIds != null && !assignedToIds.isEmpty()) {
            System.out.println("##############################");
            System.out.println("Assigned To:");
            System.out.println("##############################");

            List<User> assignedUsers = getUsersByIds(assignedToIds);
            assignedUsers.forEach(user -> {
                System.out.println("User ID: " + user.getId());
                System.out.println("User Name: " + user.getName());
                System.out.println("User Age: " + user.getAge());
                System.out.println();
            });
        } else {
            System.out.println("Assigned To: No assigned users");
        }
    }

    private List<User> getUsersByIds(List<Integer> userIds) {
        List<User> users = new ArrayList<>();

        for (Integer userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userDocument = userCollection.find(userFilter).first();

            if (userDocument != null) {
                int id = userDocument.getInteger("_id");
                String name = userDocument.getString("name");
                int age = userDocument.getInteger("age");
                users.add(new User(id, name, age));
            }
        }
        return users;
    }

    private boolean validateUserIds(List<Integer> userIds) {
        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document user = userCollection.find(userFilter).first();
            if (user == null) {
                return false;
            }
        }
        return true;
    }

}
