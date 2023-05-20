module org.Hamm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.xml.bind;

    requires transitive java.activation;
    opens org.Hamm to javafx.fxml;
    exports org.Hamm;
    exports org.Hamm.Controller;
    exports org.Hamm.Test;
    opens org.Hamm.Model.Connections to java.xml.bind;
    opens org.Hamm.Controller to javafx.fxml;
    opens org.Hamm.Model.DAO to javafx.fxml;
    opens org.Hamm.Model.Domain to javafx.base;
    opens org.Hamm.Test to javafx.fxml;
}
