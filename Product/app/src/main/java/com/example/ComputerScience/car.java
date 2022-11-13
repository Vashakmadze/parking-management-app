package com.example.ComputerScience;

public class car { // car class
    private String numberPlate;
    private String entranceDate;
    private String type;
    private String color;

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public String getNumberPlate() {
        return numberPlate;
    }
    public String getEntranceDate() {
        return entranceDate;
    }
    public String getColor() { return color; }
    public void setNumberPlate(String numberPlate) {
        this.numberPlate = numberPlate;
    }
    public void setEntranceDate(String entranceDate) {
        this.entranceDate = entranceDate;
    }

    public car(String numberPlate, String entranceDate, String color, String type) {
        this.numberPlate = numberPlate;
        this.entranceDate = entranceDate;
        this.color = color;
        this.type = type;
    }
    public car() {

    }
}
