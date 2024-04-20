module com.example.lapa3 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lapa3 to javafx.fxml;
    exports com.example.lapa3;
}