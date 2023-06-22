package org.campusmolndal;

import java.util.Scanner;

public class InputScanner {
    private Scanner scanner;

    public InputScanner() {
        scanner = new Scanner(System.in);
    }
    public int readInt() {
        return scanner.nextInt();
    }

    public double readDouble() {
        return scanner.nextDouble();
    }

    public String readString() {
        return scanner.nextLine();
    }

    public boolean readBoolean() {
        return scanner.nextBoolean();
    }

    public void close() {
        scanner.close();
    }
}

