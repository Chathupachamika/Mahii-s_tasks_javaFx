package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchActiveTasks {

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
    private DatePicker txtDateforSearch;

    @FXML
    private TextField txtSearchTitleDescription;

    @FXML
    void btnBackToHomeOnAction(ActionEvent event) {
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchQuery = txtSearchTitleDescription.getText();
        ObservableList<MahiRajapakshe> searchResults = FXCollections.observableArrayList();

        String sql = "SELECT * FROM active_tasks WHERE title LIKE ? OR description LIKE ? OR due_date = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, "%" + searchQuery + "%");
            statement.setString(2, "%" + searchQuery + "%");
            statement.setDate(3, txtDateforSearch.getValue() != null ? java.sql.Date.valueOf(txtDateforSearch.getValue()) : null);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                MahiRajapakshe task = new MahiRajapakshe();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setDueDate(resultSet.getDate("due_date").toLocalDate());
                task.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

                searchResults.add(task);
            }

           
            txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
            txtTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            txtDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            txtDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            txtCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));

            tableView.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search tasks.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
