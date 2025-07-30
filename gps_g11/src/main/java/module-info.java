module com.example.gps_g11 {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires junit;
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires org.json;
    requires okhttp3;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires com.google.gson;
    requires com.rometools.rome;
    requires java.desktop;

    opens com.example.gps_g11 to javafx.fxml;
    exports com.example.gps_g11;
    exports com.example.gps_g11.survey;
    opens com.example.gps_g11.survey to javafx.fxml;
    exports com.example.gps_g11.llm;
    opens com.example.gps_g11.llm to javafx.fxml;
    exports com.example.gps_g11.Articles;
    opens com.example.gps_g11.Articles to javafx.fxml;
    exports com.example.gps_g11.Tasks;
    opens com.example.gps_g11.Tasks to javafx.fxml;
    exports com.example.gps_g11.LoginSignUp;
    opens com.example.gps_g11.LoginSignUp to javafx.fxml;
    exports com.example.gps_g11.DB;
    opens com.example.gps_g11.DB to javafx.fxml;
    exports com.example.gps_g11.PlantsDetails;
    opens com.example.gps_g11.PlantsDetails to javafx.fxml;
    exports com.example.gps_g11.GardenPlant;
    opens com.example.gps_g11.GardenPlant to javafx.fxml;
    exports com.example.gps_g11.Home;
    opens com.example.gps_g11.Home to javafx.fxml;
    exports com.example.gps_g11.Profile;
    opens com.example.gps_g11.Profile to javafx.fxml;

}