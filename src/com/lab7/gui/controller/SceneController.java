package com.lab7.gui.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class SceneController
{
    @FXML
    public Label label = null;

    @FXML
    public Button nav1 = null, nav2 = null;

    @FXML
    public void pressButton(ActionEvent e) throws Exception
    {
        Stage window;
        FXMLLoader loader;
        VBox content;

        if (e.getSource() == nav1)
        {
            window = (Stage)nav1.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Scene1.fxml"));
        }
        else
        {
            window = (Stage)nav2.getScene().getWindow();
            loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/Scene2.fxml"));
        }

        content = loader.<VBox>load();
        Scene scene = new Scene(content, 640, 480);
        scene.getStylesheets().add("/style/Scene.css");
        window.setScene(scene);
        window.show();
    }
}