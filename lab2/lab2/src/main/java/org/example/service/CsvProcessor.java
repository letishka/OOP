package org.example.service;

import org.example.model.City;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CsvProcessor implements FileProcessor {

    @Override
    public List<City> processFile(String filePath) throws IOException {
        List<City> cities = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;
            int lineNumber = 0;

            while ((line = reader.readLine()) != null) {
                lineNumber++;

                // Быстрая проверка на пустую строку
                if (line.trim().isEmpty()) continue;

                // Пропускаем заголовок
                if (isFirstLine && (line.contains("city") || line.contains("город"))) {
                    isFirstLine = false;
                    continue;
                }

                // Оптимизированный парсинг CSV с разделителем ;
                String[] fields = fastCsvSplit(line);

                if (fields.length >= 4) {
                    try {
                        // Убираем кавычки если есть
                        String city = removeQuotes(fields[0]).trim();
                        String street = removeQuotes(fields[1]).trim();
                        String house = removeQuotes(fields[2]).trim();
                        String floorStr = removeQuotes(fields[3]).trim();

                        if (!city.isEmpty() && !street.isEmpty() && !house.isEmpty()) {
                            City cityObj = new City();
                            cityObj.setCity(city);
                            cityObj.setStreet(street);
                            cityObj.setHouse(house);
                            cityObj.setFloor(Integer.parseInt(floorStr));
                            cities.add(cityObj);
                        }
                    } catch (NumberFormatException e) {
                        // Просто пропускаем некорректные строки
                    }
                }

                isFirstLine = false;
            }
        }

        return cities;
    }

    private String[] fastCsvSplit(String line) {
        // Быстрый сплит по точке с запятой
        return line.split(";", -1);
    }

    private String removeQuotes(String str) {
        if (str != null && str.length() >= 2 &&
                str.startsWith("\"") && str.endsWith("\"")) {
            return str.substring(1, str.length() - 1);
        }
        return str;
    }
}