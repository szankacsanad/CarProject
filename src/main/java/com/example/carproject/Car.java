package com.example.carproject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Car {
    private String licensePlateNumber;
    private String name;
    private int year;

    public Car(String licensePlateNumber, String name, int year){
        this.licensePlateNumber= licensePlateNumber;
        this.name= name;
        this.year= year;
    }

    public String getLicensePlateNumber() {
        return licensePlateNumber;
    }

    public int getYear() {
        return year;
    }

    public String getName() {
        return name;
    }

    public static void saveToFile(String fileName, ObservableList<Car> cars) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
        for (Car car : cars) {
            //System.out.println(car.licensePlateNumber+","+car.name+","+ car.year);
            bw.write(car.licensePlateNumber+","+car.name+","+ car.year);
            bw.newLine();
        }
        bw.close();
    }

    public static ObservableList<Car> readFromFile(String fileName) throws IOException {
        Scanner s = new Scanner(new File(fileName));
        ObservableList<Car> cars = FXCollections.observableArrayList();
        while(s.hasNextLine()){
            String[] parts= s.nextLine().split(",");
            cars.add(new Car(parts[0],parts[1],Integer.parseInt(parts[2])));
        }
        s.close();
        return cars;
    }
}
