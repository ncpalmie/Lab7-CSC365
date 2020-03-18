package com.lab7.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class CurrentController
{
    @FXML
    public Label label = null;

    @FXML
    public Button navHome, navBook, navAlter, navCurrent, navRevenue;

    @FXML
    public void pressButton(ActionEvent e) throws Exception
    {
        Stage window;
        FXMLLoader loader;
        VBox content;

        if (e.getSource() == navHome)
        {
            window = (Stage)navHome.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Home.fxml"));
        }
        else if (e.getSource() == navBook)
        {
            window = (Stage)navBook.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Book.fxml"));
        }
        else if (e.getSource() == navAlter)
        {
            window = (Stage)navAlter.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Alter.fxml"));
        }
        else if (e.getSource() == navCurrent)
        {
            window = (Stage)navCurrent.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Current.fxml"));
        }
        else
        {
            window = (Stage)navRevenue.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Revenue.fxml"));
        }

        content = loader.<VBox>load();
        Scene scene = new Scene(content, 640, 480);
        scene.getStylesheets().add("/style/Scene.css");
        window.setScene(scene);
        window.show();
    }
}