package com.example.lapa2;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;

public class Main extends Application {
    public static String key;
    public static String extension;
    byte[] inputText;
    @Override
    public void start(Stage stage) {
        Button btnChooseFile = new Button("Choose file for encrypt");
        FileChooser fcChooseFile = new FileChooser();

        Label lPlainText = new Label("Plain text:");
        TextArea taPlainText = new TextArea();
        taPlainText.setPrefWidth(500);
        taPlainText.setPrefHeight(100);
        taPlainText.setWrapText(true);
        HBox hbPlainText = new HBox(5, lPlainText, taPlainText);

        Label lKey = new Label("Enter key to encrypt:");
        TextArea taKey = new TextArea();
        taKey.setPrefWidth(400);
        taKey.setPrefHeight(10);
        HBox hbKey = new HBox(5, lKey, taKey);

        taKey.setOnKeyTyped(event -> {
            String character = event.getCharacter();
            if (!character.matches("[01]")) {
                taKey.deletePreviousChar();
            }
            if (taKey.getText().length() > Logic.maxPower) {
                taKey.deletePreviousChar();
            }
        });


        Label lGenerateKey = new Label("Generate key:");
        TextArea taGenerateKey = new TextArea();
        taGenerateKey.setPrefWidth(500);
        taGenerateKey.setPrefHeight(100);
        taGenerateKey.setWrapText(true);
        HBox hbGenerateKey = new HBox(5, lGenerateKey, taGenerateKey);


        Label lEncryptText = new Label("Encrypt text:");
        TextArea taEncryptText = new TextArea();
        taEncryptText.setPrefWidth(500);
        taEncryptText.setPrefHeight(100);
        taEncryptText.setWrapText(true);
        HBox hbEncryptText = new HBox(5, lEncryptText, taEncryptText);

        Label lDecryptText = new Label("Decrypt text:");
        TextArea taDecryptText = new TextArea();
        taDecryptText.setPrefWidth(500);
        taDecryptText.setPrefHeight(100);
        taDecryptText.setWrapText(true);
        HBox hbDecryptText = new HBox(5, lDecryptText, taDecryptText);

        Button btnStart = new Button("Start");
        btnStart.setDisable(true);
        VBox vbKeyboardEncrypt = new VBox(10, btnChooseFile, hbPlainText, hbKey, btnStart, hbGenerateKey, hbEncryptText, hbDecryptText);
        vbKeyboardEncrypt.setAlignment(Pos.TOP_LEFT);

        Logic logic = new Logic();

        btnStart.setOnAction(actionEvent -> {
            if (taKey.getText().length() < Logic.maxPower) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid key");
                alert.showAndWait();
            } else {
                key=taKey.getText();
                logic.setRegister(key);
                try {
                    taEncryptText.setText(logic.encrypt().toString());
                    taGenerateKey.setText(logic.generateKey.toString());
                    taDecryptText.setText(logic.decrypt().toString());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        btnChooseFile.setOnAction(actionEvent -> {
            btnStart.setDisable(false);
            File input = fcChooseFile.showOpenDialog(stage);
            try {
               inputText = Files.readAllBytes(input.toPath());
               taPlainText.setText(logic.convertBytesToBits(inputText).toString());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int lastIndexOfDot = input.getPath().lastIndexOf('.');
            if (lastIndexOfDot != -1) {
                extension = input.getPath().substring(lastIndexOfDot + 1);
            }
        });

        Group root = new Group(vbKeyboardEncrypt);
        Scene scene = new Scene(root, 600, 600);
        stage.setScene(scene);
        stage.setTitle("lapa2");
        stage.show();


    }
}
