import worldinstance.block.world.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.events.Event;
import sun.java2d.loops.ProcessPath;
import sun.rmi.server.LoaderHandler;

import java.io.*;

/**
 * The Controller class for the GUI
 * <p>
 * Used to control the View depending on user input
 */
public class Controller {

    /* The view of our application */
    private View view;
    private Model model;
    private BufferedWriter writer;

    /**
     * Constructor
     * When a Controller is created, add the EventHandlers to the view
     *
     * @param view The view for this application, Model
     */
    public Controller(View view, Model model) {
        this.view = view;
        this.model = model;
        view.setEasthandler(new MoveHandler("east"));
        view.setNorthHandler(new MoveHandler("north"));
        view.setWestHandler(new MoveHandler("west"));
        view.setSouthHandler(new MoveHandler("south"));
        view.setDighandler(new DigHandler());
        view.setDrophandler(new DropHandler());
        view.setLoadHandler(new LoadHandler());
        view.setSaveHandler(new SaveHandler());
        view.setExitHandler(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.exit(0);
            }
        });
    }

    /**
     * Eventhandler class that handles behaviour of Save button in choicebox
     */
    private class SaveHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showSaveDialog(new Stage());
            try {
                model.saveMap(file.getPath());
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Eventhandler class that handles behaviour of Load button in choicebox
     */
    private class LoadHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(new Stage());
            try {
                model.loadMap(file.getPath());
                view.drawGame();
            } catch (WorldMapInconsistentException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            } catch (WorldMapFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            } catch (FileNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.showAndWait();
            }
        }
    }

    /**
     * Eventhandler class that handles behaviour of operating Drop button
     */
    private class DropHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            String xinput = view.getDrop();

            try {
                int index = Integer.parseInt(xinput);
                model.drop(index);
                view.drawGame();

            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Number Format Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Must enter integer!");
                alert.showAndWait();

            } catch (InvalidBlockException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Block Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Invalid Tile");
                alert.showAndWait();
                
            } catch (TooHighException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Too high tile");
                alert.showAndWait();
            }
        }
    }

    /**
     * Eventhandler class that handles behaviour of operating Dig button
     */
    private class DigHandler implements EventHandler<ActionEvent> {

        @Override
        public void handle(ActionEvent event) {
            try {
                model.dig();
                view.drawGame();
            } catch (InvalidBlockException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Undiggable block");
                alert.showAndWait();
            } catch (TooLowException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Too low tile");
                alert.showAndWait();
            }
        }
    }

    /**
     * Eventhandler class that handles behaviour of operating GridPane buttons
     */
    private class MoveHandler implements EventHandler<ActionEvent> {
        private String exit;

        public MoveHandler(String exit) {
            this.exit = exit;
        }

        @Override
        public void handle(ActionEvent event) {
            String choice = view.getChoice();
            try {
                if (choice.equals("move builder")) {
                    model.moveBuilder(exit);
                } else {
                    model.moveBlock(exit);
                }
                view.drawGame();
            } catch (NoExitException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("cannot exit tile. Too High/Low");
                alert.showAndWait();

            } catch (InvalidBlockException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(e.getMessage());
                alert.setContentText("Invalid Block");
                alert.showAndWait();

            } catch (TooHighException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Height out of range exception");
                alert.showAndWait();
            }
        }
    }
}

