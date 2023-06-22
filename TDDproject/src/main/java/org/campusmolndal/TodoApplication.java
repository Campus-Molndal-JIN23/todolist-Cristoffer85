package org.campusmolndal;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApplication {
    private Connection connection;
    private MongoCollection<Document> todoCollection;
    private MongoCollection<Document> userCollection;
    private Scanner scanner;

    public TodoApplication() {
        connection = new Connection();
        todoCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Todos");
        userCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Users");
        scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            System.out.println("\nTODO APPLICATION");
            System.out.println("----------------");
            System.out.println("1. Create new User");
            System.out.println("2. Read User");
            System.out.println("3. Update User");
            System.out.println("4. Delete User");
            System.out.println("----------------");
            System.out.println("5. Create Todo");
            System.out.println("6. Read Single Todo");
            System.out.println("7. Read All Todos");
            System.out.println("8. Update Todo");
            System.out.println("9. Delete Todo");
            System.out.println("----------------");
            System.out.println("0. Exit");
            System.out.println("----------------");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    createUser();
                    break;
                case 2:
                    readUser();
                    break;
                case 3:
                    updateUser();
                    break;
                case 4:
                    deleteUser();
                    break;
                case 5:
                    createTodo();
                    break;
                case 6:
                    readOneTodo();
                    break;
                case 7:
                    readAllTodos();
                    break;
                case 8:
                    updateTodo();
                    break;
                case 9:
                    deleteTodo();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    private void createTodo() {
        System.out.print("Enter the text of the Todo: ");
        String text = scanner.nextLine();

        System.out.print("Enter the ID(s) of the User(s) to assign the Todo (comma-separated): ");
        String userIdsInput = scanner.nextLine();
        List<Integer> userIds = new ArrayList<>();
        for (String userId : userIdsInput.split(",")) {
            userIds.add(Integer.parseInt(userId.trim()));
        }

        int todoId = generateUniqueId(todoCollection);
        Document todoDocument = new Document("_id", todoId)
                .append("text", text)
                .append("done", false);

        todoCollection.insertOne(todoDocument);

        for (int userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", todoId));
            userCollection.updateOne(userFilter, userUpdate);
        }

        System.out.println("Todo created successfully with ID " + todoId);
    }



    private void readOneTodo() {
        System.out.print("Enter the Todo ID: ");
        int todoId = scanner.nextInt();

        Document todoFilter = new Document("_id", todoId);
        Document todoDocument = todoCollection.find(todoFilter).first();

        if (todoDocument != null) {
            System.out.println("Todo ID: " + todoDocument.getInteger("_id"));
            System.out.println("Text: " + todoDocument.getString("text"));
            System.out.println("Done: " + todoDocument.getBoolean("done"));

            List<ObjectId> assignedToIds = todoDocument.getList("assignedTo", ObjectId.class);
            List<Integer> assignedToUserIds = getUserIds(assignedToIds);
            System.out.println("Assigned To: " + assignedToUserIds);
        } else {
            System.out.println("Todo not found.");
        }
    }


    private void readAllTodos() {
            System.out.println("All Todos:");
            List<Document> todoDocuments = todoCollection.find().into(new ArrayList<>());

            for (Document todoDocument : todoDocuments) {
                System.out.println("Todo ID: " + todoDocument.getInteger("_id"));
                System.out.println("Text: " + todoDocument.getString("text"));
                System.out.println("Done: " + todoDocument.getBoolean("done"));

                List<ObjectId> assignedToIds = todoDocument.getList("assignedTo", ObjectId.class);
                List<Integer> assignedToUserIds = getUserIds(assignedToIds);
                System.out.println("Assigned To: " + assignedToUserIds);

                System.out.println();
            }
        }

        private void updateTodo() {
        System.out.print("Enter the ID of the Todo: ");
        int todoId = scanner.nextInt();
        scanner.nextLine();

        Document todoFilter = new Document("_id", todoId);
        Document todoDocument = todoCollection.find(todoFilter).first();
        if (todoDocument == null) {
            System.out.println("Todo not found with ID " + todoId);
            return;
        }

        System.out.print("Enter the updated text of the Todo: ");
        String updatedText = scanner.nextLine();

        Document updateDocument = new Document("$set", new Document("text", updatedText));
        todoCollection.updateOne(todoFilter, updateDocument);

        System.out.println("Todo updated successfully.");
    }

    private void deleteTodo() {
        System.out.print("Enter the ID of the Todo: ");
        int todoId = scanner.nextInt();
        scanner.nextLine();

        Document todoFilter = new Document("_id", todoId);
        todoCollection.deleteOne(todoFilter);

        System.out.println("Todo deleted successfully.");
    }

    private void createUser() {
        System.out.print("Enter the name of the User: ");
        String name = scanner.nextLine();

        System.out.print("Enter the age of the User: ");
        int age = scanner.nextInt();
        scanner.nextLine();

        int userId = generateUniqueId(userCollection);
        Document userDocument = new Document("_id", userId)
                .append("name", name)
                .append("age", age)
                .append("todos", new ArrayList<Integer>());

        userCollection.insertOne(userDocument);

        System.out.println("User created successfully with ID " + userId);
    }

    private void readUser() {
        System.out.print("Enter the ID of the User: ");
        int userId = scanner.nextInt();
        scanner.nextLine(); // consume the newline character

        Document userFilter = new Document("_id", userId);
        Document userDocument = userCollection.find(userFilter).first();
        if (userDocument == null) {
            System.out.println("User not found with ID " + userId);
            return;
        }

        List<Integer> todoIds = userDocument.getList("todos", Integer.class);
        List<Document> todoDocuments = getTodoDocuments(todoIds);

        System.out.println("User ID: " + userId);
        System.out.println("Name: " + userDocument.getString("name"));
        System.out.println("Age: " + userDocument.getInteger("age"));
        System.out.println("Todos:");

        if (todoDocuments.isEmpty()) {
            System.out.println("No Todos assigned to this user.");
        } else {
            for (Document todoDocument : todoDocuments) {
                System.out.println("Todo ID: " + todoDocument.getInteger("_id"));
                System.out.println("Text: " + todoDocument.getString("text"));
                System.out.println("Done: " + todoDocument.getBoolean("done"));
                System.out.println();
            }
        }
    }


    private void updateUser() {
        System.out.print("Enter the ID of the User: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Document userFilter = new Document("_id", userId);
        Document userDocument = userCollection.find(userFilter).first();
        if (userDocument == null) {
            System.out.println("User not found with ID " + userId);
            return;
        }

        System.out.print("Enter the updated name of the User: ");
        String updatedName = scanner.nextLine();

        Document updateDocument = new Document("$set", new Document("name", updatedName));
        userCollection.updateOne(userFilter, updateDocument);

        System.out.println("User updated successfully.");
    }

    private void deleteUser() {
        System.out.print("Enter the ID of the User: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Document userFilter = new Document("_id", userId);
        userCollection.deleteOne(userFilter);

        System.out.println("User deleted successfully.");
    }


    //####################################################################################

    private int generateUniqueId(MongoCollection<Document> collection) {
        BasicDBObject sortQuery = new BasicDBObject("_id", -1);
        Document lastDocument = collection.find().sort(sortQuery).limit(1).first();
        if (lastDocument != null) {
            int lastId = lastDocument.getInteger("_id");
            return lastId + 1;
        } else {
            return 1;
        }
    }

    private List<Document> getTodoDocuments(List<Integer> todoIds) {
        List<Document> todoDocuments = new ArrayList<>();

        for (int todoId : todoIds) {
            Document todoFilter = new Document("_id", todoId);
            Document todoDocument = todoCollection.find(todoFilter).first();
            if (todoDocument != null) {
                todoDocuments.add(todoDocument);
            }
        }

        return todoDocuments;
    }

    private List<Integer> getUserIds(List<ObjectId> userObjectIds) {
        List<Integer> userIds = new ArrayList<>();

        for (ObjectId userObjectId : userObjectIds) {
            Document userFilter = new Document("_id", userObjectId);
            Document userDocument = userCollection.find(userFilter).first();
            if (userDocument != null) {
                userIds.add(userDocument.getInteger("_id"));
            }
        }

        return userIds;
    }
}
