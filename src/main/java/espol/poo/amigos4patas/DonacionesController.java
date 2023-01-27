package espol.poo.amigos4patas;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import modelo.Causa;
import modelo.Donacion;
import modelo.DonacionInavlidaException;

public class DonacionesController implements Initializable {

    @FXML
    //Contenedor donde se cargan la informacion de las cuasas
    private FlowPane panelCausas;
    @FXML
    //Label donde se muestra el nombre de la causa seleccionada
    private Label labelCausa;
    @FXML
    //TextField del nombre del donador
    private TextField txtNombre;
    @FXML
    //TextField del monto
    private TextField txtCantidad;

    private Causa causaSeleccionda;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        
        
        //llamar a llenar panel en hilo
        Thread t = new Thread( () ->
        {
            while (true){
                Platform.runLater( () -> llenarPanelCausas());
                //se utiliza runlater para hacer los cambios en la interfaz grafica
                //algunos controles como el label a veces no permiten que en el hilo se hagan los cambios
                
                try {
                    Thread.sleep(1000); //dormir 5 min, por ahora 1 para probar
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }
        }
                 );
        
        t.setDaemon(true);
        t.start();
        
        
    }

    /**
     * Metodo que carga en el contenedor panelCausas y agrega la informacion de
     * cada causa
     *
     */
    private void llenarPanelCausas() {
        panelCausas.getChildren().clear();//limpiar el panel
        //consultar las causas
        List<Causa> causas = Causa.leerCausas();

        for (Causa c : causas) {
            //para cada causa hay que crear un vbox y los otro controles que van en el

            VBox vbCausa = new VBox(5);

            //crear imageview
            ImageView iv = new ImageView();
            try {
                //crear imagen
                String ruta = App.class.getResource("img/" + c.getImagen()).getPath();
                FileInputStream is = new FileInputStream(ruta);
                Image image = new Image(is, 150, 150, false, false);
                iv.setImage(image);

                //manejar el evento sobre el imageview
                iv.setOnMouseClicked(ev -> {
                    //se debe mostrar la causa seleccionada en labelCausa y fija causa causaSeleccionda
                    causaSeleccionda = c;
                    labelCausa.setText("Causa seleccionada: " + c.getNombre());
                });

            } catch (Exception e) {
                System.out.println("error al cargar la imagen" + e.getMessage());
            }

            //label nombre causa
            Label lbNombre = new Label(c.getNombre());
            //recuperar el total de la causa
            double total = c.calcularTotal();
            //label para el total
            Label lbTotal = new Label("Recaudado $" + total);
            //agregar controles al vbox

            vbCausa.getChildren().addAll(iv, lbNombre, lbTotal);
            //agregar causa al panel
            panelCausas.getChildren().add(vbCausa);
        }

    }

    @FXML
    /**
     * Este metodo esta fijado al evento ActionEvent del boton enviar
     */
    private void realizarDonacion(ActionEvent event) {
        try {

            Donacion.escribirDonacion(causaSeleccionda, txtNombre.getText(), Double.valueOf(txtCantidad.getText()));

            //mostrar alerta que todo sale bien
            mostrarAlerta(AlertType.INFORMATION, "Donacion guardada exitosamente");

            //limpiar las cajas
            txtNombre.setText("");
            txtCantidad.setText("");
        } catch (IOException ex) {
            mostrarAlerta(AlertType.ERROR, ex.getMessage());
        } catch (DonacionInavlidaException ex) {
           mostrarAlerta(AlertType.ERROR, ex.getMessage());
        } catch (Exception ex) {
           mostrarAlerta(AlertType.ERROR, ex.getMessage());
        }
    }

    private void mostrarAlerta(AlertType tipo, String mensaje){
                    Alert alert = new Alert(tipo);

            alert.setTitle("Resultado de escribir donacion");
            alert.setHeaderText("Operacion");
            alert.setContentText(mensaje);
            alert.showAndWait();
    }
}
