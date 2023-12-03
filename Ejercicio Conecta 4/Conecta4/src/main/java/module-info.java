module com.example.conecta4 {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.conecta4 to javafx.fxml;
    exports com.example.conecta4;
}