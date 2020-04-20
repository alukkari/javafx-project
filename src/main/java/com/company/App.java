package com.company;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;


public class App extends Application {

    private Button clearBtn;
    private TextArea textAlue;

    public App() {
        System.out.println("constructor");
    }

    @Override
    public void init() {
        System.out.println("init");
    }

    public void exitValittu(ActionEvent e) {
        System.out.println("exit");
        System.exit(0);
    }

    @Override
    public void start(Stage stage) {

        MenuBar menubar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem newF = new MenuItem("New (ctrl+n)");
        MenuItem openF = new MenuItem("Open (ctrl+o)");
        MenuItem saveF = new MenuItem("Save (ctrl+s)");
        MenuItem exit = new MenuItem("Exit");

        newF.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));

        file.getItems().addAll(newF, openF, saveF, exit);

        Menu edit = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut (ctrl+x)");
        MenuItem copy = new MenuItem("Copy (ctrl+c)");
        MenuItem paste = new MenuItem("Paste (ctrl+v)");
        edit.getItems().addAll(cut, copy, paste);

        Menu run = new Menu("Run");
        MenuItem compile = new MenuItem("Compile and Run");
        run.getItems().addAll(compile);

        Menu about = new Menu("About");
        MenuItem aboutApp = new MenuItem("About App");
        about.getItems().addAll(aboutApp);

        menubar.getMenus().addAll(file, edit, run, about);
        exit.setOnAction(this::exitValittu);

        clearBtn = new Button("CLEAR");
        textAlue = new TextArea();

        clearBtn.setOnAction(actionEvent -> textAlue.setText(""));

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(clearBtn);
        VBox ylapalkki = new VBox(menubar, toolbar);

        stage.setTitle("JavaFX Code Editor");
        BorderPane borderP = new BorderPane();
        borderP.setTop(ylapalkki);
        borderP.setCenter(textAlue);

        Scene scene = new Scene(borderP, 640, 480);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    public static void main(String args[]) {
        launch(args);
    }
}