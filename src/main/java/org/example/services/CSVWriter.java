package org.example.services;

import org.example.exceptions.IllegalFieldAccessException;
import org.example.exceptions.ListOfObjectsIsEmptyException;

import java.io.*;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CSVWriter {
    /**
     * Переменная перезаписи служит обозначением создания нового файла с новой информацией
     * или добавления информации в старый файл
     */
    private final boolean newable;

    public CSVWriter(boolean newable) {
        this.newable = newable;
    }

    public CSVWriter() {
        newable = true;
    }

    public void writeToFile(List<?> objects, String filePath) throws FileNotFoundException {
        if (objects.isEmpty())
            throw new ListOfObjectsIsEmptyException();

        File csvOutputFile = new File(filePath);

        // проверка, если не перезаписывается и файла не существует
        if (!newable && !csvOutputFile.exists())
            throw new FileNotFoundException();

        Field[] fields = objects.get(0).getClass().getDeclaredFields();

        try (FileWriter pw = new FileWriter(csvOutputFile, !newable)) {
            // проверка добавления названия полей для перезаписи
            writeFieldNamesToFile(fields, pw);
            // перебор объектов коллекции для вывода строки в файл
            writeEntitiesToFile(objects, fields, pw);
        } catch (IOException ex) {
            throw new FileNotFoundException();
        }
    }

    private void writeEntitiesToFile(List<?> objects, Field[] fields, FileWriter pw) throws IOException {
        for (Object obj : objects) {
            String line = Arrays.stream(fields)
                    .peek(field -> field.setAccessible(true))
                    .map(field -> {
                        try {
                            return String.valueOf(field.get(obj));
                        } catch (IllegalAccessException e) {
                            throw new IllegalFieldAccessException("Field " + field.getName() + " can't be accessed");
                        }
                    })
                    .collect(Collectors.joining(","));
            pw.write(line);
            pw.write(System.lineSeparator());
        }
    }

    private void writeFieldNamesToFile(Field[] fields, FileWriter pw) throws IOException {
        if (newable) {
            String header = Arrays.stream(fields)
                    .map(Field::getName)
                    .collect(Collectors.joining(","));
            pw.write(header);
            pw.write(System.lineSeparator());
        }
    }
}
