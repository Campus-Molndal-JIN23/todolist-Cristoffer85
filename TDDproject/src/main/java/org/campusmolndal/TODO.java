package org.campusmolndal;

import org.bson.Document;

import java.util.Scanner;

public class TODO {
    public static void addTodo() {                      //Lägger till en Kund.
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ange namn på kund: ");
        String namn = scanner.nextLine();
        System.out.print("Ange kundnummer för kund: ");
        String kundnummer = scanner.nextLine();
        System.out.print("Ange ålder på kund: ");
        String ålder = scanner.nextLine();
        System.out.print("Ange kunds adress: ");
        String adress = scanner.nextLine();
        Document customer = new Document();
        customer.append("Namn", namn);
        customer.append("Kundnummer", kundnummer);
        customer.append("Ålder", ålder);
        customer.append("Adress", adress);
        MongoDBFacade.create(new Document());
        System.out.println("Kund tillagd.");
    }
}
