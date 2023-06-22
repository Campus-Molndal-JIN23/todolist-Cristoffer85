package org.campusmolndal;

public class Todo {
    private int id;
    private String text;
    private boolean done;
    private User assignedTo;

    public Todo(int id, String text) {
        this.id = id;
        this.text = text;
        this.done = false;
        this.assignedTo = null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void assignToUser(User user) {
        this.assignedTo = user;
        user.addTodoToUser(this);
    }
}