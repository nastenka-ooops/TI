package com.example.lapa4;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

import static java.nio.file.StandardOpenOption.APPEND;

public class Main extends Application {
    int p;
    int q;
    int r;
    int d;
    int eulerFunction;
    int hash;
    int EDC;
    byte[] inputText;
    int[] intInputText;
    String sighStart = "This is start of RSA EDC";
    String sighEnd = "This is end of RSA EDC";

    Logic logic = new Logic();

    @Override
    public void start(Stage stage) throws Exception {
        FileChooser fcChooseFile = new FileChooser();

        Label lP = new Label("  p: ");
        TextField tfP = new TextField();
        HBox hbP = new HBox(5, lP, tfP);

        Label lQ = new Label("  q: ");
        TextField tfQ = new TextField();
        HBox hbQ = new HBox(5, lQ, tfQ);

        Label lD = new Label("  d: ");
        TextField tfD = new TextField();
        HBox hbD = new HBox(5, lD, tfD);

        Label lTextHash = new Label("  text hash: ");
        TextField tfTextHash = new TextField();
        HBox hbTextHash = new HBox(5, lTextHash, tfTextHash);

        Label lEDC = new Label("  EDC: ");
        TextField tfEDC = new TextField();
        HBox hbEDC = new HBox(5, lEDC, tfEDC);


        Button btnEnterP = new Button("Enter p");

        Button btnEnterQ = new Button("Enter q");
        btnEnterQ.setDisable(true);

        Button btnEnterPrivateKey = new Button("Enter private key");
        btnEnterPrivateKey.setDisable(true);

        Button btnSingFile = new Button("Sign file");
        btnSingFile.setDisable(true);

        Button btnCheckSign = new Button("Check sign");
        btnCheckSign.setDisable(true);

        HBox buttons = new HBox(10, btnSingFile, btnCheckSign);
        buttons.setAlignment(Pos.BOTTOM_CENTER);

        btnEnterP.setOnAction(event -> {
            int tempP;
            try {
                tempP = Integer.parseInt(tfP.getText());
                if (logic.isPrimeNumber(tempP)) {
                    p = tempP;
                    btnEnterQ.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("p is set");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("p is not prime number");
                    alert.showAndWait();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid p");
                alert.showAndWait();
            }
        });

        btnEnterQ.setOnAction(event -> {
            int tempQ;
            try {
                tempQ = Integer.parseInt(tfQ.getText());
                if (logic.isPrimeNumber(tempQ)) {
                    q = tempQ;
                    r = q * p;
                    btnEnterPrivateKey.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("q is set");
                    alert.showAndWait();
                } else {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("q is not prime number");
                    alert.showAndWait();
                }
            } catch (NumberFormatException | IndexOutOfBoundsException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid q");
                alert.showAndWait();
            }
        });

        btnEnterPrivateKey.setOnAction(event -> {
            int tempD;
            try {
                tempD = Integer.parseInt(tfD.getText());
                eulerFunction = (p - 1) * (q - 1);
                if (tempD <= 1 || tempD >= eulerFunction || !logic.isRelativelyPrime(tempD, eulerFunction)) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("invalid d");
                    alert.showAndWait();
                } else {
                    d = tempD;
                    btnSingFile.setDisable(false);
                    btnCheckSign.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("d is set");
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("invalid d");
                alert.showAndWait();
            }
        });

        btnSingFile.setOnAction(event -> {
            File unsignedFile = fcChooseFile.showOpenDialog(stage);

            try {
                inputText = Files.readAllBytes(unsignedFile.toPath());
                intInputText = logic.fromByteArray2IntArray(inputText);


                hash = logic.getHash(intInputText, r);
                tfTextHash.setText(String.valueOf(hash));

                EDC = logic.powerMod(hash, d, r);
                tfEDC.setText(String.valueOf(EDC));

                File signedFile = new File("./signedFile.txt");

                Files.write(signedFile.toPath(), inputText);

                Files.writeString(signedFile.toPath(), sighStart, APPEND);

                Files.writeString(signedFile.toPath(), String.valueOf(EDC), APPEND);

                Files.writeString(signedFile.toPath(), sighEnd, APPEND);

            }catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        btnCheckSign.setOnAction(event -> {
            File signedFile = fcChooseFile.showOpenDialog(stage);

            try {
                BufferedReader bufferedReader = new BufferedReader(new FileReader(signedFile));

                String line = bufferedReader.readLine();
                StringBuilder input = new StringBuilder();
                while (line!=null){
                    input.append(line);
                    line = bufferedReader.readLine();
                }

                int start = input.lastIndexOf(sighStart);
                int end =input.lastIndexOf(sighEnd);
                try {

                    int sign = Integer.parseInt(input.substring(start + sighStart.length(), end));

                    int e = logic.EEA(eulerFunction, d)[1];
                    if (e < 0) {
                        e += eulerFunction;
                    }

                    int signHash = logic.powerMod(sign, e, r);

                    byte[] byteInput = input.substring(0, start).getBytes(StandardCharsets.UTF_8);
                    int[] intInput = logic.fromByteArray2IntArray(byteInput);

                    int textHah = logic.getHash(intInput, r);

                    if (signHash==textHah){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("sign is correct\n"+signHash+" = "+ textHah);
                        alert.showAndWait();
                    } else {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("sign is not correct\n"+signHash+" != "+ textHah);
                        alert.showAndWait();
                    }

                } catch (NumberFormatException e) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("file is not signed");
                    alert.showAndWait();
                }


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        VBox root = new VBox(10, hbP, btnEnterP, hbQ, btnEnterQ, hbD, btnEnterPrivateKey, buttons, hbTextHash,
                hbEDC);
        Scene scene = new Scene(root, 300, 350);
        stage.setScene(scene);
        stage.setTitle("lapa2");
        stage.show();

    }
}
