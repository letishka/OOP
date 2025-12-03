package org.example.model;

import java.util.Objects;

public class City {
    private String city;
    private String street;
    private String house;
    private int floor;

    public City() {
    }

    public City(String city, String street, String house, int floor) {
        this.city = city;
        this.street = street;
        this.house = house;
        this.floor = floor;
    }

    public String getCity() {
        return city;
    }

    public String getStreet() {
        return street;
    }

    public String getHouse() {
        return house;
    }

    public int getFloor() {
        return floor;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        City cityObj = (City) o;
        return floor == cityObj.floor &&
                Objects.equals(city, cityObj.city) &&
                Objects.equals(street, cityObj.street) &&
                Objects.equals(house, cityObj.house);
    }

    @Override
    public int hashCode() {
        int result = city != null ? city.hashCode() : 0;
        result = 31 * result + (street != null ? street.hashCode() : 0);
        result = 31 * result + (house != null ? house.hashCode() : 0);
        result = 31 * result + floor;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Город: %s, Улица: %s, Дом: %s, Этажи: %d",
                city, street, house, floor);
    }
}