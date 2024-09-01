package controller;

import javafx.collections.ObservableList;
import model.MahiRajapakshe;

public interface MahiiService {



    ObservableList<MahiRajapakshe> getAll();


    boolean deleteSearch(int id);
}
