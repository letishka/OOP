package org.example.service;

import org.example.model.City;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class XmlProcessor implements FileProcessor {

    @Override
    public List<City> processFile(String filePath) throws Exception {
        List<City> cities = new ArrayList<>();

        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser saxParser = factory.newSAXParser();

        DefaultHandler handler = new DefaultHandler() {
            private City currentCity;
            private StringBuilder currentValue;

            @Override
            public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
                if ("item".equals(qName)) {
                    currentCity = new City();

                    // Читаем данные из атрибутов - это в 100 раз быстрее!
                    String city = attributes.getValue("city");
                    String street = attributes.getValue("street");
                    String house = attributes.getValue("house");
                    String floor = attributes.getValue("floor");

                    if (city != null && street != null && house != null && floor != null) {
                        currentCity.setCity(city);
                        currentCity.setStreet(street);
                        currentCity.setHouse(house);
                        try {
                            currentCity.setFloor(Integer.parseInt(floor));
                            cities.add(currentCity);
                        } catch (NumberFormatException e) {
                            // Пропускаем некорректные записи
                        }
                    }
                    currentCity = null;
                }
            }
        };

        saxParser.parse(new File(filePath), handler);
        return cities;
    }
}