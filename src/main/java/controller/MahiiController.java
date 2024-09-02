package controller;

import db.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.MahiRajapakshe;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MahiiController implements MahiiService {

    @Override
    public boolean deleteSearch(int id) {
        String sql = "DELETE FROM completed_tasks WHERE id = ?";
        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setInt(1, id);
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public ObservableList<MahiRajapakshe> getAll(String searchQuery, java.sql.Date searchDate) {
        ObservableList<MahiRajapakshe> mahiRajapaksheObservableList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM completed_tasks WHERE (title LIKE ? OR description LIKE ?) AND (? IS NULL OR due_date = ?)";

        try (Connection connection = DBConnection.getInstance().getConnection();
             PreparedStatement psTm = connection.prepareStatement(sql)) {

            psTm.setString(1, "%" + searchQuery + "%");
            psTm.setString(2, "%" + searchQuery + "%");
            psTm.setDate(3, searchDate);
            psTm.setDate(4, searchDate);

            ResultSet resultSet = psTm.executeQuery();
            while (resultSet.next()) {
                MahiRajapakshe task = new MahiRajapakshe();
                task.setId(resultSet.getInt("id"));
                task.setTitle(resultSet.getString("title"));
                task.setDescription(resultSet.getString("description"));
                task.setDueDate(resultSet.getDate("due_date").toLocalDate());
                task.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());
                task.setCompletedAt(resultSet.getTimestamp("completed_at").toLocalDateTime());

                mahiRajapaksheObservableList.add(task);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks: " + e.getMessage(), e);
        }
        return mahiRajapaksheObservableList;
    }
}
