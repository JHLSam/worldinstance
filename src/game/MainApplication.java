import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.application.Application;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.canvas.GraphicsContext;
import javax.swing.JComboBox;
import java.util.Arrays;
import java.util.List;
//import java.awt.*;

public class MainApplication extends Application {


    public static void main(String[] args) {
        launch(args); //Static method inherited from Application
    } //Creates an object of this class and calls start()

    // Called to start doing application stuff
    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Block World");
        //Label lab = new Label("Hello ");
        // can only hold one "control"
        //border.setLeft(addVBox());
        //addStackPane(hbox); // Add stack to HBox in top region
        //border.setCenter(addGridPane());
        //Scene scene = new Scene(view.getScene());
        //stage.setHeight(700);
        //stage.setWidth(700);
        Model model = new Model();
        View view = new View(model);
        new Controller(view, model);
        stage.setScene(view.getScene());
        stage.show();
    }
}
