package org.example;

import org.example.model.City;
import org.example.service.CsvProcessor;
import org.example.service.FileProcessor;
import org.example.service.XmlProcessor;
import org.example.util.StatisticsUtil;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final String EXIT_COMMAND = "exit";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("ПРИЛОЖЕНИЕ ДЛЯ РАБОТЫ С CSV И XML ФАЙЛАМИ");
        System.out.printf("Для выхода введите '%s'%n%n", EXIT_COMMAND);

        while (true) {
            System.out.print("Введите путь до файла-справочника: ");
            String filePath = scanner.nextLine().trim();

            if (EXIT_COMMAND.equalsIgnoreCase(filePath)) {
                System.out.println("Завершение работы приложения...");
                break;
            }

            if (filePath.isEmpty()) {
                System.out.println("Путь не может быть пустым. Попробуйте снова.");
                continue;
            }

            processFile(filePath);
            System.out.println("\n" + "=".repeat(50) + "\n");
        }

        scanner.close();
    }

    private static void processFile(String filePath) {
        long totalStartTime = System.currentTimeMillis();

        try {
            FileProcessor processor = getFileProcessor(filePath);
            List<City> cities = processor.processFile(filePath);

            long totalTime = System.currentTimeMillis() - totalStartTime;

            // Только три пункта по заданию
            StatisticsUtil.printDuplicateEntries(cities);
            StatisticsUtil.printFloorStatistics(cities);
            System.out.printf("Время обработки файла: %d мс%n", totalTime);

        } catch (Exception e) {
            System.out.printf("Ошибка при обработке файла: %s%n", e.getMessage());
        }
    }

    private static FileProcessor getFileProcessor(String filePath) {
        if (filePath.toLowerCase().endsWith(".csv")) {
            return new CsvProcessor();
        } else if (filePath.toLowerCase().endsWith(".xml")) {
            return new XmlProcessor();
        } else {
            throw new IllegalArgumentException("Неподдерживаемый формат файла. Используйте .csv или .xml");
        }
    }
}