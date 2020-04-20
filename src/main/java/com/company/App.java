package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;


public class App extends Application {

    private Button clearBtn;
    private TextArea textAlue;

    /*
    class Kuuntelija implements EventHandler<ActionEvent> {
        public void handle(ActionEvent actionEvent) {
            System.out.println("Clear");
            textAlue.setText("");
        }
    }
    */

    public App() {
        System.out.println("constructor");
    }

    @Override
    public void init() {
        System.out.println("init");
    }

    @Override
    public void start(Stage stage) {

        clearBtn = new Button("CLEAR");
        textAlue = new TextArea();
        //clearBtn.setOnAction(new Kuuntelija());

        clearBtn.setOnAction(actionEvent -> textAlue.setText(""));

        stage.setTitle("JavaFX Code Editor");
        BorderPane borderP = new BorderPane();
        borderP.setTop(clearBtn);
        borderP.setCenter(textAlue);

        Scene scene = new Scene(borderP, 640, 480);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        //stage.setWidth(640);
        //stage.setHeight(480);
        stage.centerOnScreen();
        stage.show();

        //Scene scene = new Scene(new Button("Click!"));
        //Group group = new Group(new Button("Hello"), new Button("World"));
        //Scene scene = new Scene(group, 640, 480);
        //stage.setScene(scene);
    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    public static void main(String args[]) {
        launch(args);
    }
}