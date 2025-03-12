module com.kozyr.adms.l1 {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires dagger;

    opens com.kozyr.adms.l1 to javafx.fxml;
    exports com.kozyr.adms.l1.ui to java.base;
    exports com.kozyr.adms.l1 to javafx.graphics;
    opens com.kozyr.adms.l1.ui to javafx.fxml;
    exports com.kozyr.adms.l1.utils to javafx.graphics;
    opens com.kozyr.adms.l1.utils to javafx.fxml;
}