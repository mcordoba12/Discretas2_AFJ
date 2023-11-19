module com.example.cedintegradora {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.cedintegradora to javafx.fxml;
    exports com.example.cedintegradora to javafx.graphics;
}