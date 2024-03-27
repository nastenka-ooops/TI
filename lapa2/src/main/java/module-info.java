module com.example.lapa2 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lapa2 to javafx.fxml;
    exports com.example.lapa2;
}