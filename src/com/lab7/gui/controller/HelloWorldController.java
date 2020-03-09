package com.lab7.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import com.lab7.lib.Hello;

public class HelloWorldController
{
    @FXML
    public Label label = null;

    @FXML
    public void pressButton(Event e)
    {
        label.setText(Hello.getTest());
    }
}