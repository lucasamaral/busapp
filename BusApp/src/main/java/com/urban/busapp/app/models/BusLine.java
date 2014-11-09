package com.urban.busapp.app.models;


public class BusLine {
    private String number;
    private String name;
    private String start;
    private String end;


    public BusLine(String number, String name, String start, String end) {
        this.number = number;
        this.name = name;
        this.start = start;
        this.end = end;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String lineInfo(){
        return getNumber() + " - " + getName();
    }

    @Override
    public String toString() {
        return "BusLine{" +
                "number='" + number + '\'' +
                ", name='" + name + '\'' +
                ", start='" + start + '\'' +
                ", end='" + end + '\'' +
                '}';
    }
}