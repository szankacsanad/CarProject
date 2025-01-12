package com.example.carproject;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.control.cell.*;
import javafx.scene.layout.*;

import java.io.IOException;
import java.time.Year;
import java.util.*;

public class HelloApplication extends Application {
    ObservableList<Car> cars = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws IOException {
        // tábla létrehozása
        TableView<Car> table = new TableView();

        TableColumn<Car, String> modelColumn = new TableColumn<>("LicensePlateNumber");
        modelColumn.setCellValueFactory(new PropertyValueFactory<>("licensePlateNumber"));

        TableColumn<Car, String> manufacturerColumn = new TableColumn<>("Name");
        manufacturerColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Car, Integer> priceColumn = new TableColumn<>("Year");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("year"));

        table.getColumns().addAll(modelColumn, manufacturerColumn, priceColumn);

        try {
            cars = Car.readFromFile("cars.csv");
        } catch (Exception e) {}
        table.setItems(cars);

        // új autó hozzáadása
        TextField licensePlateNumberInput = new TextField();
        licensePlateNumberInput.setPromptText("LicensePlateNumber");

        TextField nameInput = new TextField();
        nameInput.setPromptText("Name");

        TextField yearInput = new TextField();
        yearInput.setPromptText("Year");

        Button addButton = new Button("Add Car");
        int currentYear = Year.now().getValue();
        addButton.setOnAction(e -> {
            String licensePlateNumber = licensePlateNumberInput.getText();
            String name = nameInput.getText();
            int year;

            try {
                year = Integer.parseInt(yearInput.getText());
            } catch (Exception exception) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Hiba");
                a.setContentText("Nem sikerült átalakítani az évet számmá!");
                a.showAndWait();
                return;
            }

            if (year < 1950 || year > currentYear) {
                Alert a = new Alert(Alert.AlertType.ERROR);
                a.setTitle("Hiba");
                a.setContentText("Az évnek 1950 és a jelenlegi év között kell lennie!");
                a.showAndWait();
                return;
            }

            cars.add(new Car(licensePlateNumber, name, year));

            licensePlateNumberInput.clear();
            nameInput.clear();
            yearInput.clear();
        });

        // mentés betöltés
        Button saveButton = new Button("Save");
        saveButton.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Biztos?");
            a.setContentText("Biztos vagy benne? Felül fogod írni a cars.csv fájlt");
            Optional<ButtonType> result = a.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    Car.saveToFile("cars.csv", cars);
                } catch (Exception exception) {
                    Alert ea = new Alert(Alert.AlertType.ERROR);
                    ea.setContentText("Nem sikerült a fájlba írás");
                    ea.showAndWait();
                }
            }
        });

        Button loadButton = new Button("Load");
        loadButton.setOnAction(e -> {
            Alert a = new Alert(Alert.AlertType.CONFIRMATION);
            a.setTitle("Biztos?");
            a.setContentText("Biztos vagy benne? A nem elmentett autók el fognak veszni");
            Optional<ButtonType> result = a.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    ObservableList<Car> cars1 = Car.readFromFile("cars.csv");
                    cars.clear();
                    cars.addAll(cars1);
                } catch (Exception exception) {
                    Alert ea = new Alert(Alert.AlertType.ERROR);
                    ea.setContentText("Nem sikerült a fájlból olvasás");
                    ea.showAndWait();
                }
            }
        });

        HBox inputLayout = new HBox(licensePlateNumberInput, nameInput, yearInput, addButton);
        HBox saveLoadLayout = new HBox(saveButton, loadButton);
        VBox vbox = new VBox(table, inputLayout, saveLoadLayout);
        Scene scene = new Scene(vbox, 600, 400);
        stage.setScene(scene);

        stage.setTitle("Hello!");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}