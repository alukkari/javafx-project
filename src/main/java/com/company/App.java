package com.company;

import com.company.util.FileHandler;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
    private TextField searchBox;
    private Button searchPreviousBtn;
    private Button searchNextBtn;
    private Button clearSBtn;
    private ColorPicker colorPicker;
    private TextArea textAlue;
    private FileChooser fileChooser;
    private FileHandler fh;
    private Stage stage;
    private int fromIndex = 0;

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

    public void styleChange() {
        String textFont = fontCBox.getValue().toString();
        int size = Integer.parseInt(fontSize.getText());
        Color value = colorPicker.getValue();
        String textColor = hexToRgb(value);
        textAlue.setStyle("-fx-font-family: " + textFont + ";" + "-fx-font-size: " + size + ";" + "-fx-text-fill: " + textColor + ";" );
        textAlue.requestFocus();
        textAlue.positionCaret(textAlue.getText().length());
    }

    public void newFile(ActionEvent e) {
        textAlue.setText("");
        fontCBox.setValue("Arial");
        fontSize.setText("12");
        colorPicker.setValue(Color.valueOf("Black"));

    }

    public void openFile(ActionEvent e) {
        File selectedFile = fileChooser.showOpenDialog(stage);
        fh = new FileHandler(selectedFile.getPath());
        String content = fh.openF(selectedFile.getPath());
        textAlue.setText(content);
    }

    public void saveFile(ActionEvent e) {
        File file = fileChooser.showSaveDialog(stage);
        String filePath = file.getAbsolutePath();
        fh.setTextFile(filePath);

        //fh.setTextFile("Tiedosto.txt");
        fh.saveF(textAlue.getText());
    }

    public void search(){
         String searching = searchBox.getText();
        if (searching.length() <= 0) {
            return;
        }

        String content = textAlue.getText();
        int i = content.indexOf(searching, 0);
        if (i >= 0) {
            int last = i + searching.length();
            textAlue.selectRange(i,last);
            fromIndex=last;
        }
    }

    public void searchPrevious(){
        String searching = searchBox.getText();
        if (searching.length() <= 0) {
            return;
        }

        fromIndex = (textAlue.getCaretPosition()-searching.length()-1);
        String content = textAlue.getText();
        int i = content.lastIndexOf(searching, fromIndex);
        if (i >= 0) {
            int last = i + searching.length();
            textAlue.selectRange(i,last);
        }
    }

    public void searchNext(){
        String searching = searchBox.getText();
        if (searching.length() <= 0) {
            return;
        }

        fromIndex = textAlue.getCaretPosition();
        String content = textAlue.getText();
        int i = content.indexOf(searching, fromIndex);
        if (i >= 0) {
            int last = i + searching.length();
            textAlue.selectRange(i,last);
        }
    }

    @Override
    public void start(Stage stage) {
        fileChooser = new FileChooser();
        fh = new FileHandler();

        //Locale locale = Locale.getDefault();
        Locale locale = new Locale("fi", "FI");
        ResourceBundle labels = ResourceBundle.getBundle("ui", locale);
        String title = labels.getString("title");

        MenuBar menubar = new MenuBar();

        Menu file = new Menu("File");
        MenuItem newF = new MenuItem("New");
        MenuItem openF = new MenuItem("Open");
        MenuItem saveF = new MenuItem("Save");
        MenuItem exit = new MenuItem("Exit");

        newF.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        openF.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        saveF.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        newF.setOnAction(this::newFile);

        openF.setOnAction(this::openFile);

        saveF.setOnAction(this::saveFile);

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
        clearBtn.setOnAction( e -> textAlue.setText(""));

        //FONT
        fontCBox = new ComboBox();
        fontCBox.setPrefWidth(150);
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

        //SEARCH BOX
        searchBox = new TextField();
        searchBox.setPromptText("Search");
        searchBox.setOnAction(e -> search());

        searchPreviousBtn = new Button("<");
        searchPreviousBtn.setOnAction(e -> searchPrevious());

        searchNextBtn = new Button(">");
        searchNextBtn.setOnAction(e -> searchNext());

        //CLEAR SEARCH
        clearSBtn = new Button("x");
        clearSBtn.setDisable(true);
        clearSBtn.setOnAction( e -> {
            searchBox.setText("");
            searchBox.requestFocus();
        });
        searchBox.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                clearSBtn.setDisable(searchBox.getText().length() ==0);
            }
        });


        //DEFAULT VALUES AT START
        fontCBox.setValue("Arial");
        fontSize.setText("12");
        colorPicker.setValue(Color.valueOf("Black"));

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(clearBtn, new Separator(), fontCBox, new Separator(), fontSize, new Separator(), colorPicker, new Separator(), searchBox, searchPreviousBtn, searchNextBtn, clearSBtn);
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

        Scene scene = new Scene(borderP, 800, 600);
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