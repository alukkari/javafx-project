package com.company.util;

import com.company.App;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;


public class FileHandler {

    private String textFile;
    private String content;
    private App app;


    public FileHandler() {

    }

    public FileHandler(String textFile, App app) {
        setTextFile(textFile);
        this.app = app;
    }

    public String getTextFile() {
        return textFile;
    }

    public void setTextFile(String textFile) {
        this.textFile = textFile;
    }

    // Opens the given file and returns the content, uses filePath
    public String openF(String textFile) {
        try {
            content = Files.readString(Paths.get(textFile), Charset.defaultCharset());
        } catch(IOException e) {
            System.out.println("problem with IO");
            e.printStackTrace();
            app.showErrorMsg("Error opening file!");
        }
        return content;
    }

    // Saves the content to given file path
    public void saveF(String content) {
        try {
            Files.writeString(Paths.get(textFile), content);
        } catch(IOException e) {
            System.out.println("problem with IO");
            e.printStackTrace();
            app.showErrorMsg("Error saving file!");
        }
    }
}