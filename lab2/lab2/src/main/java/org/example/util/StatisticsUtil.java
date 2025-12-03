package org.example.util;

import org.example.model.City;

import java.util.*;
import java.util.stream.Collectors;

public class StatisticsUtil {

    public static void printDuplicateEntries(List<City> cities) {
        System.out.println("\nДУБЛИРУЮЩИЕСЯ ЗАПИСИ:");

        Map<City, Long> duplicates = cities.stream()
                .collect(Collectors.groupingBy(
                        city -> city,
                        Collectors.counting()
                ))
                .entrySet().stream()
                .filter(entry -> entry.getValue() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (duplicates.isEmpty()) {
            System.out.println("Дублирующиеся записи не найдены.");
        } else {
            duplicates.forEach((city, count) ->
                    System.out.printf("Запись: %s - повторений: %d%n", city, count)
            );
        }
    }

    public static void printFloorStatistics(List<City> cities) {
        System.out.println("\nСТАТИСТИКА ЭТАЖНОСТИ ПО ГОРОДАМ:");

        Map<String, int[]> cityFloorStats = new HashMap<>();

        for (City city : cities) {
            String cityName = city.getCity();
            int floor = city.getFloor();

            int[] floors = cityFloorStats.computeIfAbsent(cityName, k -> new int[6]);

            if (floor >= 1 && floor <= 5) {
                floors[floor]++;
            }
        }

        if (cityFloorStats.isEmpty()) {
            System.out.println("Нет данных для статистики.");
            return;
        }

        List<String> sortedCities = new ArrayList<>(cityFloorStats.keySet());
        Collections.sort(sortedCities);

        for (String city : sortedCities) {
            int[] floors = cityFloorStats.get(city);
            System.out.printf("Город: %s%n", city);
            for (int floor = 1; floor <= 5; floor++) {
                System.out.printf("  %d-этажных зданий: %d%n", floor, floors[floor]);
            }
        }
    }
}