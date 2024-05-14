module com.example.lapa4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lapa4 to javafx.fxml;
    exports com.example.lapa4;
}