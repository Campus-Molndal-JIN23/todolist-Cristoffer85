package org.campusmolndal;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoManager {
    private final MongoCollection<Document> todoCollection;
    private final MongoCollection<Document> userCollection;

    public TodoManager(MongoCollection<Document> todoCollection, MongoCollection<Document> userCollection) {
        this.todoCollection = todoCollection;
        this.userCollection = userCollection;
    }

    public void createTodo(Scanner scanner) {
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

    public void readOneTodo(Scanner scanner) {
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

    public void readAllTodos() {
        System.out.println("All Todos:");
        List<Document> todoDocuments = todoCollection.find().into(new ArrayList<>());

        for (Document todoDocument : todoDocuments) {
            System.out.println("Todo ID: " + todoDocument.getInteger("_id"));
            System.out.println("Text: " + todoDocument.getString("text"));
            System.out.println("Done: " + todoDocument.getBoolean("done"));

            List<Integer> assignedToIds = todoDocument.getList("assignedTo", Integer.class);
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

    public void updateTodo(Scanner scanner) {
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

    public void deleteTodo(Scanner scanner) {
        System.out.print("Enter the ID of the Todo: ");
        int todoId = scanner.nextInt();
        scanner.nextLine();

        Document todoFilter = new Document("_id", todoId);
        todoCollection.deleteOne(todoFilter);

        Document userFilter = new Document("todos", todoId);
        Document userUpdate = new Document("$pull", new Document("todos", todoId));
        userCollection.updateMany(userFilter, userUpdate);

        System.out.println("Todo deleted successfully.");
    }

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
}
