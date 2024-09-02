package controller;

import javafx.collections.ObservableList;
import model.MahiRajapakshe;

public interface MahiiService {


    boolean deleteSearch(int id);


    ObservableList<MahiRajapakshe> getAll(String searchQuery, java.sql.Date searchDate);
}
