package org.campusmolndal;

public class Todo {
    private int id;
    private String text;
    private boolean done;
    private int assignedTo;

    public Todo() {
    }

    public Todo(int id, String text, boolean done, int assignedTo) {
        this.id = id;
        this.text = text;
        this.done = done;
        this.assignedTo = assignedTo;
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

    public int getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(int assignedTo) {
        this.assignedTo = assignedTo;
    }
}
