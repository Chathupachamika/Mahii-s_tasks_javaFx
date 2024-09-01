package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompleteTaskForm {

    @FXML
    private TableView<MahiRajapakshe> tableView;

    @FXML
    private TableColumn<MahiRajapakshe, Integer> txtId;

    @FXML
    private TableColumn<MahiRajapakshe, String> txtTitle;

    @FXML
    private TableColumn<MahiRajapakshe, String> txtDescription;

    @FXML
    private TableColumn<MahiRajapakshe, String> txtDueDate;

    @FXML
    private TableColumn<MahiRajapakshe, String> txtCreatedDate;

    @FXML
    private TableColumn<MahiRajapakshe, String> txtCompletedAt;

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        ObservableList<MahiRajapakshe> completedTasks = FXCollections.observableArrayList();

        String sql = "SELECT id, title, description, due_date, created_at, completed_at FROM completed_tasks";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MahiRajapakshe task = new MahiRajapakshe(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        true,
                        resultSet.getTimestamp("created_at").toLocalDateTime(),
                        resultSet.getTimestamp("completed_at") != null ? resultSet.getTimestamp("completed_at").toLocalDateTime() : null
                );
                completedTasks.add(task);
            }

            tableView.setItems(completedTasks);
            System.out.println("Tasks loaded successfully.");
            completedTasks.forEach(task -> System.out.println(task));

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to load completed tasks.");
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void initialize() {
        // Set up the columns in the table
        txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
        txtTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        txtDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        txtDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        txtCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        txtCompletedAt.setCellValueFactory(new PropertyValueFactory<>("completedAt"));
    }
    @FXML
    void btnBackToHomeOnAction(ActionEvent event) {
        Stage stage1 = new Stage();
        try {
            stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/profile_form.fxml"))));
            stage1.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
    }
}
