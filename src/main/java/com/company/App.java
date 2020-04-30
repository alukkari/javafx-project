package com.company;

import com.company.util.FileHandler;
import com.company.util.JavaCompiler;
import javafx.animation.RotateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
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
import javafx.util.Duration;

import javax.swing.text.html.ImageView;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;


public class App extends Application {

    private MenuItem saveF;
    private MenuItem compile;
    private Button clearBtn;
    private Button runBtn;
    private ComboBox fontCBox;
    private TextField fontSize;
    private TextField searchBox;
    private Button searchPreviousBtn;
    private Button searchNextBtn;
    private Button clearSBtn;
    private ColorPicker colorPicker;
    private TextArea textAlue;
    private TextArea outputAlue;
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
        Thread t = new Thread(() -> {
            fh = new FileHandler(selectedFile.getPath());
            String content = fh.openF(selectedFile.getPath());

            if(content != null) {
                Platform.runLater(() -> {
                    textAlue.setText(content);
                    saveF.setDisable(false);
                    runBtn.setDisable(false);
                    compile.setDisable(false);
                });
            } else {
                Platform.runLater(() -> {
                    showErrorMsg("Error loading file!");
                });
            }
        });
        t.start();
    }

    public void saveAsFile(ActionEvent e) {
        File file = fileChooser.showSaveDialog(stage);
        Thread t = new Thread(() -> {
            String filePath = file.getAbsolutePath();
            fh.setTextFile(filePath);

            Platform.runLater(() -> {
                fh.saveF(textAlue.getText());
                saveF.setDisable(false);
            });
        });
        t.start();
    }

    public void saveFile(ActionEvent e) {
        Thread t = new Thread(() -> {
            Platform.runLater(() -> {
                fh.saveF(textAlue.getText());
            });
        });
        t.start();
    }

    public void search() {
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

    public void searchPrevious() {
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

    public void searchNext() {
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

    public void showErrorMsg(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error Dialog");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void compAndRun() throws IOException, InterruptedException {
        String file = fh.getTextFile();
        JavaCompiler compiler = new JavaCompiler();
        String output = compiler.compileAndRun(file);
        outputAlue.setText(output);
    }

    public void setEffect(Button b) {
        Light.Distant light = new
        Light.Distant();
        light.setAzimuth(-135.0f);
        Lighting l = new Lighting();
        l.setLight(light);
        l.setSurfaceScale(1.0f);
        b.setEffect(l);
    }

    public void animateButton(Button b) {
        RotateTransition rotateTransition = new  RotateTransition();
        rotateTransition.setDuration(Duration.millis(500));
        rotateTransition.setByAngle(360);
        rotateTransition.setNode(b);
        rotateTransition.play();
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
        MenuItem openF = new MenuItem("Open...");
        MenuItem saveAsF = new MenuItem("Save As...");
        saveF = new MenuItem("Save");
        saveF.setDisable(true);
        MenuItem exit = new MenuItem("Exit");
        SeparatorMenuItem separator = new SeparatorMenuItem();

        newF.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN));
        openF.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
        saveAsF.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
        saveF.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));

        newF.setOnAction(this::newFile);

        openF.setOnAction(this::openFile);

        saveAsF.setOnAction(this::saveAsFile);

        saveF.setOnAction(this::saveFile);

        file.getItems().addAll(newF, openF, saveAsF, saveF, separator, exit);

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
        compile = new MenuItem("Compile and Run");
        compile.setDisable(true);

        compile.setOnAction(e -> {
            try {
                compAndRun();
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });

        run.getItems().addAll(compile);

        Menu about = new Menu("About");
        MenuItem aboutApp = new MenuItem("About App");
        about.getItems().addAll(aboutApp);

        menubar.getMenus().addAll(file, edit, run, about);

        exit.setOnAction(this::exitValittu);
        about.setOnAction(this::aboutValittu);

        textAlue = new TextArea();
        outputAlue = new TextArea();

        clearBtn = new Button("CLEAR");
        setEffect(clearBtn);
        clearBtn.setOnAction( e -> {
            textAlue.setText("");
            outputAlue.setText("");
        });

        runBtn = new Button("RUN");
        setEffect(runBtn);
        runBtn.setDisable(true);
        runBtn.setOnAction((e -> {
            Thread t = new Thread(() -> {
                try {
                    Platform.runLater(() -> {
                        animateButton(runBtn);
                    });
                    compAndRun();
                } catch (IOException ex) {
                    ex.printStackTrace();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            });
            t.start();
        }));

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
        setEffect(searchPreviousBtn);
        searchPreviousBtn.setOnAction(e -> searchPrevious());

        searchNextBtn = new Button(">");
        setEffect(searchNextBtn);
        searchNextBtn.setOnAction(e -> searchNext());

        //CLEAR SEARCH
        clearSBtn = new Button("x");
        setEffect(clearSBtn);
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
        fontSize.setText("14");
        colorPicker.setValue(Color.valueOf("Black"));

        ToolBar toolbar = new ToolBar();
        toolbar.getItems().addAll(clearBtn, new Separator(), runBtn, new Separator(), fontCBox, new Separator(), fontSize, new Separator(),
                colorPicker, new Separator(), searchBox, searchPreviousBtn, searchNextBtn, clearSBtn);
        VBox ylapalkki = new VBox(menubar, toolbar);

        //OUTPUT
        outputAlue.setEditable(false);
        outputAlue.setStyle("-fx-font-family: " + "Arial" + ";" + "-fx-font-size: " + "14" + ";" +
                "-fx-text-fill: green" + ";" + "-fx-control-inner-background: " + "black" + ";");
        outputAlue.setText("");
        BorderPane borderP2 = new BorderPane();
        borderP2.setTop(new Label("Output"));
        borderP2.setCenter(outputAlue);

        //SPLITPANE
        SplitPane splitPane = new SplitPane();
        splitPane.setOrientation(Orientation.VERTICAL);
        splitPane.getItems().addAll(textAlue, borderP2);

        stage.setTitle(title);
        BorderPane borderP = new BorderPane();
        borderP.setTop(ylapalkki);
        borderP.setCenter(splitPane);

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