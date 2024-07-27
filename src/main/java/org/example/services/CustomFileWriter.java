package org.example.services;

import org.example.exceptions.IllegalFieldAccessException;
import org.example.exceptions.ListOfObjectsIsEmptyException;

import java.io.FileNotFoundException;
import java.util.List;

public interface CustomFileWriter {
    void writeToFile(List<?> objects, String filePath) throws FileNotFoundException, ListOfObjectsIsEmptyException, IllegalFieldAccessException;
}
