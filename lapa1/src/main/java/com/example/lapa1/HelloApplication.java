package com.example.lapa1;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Box;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Objects;

public class HelloApplication extends Application {
    final String russianAlphabet = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private String encryptRailwayMethod (int key, StringBuilder text){
        StringBuilder encryptText = new StringBuilder();
        int period = (key - 1) * 2;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                int remainder;
                if (period != 0)
                    remainder = j % period;
                else
                    remainder = 0;

                int index = key - 1 - Math.abs(key - 1 - remainder);
                if (index == i) {
                    encryptText.append(text.charAt(j));
                }
            }
        }
        return new String(encryptText);
    }
    private String decryptRailwayMethod(int key, String text){
        StringBuilder decryptText = new StringBuilder();
        int[] numbs = new int[text.length()];


        int period = (key - 1) * 2;
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < text.length(); j++) {
                int remainder;
                if (period != 0)
                    remainder = j % period;
                else
                    remainder = 0;

                int index = key - 1 - Math.abs(key - 1 - remainder);
                if (index == i) {
                    numbs[j]=index;
                }
            }
        }
        Arrays.sort(numbs);

        boolean down = true;
        int l =0;

        while (decryptText.length()!=text.length()){
            for (int i = 0; i < text.length(); i++) {
                if (numbs[i]==l){
                    numbs[i]=-1;
                    decryptText.append(text.charAt(i));
                    break;
                }
            }
            if (l==0){
                down=true;
            }
            if(l==key-1){
                down=false;
            }
            if (down){
                l++;
            } else
                l--;
            if (key==1){
                l=0;
            }
        }
        return new String(decryptText);
    }
    private String[] railwayMethod (String text, String key) {
        StringBuilder startText = new StringBuilder(text);
        StringBuilder startKey = new StringBuilder(key);
        String[] res = new String[2];

        for (int i = 0; i < startKey.length(); ) {
            if (!(startKey.charAt(i) >= '0' && startKey.charAt(i) <= '9')) {
                startKey.deleteCharAt(i);
            } else
                i++;
        }
        try {
            int encryptKey = Integer.parseInt(new String(startKey));

            if (encryptKey != 0) {
                for (int i = 0; i < startText.length(); ) {
                    if (!((startText.charAt(i) >= 'A' && startText.charAt(i) <= 'Z') ||
                            (startText.charAt(i) >= 'a' && startText.charAt(i) <= 'z'))) {
                        startText.deleteCharAt(i);
                    } else
                        i++;
                }
                res[0] = encryptRailwayMethod(encryptKey, startText);
                res[1] = decryptRailwayMethod(encryptKey, res[0]);
                return res;

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setContentText("Invalid key");
                alert.showAndWait();
                return res;
            }
        } catch (Exception exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Invalid key");
            alert.showAndWait();
            return res;
        }
    }

    private String encryptVigenereAlgorithm (String text, String key){
        StringBuilder encryptText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
                int textNumber = russianAlphabet.indexOf(text.charAt(i));
                int keyNumber = russianAlphabet.indexOf(key.charAt(i%key.length()));
                encryptText.append(russianAlphabet.charAt((textNumber+keyNumber)%russianAlphabet.length()));
        }
        return new String(encryptText);
    }
    private String decryptVigenereAlgorithm (String text, String key){
        StringBuilder decryptText = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            int textNumber = russianAlphabet.indexOf(text.charAt(i));
            int keyNumber = russianAlphabet.indexOf(key.charAt(i%key.length()));
            decryptText.append(russianAlphabet.charAt((textNumber+russianAlphabet.length()-keyNumber)%russianAlphabet.length()));
        }
        return new String(decryptText);
    }
    private String[] vigenereAlgorithm (String text, String key) {
        StringBuilder startText = new StringBuilder(text.toUpperCase());
        StringBuilder startKey = new StringBuilder(key.toUpperCase());
        String[] res = new String[2];

        for (int i = 0; i < startText.length(); ) {
            if (russianAlphabet.indexOf(startText.charAt(i))<0){
                startText.deleteCharAt(i);
            } else
                i++;
        }

        for (int i = 0; i < startKey.length(); ) {
            if (russianAlphabet.indexOf(startKey.charAt(i))<0){
                startKey.deleteCharAt(i);
            } else
                i++;
        }
        
        try {
            String encryptKey = new String(startKey);
            String encryptText = new String(startText);
            res[0]=encryptVigenereAlgorithm(encryptText, encryptKey);
            res[1]=decryptVigenereAlgorithm(res[0], encryptKey);

            return res;
                
        } catch (Exception exception){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Invalid data");
            alert.showAndWait();
            return res;
        }
    }

    @Override
    public void start(Stage stage) throws IOException {
        RadioButton rbChooseInput = new RadioButton("Fail input");

        Label lEncryptMethod = new Label("Choose of encryption:");
        ChoiceBox<String> cbEncryptMethod = new ChoiceBox<>(FXCollections.observableArrayList("метод «железнодорожной изгороди», текст на английском языке", "алгоритм Виженера, прямой ключ, текст на русском языке"));
        cbEncryptMethod.getSelectionModel().selectFirst();
        HBox hbEncryptMethod = new HBox(5, lEncryptMethod, cbEncryptMethod);

        Button btnChooseFile = new Button("Choose file");
        btnChooseFile.setDisable(true);
        FileChooser fcChooseFile = new FileChooser();

        Label lText = new Label("Enter text to encrypt:");
        TextField tfText = new TextField();
        HBox hbText = new HBox(5, lText, tfText);

        Label lKey = new Label("Enter key to encrypt:");
        TextField tfKey = new TextField();
        HBox hbKey = new HBox(5, lKey, tfKey);

        Label lEncryptText = new Label("Encrypt text:");
        TextField tfEncryptText = new TextField();
        tfEncryptText.setPrefWidth(500);
        HBox hbEncryptText = new HBox(5, lEncryptText, tfEncryptText);

        Label lDecryptText = new Label("Decrypt text:");
        TextField tfDecryptText = new TextField();
        tfDecryptText.setPrefWidth(500);
        HBox hbDecryptText = new HBox(5, lDecryptText, tfDecryptText);

        Button btnStart = new Button("Start");
        VBox vbKeyboardEncrypt = new VBox(10, rbChooseInput, hbEncryptMethod, btnChooseFile, hbText, hbKey, btnStart, hbEncryptText, hbDecryptText);
        vbKeyboardEncrypt.setAlignment(Pos.TOP_LEFT);

        rbChooseInput.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                tfText.setDisable(rbChooseInput.isSelected());
                btnChooseFile.setDisable(!rbChooseInput.isSelected());
            }
        });

        final File[] input = new File[1];
        btnChooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                 input[0] = fcChooseFile.showOpenDialog(stage);
            }
        });

        btnStart.setOnAction(new EventHandler<ActionEvent>(){
            @Override
            public void handle(ActionEvent actionEvent) {
                StringBuilder text = new StringBuilder();
                if (!rbChooseInput.isSelected()){
                text = new StringBuilder(tfText.getText());
                }
                else {
                    try{
                        BufferedReader reader = new BufferedReader(new FileReader(input[0]));
                        String buff = reader.readLine();
                        while (buff!=null){
                            text.append(buff);
                            buff = reader.readLine();
                        }
                        reader.close();
                    }catch (IOException e){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invalid data");
                        alert.showAndWait();
                    }
                }
                String key = tfKey.getText();
                String[] res;
                if (cbEncryptMethod.getSelectionModel().getSelectedIndex()==0){
                     res = railwayMethod(new String(text),key);
                } else {
                     res = vigenereAlgorithm(new String(text),key);
                }
                    if(Objects.equals(res[0], "") || Objects.equals(res[1], "")){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setContentText("Invalid data");
                        alert.showAndWait();
                    }
                    else{
                        tfEncryptText.setText(res[0]);
                        tfDecryptText.setText(res[1]);
                            try {
                                FileWriter writer = new FileWriter("C:\\Users\\madam\\OneDrive\\Рабочий стол\\output.txt", true);
                                writer.write(res[0]);
                                writer.write("\n");
                                writer.write(res[1]);
                                writer.write("\n");
                                writer.close();
                            } catch (Exception e){
                        }
                    }
            }
        }
        );

        Group root = new Group(vbKeyboardEncrypt);
        Scene scene = new Scene(root, 600, 300);
        stage.setScene(scene);
        stage.setTitle("lapa1");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}