module com.example.cedintegradora {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;


    opens com.example.cedintegradora to javafx.fxml;
    opens com.example.cedintegradora.test to junit;
    exports com.example.cedintegradora to javafx.graphics;
}