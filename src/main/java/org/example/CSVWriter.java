package org.example;

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
    private final boolean rewritable;

    public CSVWriter(boolean rewritable) {
        this.rewritable = rewritable;
    }

    public CSVWriter() {
        rewritable = true;
    }

    public void writeToFile(List<?> objects, String filePath) throws FileNotFoundException {
        if (objects.isEmpty())
            throw new IllegalArgumentException();

        File csvOutputFile = new File(filePath);

        // проверка, если не перезаписывается и файла не существует
        if (!rewritable && !csvOutputFile.exists())
            throw new FileNotFoundException();

        Field[] fields = objects.get(0).getClass().getDeclaredFields();

        try (FileWriter pw = new FileWriter(csvOutputFile, !rewritable)) {
            // проверка добавления названия полей для перезаписи
            if (rewritable) {
                String header = Arrays.stream(fields)
                        .map(Field::getName)
                        .collect(Collectors.joining(","));
                pw.write(header);
                pw.write(System.lineSeparator());
            }

            // перебор объектов коллекции для вывода строки в файл
            for (Object obj : objects) {
                String line = Arrays.stream(fields)
                        .peek(field -> field.setAccessible(true))
                        .map(field -> {
                            try {
                                return String.valueOf(field.get(obj));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .collect(Collectors.joining(","));
                pw.write(line);
                pw.write(System.lineSeparator());
            }
        } catch (IOException ex) {
            throw new FileNotFoundException();
        }
    }
}
