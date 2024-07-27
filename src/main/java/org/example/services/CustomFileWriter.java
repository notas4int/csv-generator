package org.example.services;

import java.io.FileNotFoundException;
import java.util.List;

public interface CustomFileWriter {
    void writeToFile(List<?> objects, String filePath) throws FileNotFoundException;
}
