package com.lab7.gui.controller;

import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import com.lab7.lib.Hello;

public class HelloWorldController
{
    @FXML
    public Button hello = null;

    @FXML
    public void pressButton(Event e)
    {
        hello.setText(Hello.getTest());
    }
}