module espol.poo.amigos4patas {
    requires javafx.controls;
    requires javafx.fxml;

    opens espol.poo.amigos4patas to javafx.fxml;
    exports espol.poo.amigos4patas;
}
