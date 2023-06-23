package org.campusmolndal;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserManager {
    private final MongoCollection<Document> userCollection;
    private final MongoCollection<Document> todoCollection;

    public UserManager(MongoCollection<Document> userCollection, MongoCollection<Document> todoCollection) {
        this.userCollection = userCollection;
        this.todoCollection = todoCollection;
    }

    //############################ CRUDOPERATIONS ##############################

    public void createUser(Scanner scanner) {
        System.out.println("-------------------");
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

    public void readUser(Scanner scanner) {
        System.out.println("-------------------");
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
        System.out.println("##############################");
        System.out.println("Todos:");
        System.out.println("##############################");
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

    public void updateUser(Scanner scanner) {
        System.out.println("-------------------");
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

    public void deleteUser(Scanner scanner) {
        System.out.println("-------------------");
        System.out.print("Enter the ID of the User: ");
        int userId = scanner.nextInt();
        scanner.nextLine();

        Document userFilter = new Document("_id", userId);
        userCollection.deleteOne(userFilter);

        System.out.println("User deleted successfully.");
    }

    //############################ MISC (Generate Unique ID här, getTodoDocuments från den andra klassen) etc... ##############################

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
}