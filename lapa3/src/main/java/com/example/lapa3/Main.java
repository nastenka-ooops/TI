package com.example.lapa3;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    int p;
    int k;
    int x;
    int y;
    int g;
    byte[] inputText;
    byte[] signs;
    ArrayList<Integer> encryptText = new ArrayList<>();
    ArrayList<Byte> decryptText = new ArrayList<>();
    public static String extension;

    Random random = new Random();
    Logic logic = new Logic();
    File encryptFile = new File("encryptFile.txt");
    File decryptFile;

    @Override
    public void start(Stage stage) throws Exception {
        Button btnChooseFile = new Button("Choose file for encrypt");
        FileChooser fcChooseFile = new FileChooser();

        Label lP = new Label("  p: ");
        TextField tfP = new TextField();
        HBox hbP = new HBox(5, lP, tfP);

        Label lX = new Label("  x: ");
        TextField tfX = new TextField();
        HBox hbX = new HBox(5, lX, tfX);

        Label lK = new Label("  k: ");
        TextField tfK = new TextField();
        HBox hbK = new HBox(5, lK, tfK);

        Label lEncryptText = new Label("  encrypt text: ");
        TextArea taEncryptText = new TextArea();
        taEncryptText.setWrapText(true);
        HBox hbEncryptText = new HBox(5, lEncryptText, taEncryptText);

        Label lDecryptText = new Label("  decrypt text: ");
        TextArea taDecryptText = new TextArea();
        taDecryptText.setWrapText(true);
        HBox hbDecryptText = new HBox(5, lDecryptText, taDecryptText);

        Label lY = new Label("  y: ");
        TextField tfY = new TextField();
        HBox hbY = new HBox(5, lY, tfY);


        Button btnCalculatePublicKey = new Button("Calculate public key");

        Button btnEnterPrivateKey = new Button("Enter private key");
        btnEnterPrivateKey.setDisable(true);

        Button btnEnterSecretNumber = new Button("Enter secret number");
        btnEnterSecretNumber.setDisable(true);

        Button btnStart = new Button("Encrypt");
        btnStart.setDisable(true);

        Label lPrimitiveRoots = new Label("  primitive roots: ");
        ComboBox<Integer> cbPrimitiveRoots = new ComboBox<>();
        cbPrimitiveRoots.setDisable(true);
        TextField tfCount= new TextField();
        HBox hbPrimitiveRoots = new HBox(5, lPrimitiveRoots, cbPrimitiveRoots, tfCount);

        btnChooseFile.setOnAction(actionEvent -> {
            btnStart.setDisable(false);
            File input = fcChooseFile.showOpenDialog(stage);
            try {
                inputText = Files.readAllBytes(input.toPath());
                signs =new byte[inputText.length];
                for (int i = 0; i < signs.length; i++) {
                    if (inputText[i]<0){
                        signs[i]=-1;
                        inputText[i]= (byte) -inputText[i];
                    } else {
                        signs[i]=1;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            int lastIndexOfDot = input.getPath().lastIndexOf('.');
            if (lastIndexOfDot != -1) {
                extension = input.getPath().substring(lastIndexOfDot + 1);
            }
        });

        btnCalculatePublicKey.setOnAction(event -> {
            int tempP;
            try {
                tempP = Integer.parseInt(tfP.getText());
                if (logic.isPrimeNumber(tempP)) {
                    p = tempP;
                    cbPrimitiveRoots.setItems(FXCollections.observableList(logic.findAllPrimitiveRoots(p, logic.findAllPrimeDivisors(p - 1))));

                    cbPrimitiveRoots.setValue(cbPrimitiveRoots.getItems().get(0));
                    cbPrimitiveRoots.setDisable(false);
                    btnEnterPrivateKey.setDisable(false);
                    btnEnterSecretNumber.setDisable(false);
                    tfCount.setText(String.valueOf(logic.findAllPrimitiveRoots(p, logic.findAllPrimeDivisors(p - 1)).size()));
                }
                    else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("p is not prime number");
                    alert.showAndWait();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e ) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid p");
                alert.showAndWait();
            }
        });

        btnEnterPrivateKey.setOnAction(event -> {
            int tempX;
            try {
                tempX = Integer.parseInt(tfX.getText());
                if (tempX >= p - 1 || tempX == 1) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("invalid x");
                    alert.showAndWait();
                } else {
                    x = tempX;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("x is set");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid x");
                alert.showAndWait();
            }
        });

        btnEnterSecretNumber.setOnAction(event -> {
            int tempK;
            try {
                tempK = Integer.parseInt(tfK.getText());
                if ((tempK>0 && tempK<p) && !logic.isRelativelyPrime(tempK, p-1)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("invalid k");
                    alert.showAndWait();
                } else {
                    k = tempK;
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("k is set");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid k");
                alert.showAndWait();
            }
        });

        btnStart.setOnAction(event -> {
            g = cbPrimitiveRoots.getValue();

            y = logic.powerMod(g, x, p);
            tfY.setText(String.valueOf(y));

            int randomNumber=0;

            for (byte value : inputText) {
                int a = logic.powerMod(g, k + randomNumber, p);
                int b = logic.powerModWithMultiply(y, k+randomNumber, value, p);
                encryptText.add(a);
                encryptText.add(b);
                do {
                    randomNumber = random.nextInt(100);
                } while ((k+randomNumber>0 && k+randomNumber<p) && !logic.isRelativelyPrime(k+randomNumber,p-1 ));
            }
            taEncryptText.setText(encryptText.toString());


            try {
                Files.write(encryptFile.toPath(), logic.fromArrayListOfIntegersToArrayOfBytes(encryptText));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


            for (int i = 0; i < encryptText.size(); i+=2) {
                byte temp = (byte) logic.powerModWithMultiply(encryptText.get(i),-x, encryptText.get(i+1),p);
                decryptText.add(temp);
            }

            for (int i = 0; i < decryptText.size(); i++) {
                if (signs[i]<0){
                    decryptText.set(i, (byte) -decryptText.get(i));
                }
            }
            taDecryptText.setText(decryptText.toString());
            decryptFile = new File("decryptFile."+extension);
            try {
                Files.write(decryptFile.toPath(),logic.fromArrayListOfBytesToArrayOfBytes(decryptText));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        });

        VBox root = new VBox(10, hbP, btnCalculatePublicKey, hbPrimitiveRoots, hbX, btnEnterPrivateKey,
                hbK,btnEnterSecretNumber, btnChooseFile, btnStart, hbY, hbEncryptText, hbDecryptText);
        Scene scene = new Scene(root, 600, 730);
        stage.setScene(scene);
        stage.setTitle("lapa2");
        stage.show();

    }
}
