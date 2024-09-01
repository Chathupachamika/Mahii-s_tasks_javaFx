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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SearchTaskForm {

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
    private DatePicker txtDateforSearch;

    @FXML
    private TextField txtSearchTitleDescription;

    MahiiService service =new MahiiController();
    @FXML
    void btnSearchOnAction(ActionEvent event) {
        String searchQuery = txtSearchTitleDescription.getText();
        ObservableList<MahiRajapakshe> searchResults = FXCollections.observableArrayList();

        String sql = "SELECT * FROM completed_tasks WHERE title LIKE ? OR description LIKE ? OR due_date = ?";
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
                task.setCompletedAt(resultSet.getTimestamp("completed_at").toLocalDateTime());

                searchResults.add(task);
            }

            // Bind data to TableView
            txtId.setCellValueFactory(new PropertyValueFactory<>("id"));
            txtTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
            txtDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
            txtDueDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
            txtCreatedDate.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
            txtCompletedAt.setCellValueFactory(new PropertyValueFactory<>("completedAt"));

            tableView.setItems(searchResults);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to search tasks.");
        }
    }

    @FXML
    void btnDeleteSearchOnAction(ActionEvent event) {

        if (service.deleteSearch(Integer.parseInt(txtId.getText()))){
            new Alert(Alert.AlertType.INFORMATION,"Customer Deleted !!").show();
        }else{
            new Alert(Alert.AlertType.ERROR).show();
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
    void btnAddOnAction(ActionEvent event) {
        Stage stage1 = new Stage();
        try {
            stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/add_task_form.fxml"))));
            stage1.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
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

    @FXML
    void btnCompleteOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/complete_task_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
    }
}
