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
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;

import java.io.File;


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

    public void aboutValittu(ActionEvent e) {
        System.out.println("About");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About App");
        alert.setHeaderText(null);
        alert.setContentText("Information about this App...");
        alert.showAndWait();
    }

    @Override
    public void start(Stage stage) {

        MenuBar menubar = new MenuBar();
        FileChooser fileChooser = new FileChooser();

        Menu file = new Menu("File");
        MenuItem newF = new MenuItem("New");
        MenuItem openF = new MenuItem("Open");
        MenuItem saveF = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        newF.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        openF.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        saveF.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        newF.setOnAction(e -> System.out.println("New"));
        openF.setOnAction(e -> {
            System.out.println("Open");
            File selectedFile = fileChooser.showOpenDialog(stage);
        });
        saveF.setOnAction(e -> System.out.println("Save"));

        file.getItems().addAll(newF, openF, saveF, exit);

        Menu edit = new Menu("Edit");
        MenuItem cut = new MenuItem("Cut");
        MenuItem copy = new MenuItem("Copy");
        MenuItem paste = new MenuItem("Paste");

        cut.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.SHORTCUT_DOWN));
        copy.setAccelerator(new KeyCodeCombination(KeyCode.C, KeyCombination.SHORTCUT_DOWN));
        paste.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.SHORTCUT_DOWN));

        cut.setOnAction(e -> System.out.println("Cut"));
        copy.setOnAction(e -> System.out.println("Copy"));
        paste.setOnAction(e -> System.out.println("Paste"));

        edit.getItems().addAll(cut, copy, paste);

        Menu run = new Menu("Run");
        MenuItem compile = new MenuItem("Compile and Run");
        run.getItems().addAll(compile);

        Menu about = new Menu("About");
        MenuItem aboutApp = new MenuItem("About App");
        about.getItems().addAll(aboutApp);

        menubar.getMenus().addAll(file, edit, run, about);

        exit.setOnAction(this::exitValittu);
        about.setOnAction(this::aboutValittu);

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