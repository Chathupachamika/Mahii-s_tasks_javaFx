package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ActiveTaskForm {

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
    void btnBackToHomeOnAction(ActionEvent event){
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
    }

    @FXML
    void btnReloadOnAction(ActionEvent event) {
        loadCompletedTasks();
    }

    private void loadCompletedTasks() {
        ObservableList<MahiRajapakshe> activeTasks = FXCollections.observableArrayList();

        String sql = "SELECT id, title, description, due_date, created_at FROM active_tasks";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MahiRajapakshe task = new MahiRajapakshe(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("due_date").toLocalDate(),
                        resultSet.getTimestamp("created_at").toLocalDateTime()
                );
                activeTasks.add(task);
            }

            tableView.setItems(activeTasks);
            System.out.println("Tasks loaded successfully.");
            activeTasks.forEach(task -> System.out.println(task));

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
        txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
        txtTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        txtDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        txtDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        txtCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
    }

}
