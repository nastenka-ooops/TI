module com.example.lapa1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.lapa1 to javafx.fxml;
    exports com.example.lapa1;
}