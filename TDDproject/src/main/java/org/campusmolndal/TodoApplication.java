package org.campusmolndal;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApplication {
    Connection connection;
    MongoCollection<Document> todoCollection;
    MongoCollection<Document> userCollection;
    Scanner scanner;

    public TodoApplication() {
        connection = new Connection();
        todoCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Todos");
        userCollection = connection.mongoClient.getDatabase("TODOAPP").getCollection("Users");
        scanner = new Scanner(System.in);
    }

    public void run() {
        boolean exit = false;

        while (!exit) {
            System.out.println(

             "\n             ## TODO APPLICATION ##"
            +"\n----------------------------------------------------"
            +"\n1. Create new User      |        5. Create Todo"
            +"\n2. Read User            |        6. Read Single Todo"
            +"\n3. Update User          |        7. Read All Todos"
            +"\n4. Delete User          |        8. Update Todo"
            +"\n                        |        9. Delete Todo"
            +"\n----------------------------------------------------"
            +"\n                                 0. Exit");

            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> createUser();
                case 2 -> readUser();
                case 3 -> updateUser();
                case 4 -> deleteUser();
                case 5 -> createTodo();
                case 6 -> readOneTodo();
                case 7 -> readAllTodos();
                case 8 -> updateTodo();
                case 9 -> deleteTodo();
                case 0 -> exit = true;
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
        scanner.close();
    }

    //############################### USERS ########################################

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
        scanner.nextLine();

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

    //############################### TODOS ########################################

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
                .append("done", false)
                .append("assignedTo", userIds); // Add assignedTo field with userIds

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

            List<Integer> assignedToIds = todoDocument.getList("assignedTo", Integer.class);      //----Här någonstans emellan felet med att todoo inte visas angiven användare på

            if (assignedToIds != null && !assignedToIds.isEmpty()) {
                System.out.println("Assigned To:");
                for (int userId : assignedToIds) {
                    System.out.println("User ID: " + userId);                                          //----Endpoint fel
                }
            } else {
                System.out.println("Assigned To: No assigned users");
            }

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
            if (assignedToIds != null && !assignedToIds.isEmpty()) {
                List<User> assignedUsers = getUsersByIds(assignedToIds);
                System.out.println("Assigned To:");
                for (User user : assignedUsers) {
                    System.out.println("User ID: " + user.getId());
                    System.out.println("User Name: " + user.getName());
                    System.out.println("User Age: " + user.getAge());
                    System.out.println();
                }
            } else {
                System.out.println("Assigned To: No assigned users");
            }
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

    //############################### MISC ########################################

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

    private List<User> getUsersByIds(List<ObjectId> userIds) {
        List<User> users = new ArrayList<>();

        for (ObjectId userId : userIds) {
            Document userFilter = new Document("_id", userId);
            Document userDocument = userCollection.find(userFilter).first();
            if (userDocument != null) {
                User user = new User(
                        userDocument.getInteger("_id"),
                        userDocument.getString("name"),
                        userDocument.getInteger("age")
                );
                users.add(user);
            }
        }
        return users;
    }
}
