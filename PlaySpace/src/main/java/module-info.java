module com.example.demo4 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;


    opens com.example.PlaySpace to javafx.fxml;
    exports com.example.PlaySpace;
}