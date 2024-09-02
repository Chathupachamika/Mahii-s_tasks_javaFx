package controller;

import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

public class AddTaskFormController {

    @FXML
    private ImageView icnHome1;

    @FXML
    private JFXTextField txtAddToDoDescription;

    @FXML
    private JFXTextField txtAddToDoTitle;

    @FXML
    private DatePicker txtDate;

    @FXML
    void btnAddOnAction(ActionEvent event) {
        String title = txtAddToDoTitle.getText();
        String description = txtAddToDoDescription.getText();
        LocalDate dueDate = txtDate.getValue();

        MahiRajapakshe newTask = new MahiRajapakshe();
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setDueDate(dueDate);
        newTask.setCompleted(false);

        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String sql = "INSERT INTO active_tasks (title, description, due_date, is_completed) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(sql); //
            statement.setString(1, newTask.getTitle());
            statement.setString(2, newTask.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(newTask.getDueDate()));
            statement.setBoolean(4, newTask.isCompleted());
            statement.executeUpdate();

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Task Added");
            alert.setHeaderText(null);
            alert.setContentText("Task Added Successfully....");
            alert.showAndWait();



        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Database Error.....");
            alert.setHeaderText(null);
            alert.setContentText("There was an error......");
            alert.showAndWait();
        }

    }

    @FXML
    void btnBackToHomeOnAction(ActionEvent event) {
        Stage stage2 = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage2.close();
    }

}

