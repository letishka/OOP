package org.example.service;

import org.example.model.City;
import java.util.List;

public interface FileProcessor {
    List<City> processFile(String filePath) throws Exception;
}