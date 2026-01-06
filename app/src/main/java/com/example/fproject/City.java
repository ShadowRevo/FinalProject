package com.example.fproject;

public class City {
    private String name;
    private int imageRes;

    public City(String name, int imageRes) {
        this.name = name;
        this.imageRes = imageRes;
    }

    public String getName() { return name; }
    public int getImageRes() { return imageRes; }
}
