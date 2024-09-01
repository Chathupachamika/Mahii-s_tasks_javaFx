package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MahiRajapakshe;

import java.sql.*;

public class MahiiController implements MahiiService {


    @Override
    public ObservableList<MahiRajapakshe> getAll() {
        // Not implemented yet. You might want to retrieve both active and completed tasks here.
        return null;
    }

    @Override
    public boolean deleteSearch(int id) {
        String sql = "DELETE FROM completed_tasks WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            // Set the ID parameter
            statement.setInt(1, id);

            // Execute the update and return true if a row was affected
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public ObservableList<MahiRajapakshe> getCompletedTasks() {
        ObservableList<MahiRajapakshe> completedTasks = FXCollections.observableArrayList();
        String sql = "SELECT id, title, description, due_date, created_at, completed_at FROM completed_tasks";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                MahiRajapakshe task = new MahiRajapakshe();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setDueDate(resultSet.getDate("due_date").toLocalDate());
                task.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                task.setCompletedAt(resultSet.getTimestamp("completed_at").toLocalDateTime());

                completedTasks.add(task);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return completedTasks;
    }
}
