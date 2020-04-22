package com.company;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.*;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class App extends Application {

    private Button clearBtn;
    private ComboBox fontCBox;
    private TextField fontSize;
    private ColorPicker colorPicker;
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

    public void cutText(ActionEvent e) {
        textAlue.cut();
    }

    public void copyText(ActionEvent e) {
        textAlue.copy();
    }

    public void pasteText(ActionEvent e) {
        textAlue.paste();
    }

    public String hexToRgb(Color color) {
        final int MAX = 255;
        return "rgb("
                + (color.getRed() * MAX)
                + "," + (color.getGreen() * MAX)
                + "," + (color.getBlue() * MAX)
                + ")";
    }

    public void styleChange(){
        String textFont = fontCBox.getValue().toString();
        int size = Integer.parseInt(fontSize.getText());
        Color value = colorPicker.getValue();
        String textColor = hexToRgb(value);
        textAlue.setStyle("-fx-font-family: " + textFont + ";" + "-fx-font-size: " + size + ";" + "-fx-text-fill: " + textColor + ";" );
        textAlue.requestFocus();
        textAlue.positionCaret(textAlue.getText().length());
    }

    @Override
    public void start(Stage stage) {

        //Locale locale = Locale.getDefault();
        Locale locale = new Locale("fi", "FI");
        ResourceBundle labels = ResourceBundle.getBundle("ui", locale);
        String title = labels.getString("title");

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

        cut.setOnAction(this::cutText);
        copy.setOnAction(this::copyText);
        paste.setOnAction(this::pasteText);

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

        textAlue = new TextArea();

        clearBtn = new Button("CLEAR");
        clearBtn.setOnAction(actionEvent -> textAlue.setText(""));

        //FONT
        fontCBox = new ComboBox();
        List<String> fontFamilies = Font.getFamilies();
        fontCBox.setItems(FXCollections.observableArrayList(fontFamilies));
        fontCBox.setOnAction(e -> styleChange());

        //FONT SIZE
        fontSize = new TextField();
        fontSize.setPrefColumnCount(3);
        fontSize.setOnAction(e -> styleChange());

        //COLORPICKER
        colorPicker = new ColorPicker();
        colorPicker.setOnAction(e -> styleChange());

        //DEFAULT VALUES AT START
        fontCBox.setValue("Arial");
        fontSize.setText("12");
        colorPicker.setValue(Color.valueOf("Black"));

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(clearBtn, new Separator(), fontCBox, new Separator(), fontSize, new Separator(), colorPicker);
        VBox ylapalkki = new VBox(menubar, toolbar);

        stage.setTitle(title);
        BorderPane borderP = new BorderPane();
        borderP.setTop(ylapalkki);
        borderP.setCenter(textAlue);

        textAlue.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.TAB) {
                //System.out.println("tab painettu");
                int cursorIndex = textAlue.getCaretPosition();
                textAlue.replaceText(cursorIndex-1, cursorIndex, "    ");
            }
        });

        Scene scene = new Scene(borderP, 640, 480);
        stage.initStyle(StageStyle.DECORATED);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        textAlue.requestFocus();

    }

    @Override
    public void stop() {
        System.out.println("stop");
    }

    public static void main(String args[]) {
        launch(args);
    }
}