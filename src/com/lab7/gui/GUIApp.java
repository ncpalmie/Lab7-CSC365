package com.lab7.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Label;

public class GUIApp extends Application
{
    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/HelloWorld.fxml"));
        VBox content = loader.<VBox>load();

        Scene scene = new Scene(content, 640, 480);
        scene.getStylesheets().add("/HelloWorld.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}