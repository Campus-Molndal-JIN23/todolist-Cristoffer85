package org.campusmolndal;

import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

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
        System.out.print("Enter the text of the Todo: ");
        String text = scanner.nextLine();

        System.out.print("Enter the ID(s) of the User(s) to assign the Todo (comma-separated): ");
        String userIdsInput = scanner.nextLine();

        List<Integer> userIds = Arrays.stream(userIdsInput.split(","))                     //Integer-Lista med regex för att lägga till en todoe på tex flera användare, komma emellan
                .map(String::trim)
                .map(Integer::parseInt)
                .toList();

        int todoId = generateUniqueId(todoCollection);

        Document todoDocument = new Document("_id", todoId)
                .append("text", text)
                .append("done", false)
                .append("assignedTo", userIds);

        todoCollection.insertOne(todoDocument);

        for (int userId : userIds) {                                                            //Loopar igenom UserIds för att hitta befintlig User att assigna Todoe
            Document userFilter = new Document("_id", userId);
            Document userUpdate = new Document("$push", new Document("todos", todoId));
            userCollection.updateOne(userFilter, userUpdate);
        }

        System.out.println("Todo created successfully with ID " + todoId);
    }

    public void readOneTodo(Scanner scanner) {
        System.out.println("-------------------");
        System.out.print("Enter the Todo ID: ");
        int todoId = scanner.nextInt();

        Document todoFilter = new Document("_id", todoId);                                      //Filtrerar igenom todoe-collectionen för att läsa todoe
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

        System.out.print("Enter the updated status of the Todo (true/false): ");
        boolean updatedStatus = scanner.nextBoolean();
        scanner.nextLine();

        Document updateDocument = new Document("$set", new Document("text", updatedText).append("done", updatedStatus));
        todoCollection.updateOne(todoFilter, updateDocument);

        System.out.println("Todo updated successfully.");
    }


    public void deleteTodo(Scanner scanner) {
        System.out.println("-------------------");
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
}
