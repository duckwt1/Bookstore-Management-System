module main.java2final {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires jakarta.persistence;
    requires org.hibernate.orm.core;
    requires java.naming;
    requires jakarta.mail;

    opens main.java2final to javafx.fxml;
    exports main.java2final;
    exports controller;
    opens controller to javafx.fxml;
    opens dto to org.hibernate.orm.core, javafx.base;
}