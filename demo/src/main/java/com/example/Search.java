package com.example;

import java.io.IOException;  
import java.net.URI;
import java.util.Scanner;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.awt.Desktop;
import com.google.gson.Gson;

public class Search {  
    private static final String base = "https://ru.wikipedia.org/wiki/";  
    private static final String api = "https://ru.wikipedia.org/w/api.php?action=query&list=search&utf8=&format=json&srsearch=";  
    private static final int timeout_sec = 15;
    private static final Gson gson = new Gson();  
  
    public static String JSONfromURL(String url){  
        System.out.println("Запрашиваем URL: " + url); // ОТЛАДКА
        
        HttpClient client = HttpClient.newHttpClient();  
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))  
                .timeout(Duration.ofSeconds(timeout_sec))
                .header("Accept", "application/json")
                .header("User-Agent", "WikiSearchBot/1.0")  
                .GET()
                .build();  
  
        HttpResponse<String> response = null;  
        try {  
            response = client.send(request, HttpResponse.BodyHandlers.ofString());  
            System.out.println("Статус ответа: " + response.statusCode()); // ОТЛАДКА
        } catch (IOException e) {  
            System.out.println("IO ошибка: " + e.getMessage());  
            return null;
        } catch (InterruptedException e) {  
            System.out.println("Прервано: " + e.getMessage());  
            return null;
        }  
  
        if (response != null && response.statusCode() >= 200 && response.statusCode() < 300) {
            return response.body();
        } else {  
            throw new RuntimeException("HTTP ошибка: " + (response != null ? response.statusCode() : "No response"));  
        }  
    }  
  
    public static void parseAndResults(String jsonResponse){  
        try {  
            System.out.println("Полученный JSON: " + jsonResponse.substring(0, Math.min(500, jsonResponse.length())) + "..."); // ОТЛАДКА
            WikiResponse response = gson.fromJson(jsonResponse, WikiResponse.class);  
            // Проверки
            if (response == null) {
                System.out.println("Response is null");
                return;
            }
            if (response.query == null) {
                System.out.println("Query is null");
                return;
            }
            if (response.query.search == null) {
                System.out.println("Search results are null");
                return;
            }
            System.out.println("Найдено результатов: " + response.query.search.size()); // ОТЛАДКА
            if (response.query.search.isEmpty()) {  
                System.out.println("Ничего не найдено");  
                return;
            }  
            // Найденные заголовки статей
            for (int i = 0; i < response.query.search.size(); i++) {
                SearchResult result = response.query.search.get(i);
                System.out.println((i+1) + ". " + result.title);
            }
            SearchResult firstResult = response.query.search.get(0);  
            String articleUrl = base + firstResult.title.replace(" ", "_");
            System.out.println("Найдена статья: " + firstResult.title);  
            System.out.println("Открываю в браузере: " + articleUrl);  
            Desktop.getDesktop().browse(new URI(articleUrl));  
        } catch(Exception e) {  
            System.out.println("Ошибка при обработке результатов: " + e.getMessage());  
            e.printStackTrace();
        }  
    }  
  
    public static void main(String[] args) { 
        System.setProperty("file.encoding", "UTF-8");
        Scanner scanner = new Scanner(System.in);  
        System.out.print("Введите поисковый запрос: ");
        String text = scanner.nextLine();  
        try {  
            String encodedText = URLEncoder.encode(text, StandardCharsets.UTF_8);  
            String apiUrl = api + encodedText;  
            String jsonResponse = JSONfromURL(apiUrl);  
            
            if (jsonResponse != null) {
                parseAndResults(jsonResponse);  
            } else {
                System.out.println("Не удалось получить данные");
            }
        } catch(Exception e){  
            System.out.println("Ошибка: " + e.getMessage());  
        } finally{  
            scanner.close(); 
        }  
    }  
    
    static class WikiResponse {  
        public Query query; 
    }  
    
    static class Query {  
        public List<SearchResult> search; 
    }  
    
    static class SearchResult {  
        public String title; 
    }  
}