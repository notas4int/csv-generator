package org.example;

import org.example.models.Person;
import org.example.services.CSVWriter;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class App {
    public static void main(String[] args) throws FileNotFoundException {
        Person firstPerson = new Person("Vava", "Vavava", 23, 180);
        List<Person> persons = new ArrayList<>();
        persons.add(firstPerson);
        CSVWriter writer = new CSVWriter(false);
        writer.writeToFile(persons, "C:\\prog\\csv-generator\\src\\main\\resources\\res\\file6.csv");
    }
}
