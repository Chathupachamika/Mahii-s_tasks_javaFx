package controller;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXTextField;
import db.DBConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import model.MahiRajapakshe;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class ProfileFormController {

    @FXML
    private JFXCheckBox chechBox1;

    @FXML
    private JFXCheckBox chechBox2;

    @FXML
    private JFXCheckBox chechBox3;

    @FXML
    private JFXCheckBox chechBox4;

    @FXML
    private JFXCheckBox chechBox5;

    @FXML
    private JFXCheckBox chechBox6;

    @FXML
    private JFXTextField txtDescription1;

    @FXML
    private JFXTextField txtDescription2;

    @FXML
    private JFXTextField txtDescription3;

    @FXML
    private JFXTextField txtDescription4;

    @FXML
    private JFXTextField txtDescription5;

    @FXML
    private JFXTextField txtDescription6;

    @FXML
    private JFXTextField txtTitle1;

    @FXML
    private JFXTextField txtTitle2;

    @FXML
    private JFXTextField txtTitle3;

    @FXML
    private JFXTextField txtTitle4;

    @FXML
    private JFXTextField txtTitle5;

    @FXML
    private JFXTextField txtTitle6;
    private JFXCheckBox checkBox;
    private JFXTextField titleField;
    private JFXTextField descriptionField;

    @FXML
    void btnAddTask(ActionEvent event) {
        Stage stage1 = new Stage();
        try {
            stage1.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/add_task_form.fxml"))));
            stage1.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (chechBox1.isSelected()) {
            addTaskToCompleted(txtTitle1.getText(), txtDescription1.getText());
            txtTitle1.setText("");
            txtDescription1.setText("");
        }
        if (chechBox2.isSelected()) {
            addTaskToCompleted(txtTitle2.getText(),txtDescription2.getText());
            txtTitle2.setText("");
            txtDescription2.setText("");
        }
        if (chechBox3.isSelected()) {
            addTaskToCompleted(txtTitle3.getText(), txtDescription3.getText());
            txtTitle3.setText("");
            txtDescription3.setText("");
        }
        if (chechBox4.isSelected()) {
            addTaskToCompleted(txtTitle4.getText(), txtDescription4.getText());
            txtTitle4.setText("");
            txtDescription4.setText("");
        }
        if (chechBox5.isSelected()) {
            addTaskToCompleted(txtTitle5.getText(), txtDescription5.getText());
            txtTitle5.setText("");
            txtDescription5.setText("");
        }
        if (chechBox6.isSelected()) {
            addTaskToCompleted(txtTitle6.getText(), txtDescription6.getText());
            txtTitle6.setText("");
            txtDescription6.setText("");
        }
    }

    private void addTaskToCompleted(String title, String description) {
        MahiRajapakshe task = new MahiRajapakshe();
        task.setTitle(title);
        task.setDescription(description);
        task.setDueDate(LocalDate.now());  // Example due date
        task.setCompleted(true);

        // Print to console
        System.out.println(task);

        // Insert into database
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String sql = "INSERT INTO completed_tasks (title, description, due_date, completed_at) VALUES (?, ?, ?, NOW())";
            PreparedStatement pstmt = connection.prepareStatement(sql);
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setDate(3, java.sql.Date.valueOf(task.getDueDate()));
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        processTask(checkBox,titleField,descriptionField);

    }


    @FXML
    void btnReloadTasks(ActionEvent event)  {
        // Step 2: Reload the active tasks from the database
        loadActiveTasks();


    }

    private void loadActiveTasks() {
        try (Connection connection = DBConnection.getInstance().getConnection()) {
            String query = "SELECT * FROM active_tasks WHERE is_completed = 0";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Populate the fields with data from the database
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle1, txtDescription1, chechBox1);
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle2, txtDescription2, chechBox2);
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle3, txtDescription3, chechBox3);
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle4, txtDescription4, chechBox4);
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle5, txtDescription5, chechBox5);
            if (resultSet.next()) populateTaskFields(resultSet, txtTitle6, txtDescription6, chechBox6);

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Error", "Unable to load tasks from the database.");
        }
    }

    private void populateTaskFields(ResultSet resultSet, JFXTextField titleField, JFXTextField descriptionField, JFXCheckBox checkBox) throws SQLException {
        titleField.setText(resultSet.getString("title"));
        descriptionField.setText(resultSet.getString("description"));
        checkBox.setSelected(false);
    }

    private void processTask(JFXCheckBox checkBox, JFXTextField titleField, JFXTextField descriptionField) {
        this.checkBox = checkBox;
        this.titleField = titleField;
        this.descriptionField = descriptionField;
        if (checkBox.isSelected() && !titleField.getText().isEmpty()) {
            try (Connection connection = DBConnection.getInstance().getConnection()) {
                // Move task from active_tasks to completed_tasks
                String title = titleField.getText();
                String description = descriptionField.getText();

                // Find the task ID in active_tasks
                String findTaskQuery = "SELECT id FROM active_tasks WHERE title = ? AND description = ?";
                PreparedStatement findStatement = connection.prepareStatement(findTaskQuery);
                findStatement.setString(1, title);
                findStatement.setString(2, description);
                ResultSet rs = findStatement.executeQuery();

                if (rs.next()) {
                    int taskId = rs.getInt("id");

                    // Insert task into completed_tasks
                    String insertQuery = "INSERT INTO completed_tasks (title, description, due_date, created_at) " +
                            "SELECT title, description, due_date, created_at FROM active_tasks WHERE id = ?";
                    PreparedStatement insertStatement = connection.prepareStatement(insertQuery);
                    insertStatement.setInt(1, taskId);
                    insertStatement.executeUpdate();

                    // Remove task from active_tasks
                    String deleteQuery = "DELETE FROM active_tasks WHERE id = ?";
                    PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
                    deleteStatement.setInt(1, taskId);
                    deleteStatement.executeUpdate();

                    // Clear the UI elements
                    titleField.clear();
                    descriptionField.clear();
                    checkBox.setSelected(false);

                    showAlert("Success", "Task added to completed tasks successfully!");
                    System.out.println("Task moved to completed tasks: " + title + " - " + description);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Error", "Unable to add the task to completed tasks.");
            }
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    void btnActiveOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/active_task_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
    }

    @FXML
    void btnSearchOnAction(ActionEvent event) {
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("../view/search_task_form.fxml"))));
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
