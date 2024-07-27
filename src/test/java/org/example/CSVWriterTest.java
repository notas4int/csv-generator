package org.example;

import org.example.models.Person;
import org.example.services.CSVWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.lang.reflect.Field;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class CSVWriterTest {
    private CSVWriter csvWriter;
    private final String FILE_PATH = "C:\\prog\\csv-generator\\src\\main\\java\\org\\example\\res\\test-file.csv";
    private final File file = new File(FILE_PATH);

    @BeforeEach
    void setUp() {
        csvWriter = new CSVWriter();
    }

    @AfterEach
    void cleanUpFile() {
        file.delete();
    }

    @Test
    void writeToFile_SuccessfulSerialized() throws FileNotFoundException {
        List<Person> persons = new ArrayList<>();
        Person firstPerson = new Person("Vava", "Vavava", 23, 180);
        Person secondPerson = new Person("Lala", "Lalala", 24, 181);
        persons.add(firstPerson);
        persons.add(secondPerson);
        csvWriter.writeToFile(persons, FILE_PATH);

        try (Scanner console = new Scanner(file)) {
            String[] exceptedFieldsNameStrArr = console.nextLine().split(",");

            Field[] fields = firstPerson.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++)
                assertEquals(fields[i].getName(), exceptedFieldsNameStrArr[i]);

            String[] fieldsStrArr;
            Person currentPerson;
            int i = 0;

            while (console.hasNextLine()) {
                fieldsStrArr = console.nextLine().split(",");

                currentPerson = new Person(fieldsStrArr[0], fieldsStrArr[1],
                        Integer.parseInt(fieldsStrArr[2]), Integer.parseInt(fieldsStrArr[3]));
                assertEquals(persons.get(i), currentPerson);
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeToFile_WithEmptyListObjects() {
        assertThrows(IllegalArgumentException.class, () -> csvWriter.writeToFile(Collections.emptyList(), FILE_PATH));
    }

    @Test
    void writeToFile_WithAddingObjects() throws FileNotFoundException {
        List<Person> persons = new ArrayList<>();
        Person firstPerson = new Person("Vava", "Vavava", 23, 180);
        Person secondPerson = new Person("Lala", "Lalala", 24, 181);
        Person thirdPerson = new Person("Zaza", "Zazaza", 25, 182);
        Person fourthPerson = new Person("Mama", "Mamama", 26, 183);

        persons.add(firstPerson);
        persons.add(secondPerson);
        persons.add(thirdPerson);
        persons.add(fourthPerson);

        csvWriter.writeToFile(List.of(firstPerson, secondPerson), FILE_PATH);

        csvWriter = new CSVWriter(false);

        csvWriter.writeToFile(List.of(thirdPerson, fourthPerson), FILE_PATH);

        try (Scanner console = new Scanner(file)) {
            String[] exceptedFieldsNameStrArr = console.nextLine().split(",");

            Field[] fields = firstPerson.getClass().getDeclaredFields();

            for (int i = 0; i < fields.length; i++)
                assertEquals(fields[i].getName(), exceptedFieldsNameStrArr[i]);

            String[] fieldsStrArr;
            Person currentPerson;
            int i = 0;

            while (console.hasNextLine()) {
                fieldsStrArr = console.nextLine().split(",");

                currentPerson = new Person(fieldsStrArr[0], fieldsStrArr[1],
                        Integer.parseInt(fieldsStrArr[2]), Integer.parseInt(fieldsStrArr[3]));
                assertEquals(persons.get(i), currentPerson);
                i++;
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void writeToFile_WithAddingObjects_WithoutFile() {
        csvWriter = new CSVWriter(false);
        Person firstPerson = new Person("Vava", "Vavava", 23, 180);
        Person secondPerson = new Person("Lala", "Lalala", 24, 181);
        assertThrows(FileNotFoundException.class, () -> csvWriter.writeToFile(List.of(firstPerson, secondPerson), FILE_PATH));
    }
}