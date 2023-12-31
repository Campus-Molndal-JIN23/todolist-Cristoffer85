package org.campusmolndal;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String name;
    private int age;
    private List<Todo> todos;

    public User(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.todos = new ArrayList<>();
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<Todo> getTodos() {
        return todos;
    }

    public void addTodoToUser(Todo todo) {
        this.todos.add(todo);
    }
}