package com.customer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.util.Optional;

public class Main extends Application {

    // ObservableList of customers
    private final ObservableList<Customer> customers =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        // -----------------------------
        // Title
        // -----------------------------
        Label title = new Label("Customer Manager");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        // -----------------------------
        // Name field
        // -----------------------------
        Label nameLabel = new Label("Name:");

        TextField nameField = new TextField();
        nameField.setPromptText("Enter customer name");

        // -----------------------------
        // Province list
        // -----------------------------
        Label provinceLabel = new Label("Province:");

        ComboBox<String> provinceComboBox = new ComboBox<>();

        provinceComboBox.getItems().addAll(
                "Central",
                "Copperbelt",
                "Eastern",
                "Luapula",
                "Lusaka",
                "Muchinga",
                "Northern",
                "North-Western",
                "Southern",
                "Western"
        );

        provinceComboBox.setPromptText("Select province");

        // -----------------------------
        // Buttons
        // -----------------------------
        Button addButton = new Button("Add Customer");
        Button deleteButton = new Button("Delete Customer");

        // -----------------------------
        // TableView
        // -----------------------------
        TableView<Customer> tableView = new TableView<>();

        TableColumn<Customer, String> nameColumn =
                new TableColumn<>("Name");

        nameColumn.setCellValueFactory(
                new PropertyValueFactory<>("name")
        );

        TableColumn<Customer, String> provinceColumn =
                new TableColumn<>("Province");

        provinceColumn.setCellValueFactory(
                new PropertyValueFactory<>("province")
        );

        tableView.getColumns().addAll(
                nameColumn,
                provinceColumn
        );

        tableView.setItems(customers);

        // Make columns resize nicely
        nameColumn.prefWidthProperty()
                .bind(tableView.widthProperty().multiply(0.5));

        provinceColumn.prefWidthProperty()
                .bind(tableView.widthProperty().multiply(0.5));

        // -----------------------------
        // Add customer
        // -----------------------------
        addButton.setOnAction(event -> {

            String name = nameField.getText().trim();
            String province = provinceComboBox.getValue();

            // Validate name
            if (name.isEmpty()) {

                showError(
                        "Invalid Input",
                        "Please enter the customer's name."
                );

                nameField.requestFocus();
                return;
            }

            // Validate province
            if (province == null) {

                showError(
                        "Invalid Input",
                        "Please select a province."
                );

                provinceComboBox.requestFocus();
                return;
            }

            // Add customer
            Customer customer =
                    new Customer(name, province);

            customers.add(customer);

            // Clear form
            nameField.clear();
            provinceComboBox.getSelectionModel().clearSelection();

            nameField.requestFocus();
        });

        // -----------------------------
        // Delete customer
        // -----------------------------
        deleteButton.setOnAction(event -> {

            Customer selectedCustomer =
                    tableView.getSelectionModel().getSelectedItem();

            if (selectedCustomer == null) {

                showError(
                        "No Customer Selected",
                        "Please select a customer to delete."
                );

                return;
            }

            // Confirmation dialog
            Alert confirmation = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            confirmation.setTitle("Confirm Deletion");
            confirmation.setHeaderText("Delete Customer?");
            confirmation.setContentText(
                    "Are you sure you want to delete "
                            + selectedCustomer.getName()
                            + "?"
            );

            Optional<ButtonType> result =
                    confirmation.showAndWait();

            if (result.isPresent()
                    && result.get() == ButtonType.OK) {

                customers.remove(selectedCustomer);
            }
        });

        // -----------------------------
        // Keyboard access
        // -----------------------------

        // Press Enter in name field
        nameField.setOnAction(event ->
                addButton.fire()
        );

        // Press Enter in province box
        provinceComboBox.setOnAction(event ->
                addButton.fire()
        );

        // -----------------------------
        // Form layout
        // -----------------------------
        GridPane form = new GridPane();

        form.setHgap(10);
        form.setVgap(10);

        form.add(nameLabel, 0, 0);
        form.add(nameField, 1, 0);

        form.add(provinceLabel, 0, 1);
        form.add(provinceComboBox, 1, 1);

        // -----------------------------
        // Button layout
        // -----------------------------
        HBox buttons = new HBox(
                10,
                addButton,
                deleteButton
        );

        buttons.setAlignment(Pos.CENTER);

        // -----------------------------
        // Main layout
        // -----------------------------
        VBox root = new VBox(
                15,
                title,
                form,
                buttons,
                tableView
        );

        root.setPadding(new Insets(20));

        VBox.setVgrow(tableView, Priority.ALWAYS);

        // -----------------------------
        // Scene
        // -----------------------------
        Scene scene = new Scene(root, 600, 500);

        stage.setTitle("Customer Manager");
        stage.setScene(scene);
        stage.show();

        nameField.requestFocus();
    }

    // -----------------------------
    // Error dialog method
    // -----------------------------
    private void showError(String title, String message) {

        Alert alert = new Alert(
                Alert.AlertType.ERROR
        );

        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}