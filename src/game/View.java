import worldinstance.block.world.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * The View class for the Canvas Example
 * <p>
 * This class creates the GUI view and has methods which can update to the created view
 */
public class View {

    private Model model;
    private HBox rootBox;
    private GraphicsContext gfx;
    private Button[] buttons;
    private ChoiceBox choiceBox;
    private TextField xInput;
    private Button north;
    private Button south;
    private Button east;
    private Button west;
    private Button dig;
    private Button drop;
    private ChoiceBox<String> cb;
    private HBox inputBox;
    private MenuItem load;
    private MenuItem save;
    private MenuItem ex;

    /**
     * Constructor
     * <p>
     * When a view is created, start building the initial scene graph,
     * by adding all the necessary components.
     */
    public View(Model model) throws FileNotFoundException {
        rootBox = new HBox();
        addComponents();
        this.model = model;
    }

    /**
     * Method to record the values of choicebox(Cascading elements)
     */
    public String getChoice() {
        return cb.getValue();
    }

    /**
     * Method to record the values of the TextField
     */
    public String getDrop() {
        return xInput.getText();
    }

    /**
     * Method to define drawing of tiles and builder
     */
    public void drawGame() {
        Map<Tile, Position> tilePositionMap = model.getTilePositionMap();
        if (tilePositionMap == null) return;

        gfx.clearRect(0, 0, 450, 450);

        for (Tile tile : tilePositionMap.keySet()) {
            double x = (double) tilePositionMap.get(tile).getX();
            double y = (double) tilePositionMap.get(tile).getY();
            String colour = "";
            try {
                colour = tile.getTopBlock().getColour();
            } catch (TooLowException r) {
                colour = "orange";
            }
            gfx.setFill(Paint.valueOf(colour));
            gfx.fillRect(x, y, 45, 45);
            gfx.setStroke(Paint.valueOf("black"));
            gfx.strokeRect(x, y, 45, 45);
            int amount = tile.getBlocks().size();
            String textColour = "black";
            if (colour.equals(textColour))
                textColour = "white";
            gfx.setStroke(Paint.valueOf(textColour));
            gfx.strokeText("" + amount, x + 22.5, y + 22.5);
        }

        gfx.setFill(Paint.valueOf("yellow"));
        gfx.fillOval(model.getBPos().getX() + 22.5,
                model.getBPos().getY() + 22.5, 20, 20);

        fillInventory();
    }

    /**
     * Set value of property OnAction for clicking Save button
     */
    public void setSaveHandler(EventHandler<ActionEvent> handler) {
        save.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking Exit button
     */
    public void setExitHandler(EventHandler<ActionEvent> handler) {
        ex.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking Load button
     */
    public void setLoadHandler(EventHandler<ActionEvent> handler) {
        load.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking North button
     */
    public void setNorthHandler(EventHandler<ActionEvent> handler) {
        north.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking South button
     */
    public void setSouthHandler(EventHandler<ActionEvent> handler) {
        south.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking West button
     */
    public void setWestHandler(EventHandler<ActionEvent> handler) {
        west.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking East button
     */
    public void setEasthandler(EventHandler<ActionEvent> handler) {
        east.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking Dig button
     */
    public void setDighandler(EventHandler<ActionEvent> handler) {
        dig.setOnAction(handler);
    }

    /**
     * Set value of property OnAction for clicking Drop button
     */
    public void setDrophandler(EventHandler<ActionEvent> handler) {
        drop.setOnAction(handler);
    }
    /**
     * Set value of property OnAction for clicking ChoiceBox button
     */

    /*public ComboBox addComboBox() {
        //Create the combo box, select item at index 4.
        //Indices start at 0, so 4 specifies the pig.
        List<String> actionList = Arrays.asList("move builder", "move block",
                "drop", "dig");
        ComboBox cascadenow = new ComboBox();
        cascadenow.getItems().addAll(actionList);
        //cascadenow.setSelectedIndex(2);
        //cascadenow.addActionListener(this);
        return cascadenow;
    }*/

    /**
     * Method to create cascading "File" menubar with elements with elements "Load,Save,Exit"
     */
    public MenuBar addMenuBar() {
        Menu menu = new Menu("File");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu);
        load = new MenuItem("Load");
        save = new MenuItem("Save");
        ex = new MenuItem("Exit");
        menu.getItems().add(load);
        menu.getItems().add(save);
        menu.getItems().add(ex);
        return menuBar;
    }

    /**
     * Get the Scene of the GUI with the scene graph
     *
     * @return the current scene
     */
    public Scene getScene() {
        return new Scene(rootBox);
    }

    /**
     * /**
     * Adds the given handler to the given ith button
     *
     * @param handler the handler to add to the button
     */
    public void addButtonHandler(EventHandler<ActionEvent> handler) {
        /* Adds the handler to the setOnAction meaning when button is pressed
         * the handle() method inside this handler will be called
         */
        for (Button b : buttons) {
            b.setOnAction(handler);
        }
    }

    /**
     * Adds all the GUI elements to the root layout
     * <p>
     * These is where the scene graph is created
     */
    private void addComponents() throws FileNotFoundException {
        VBox leftBox = new VBox();
        leftBox.setPadding(new Insets(10, 10, 10, 10));
        leftBox.setStyle("-fx-background-color: white");
        addLeftSideComponents(leftBox);
        VBox rightBox = new VBox();
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(20, 20, 20, 20));
        rightBox.setStyle("-fx-background-color: #336699");
        addRightSideComponents(rightBox);
        rootBox.getChildren().addAll(leftBox, rightBox);
    }

    /**
     * Method to append objects to Builder's Inventory
     */
    private void fillInventory() {
        inputBox.getChildren().clear();
        Label inventory = new Label("Builder Inventory:");
        String inventoryList = "";

        if (model.getBuilderInventory() == null) return;

        for (String string : model.getBuilderInventory()) {
            inventoryList += string;
            inventoryList += " ";
        }
        Label rest = new Label(inventoryList);
        inputBox.getChildren().addAll(inventory, rest);
    }

    /**
     * Add all the Gui elements to the left container,
     * such as the canvas and the text fields
     *
     * @param box the container to add the elements to
     */
    public void addLeftSideComponents(VBox box) {

        /* add the canvas inside a HBox */
        HBox canvasContainer = new HBox();
        MenuBar menuBar = addMenuBar();
        Canvas canvas = new Canvas(450, 450);
        gfx = canvas.getGraphicsContext2D();
        canvasContainer.getChildren().add(canvas);
        canvasContainer.setStyle("-fx-border-color: black");
        /* the hBox (canvasContainer) is used so that border can be added around the canvas */
        /* Create another HBox and add textInputs and Labels inside it */
        inputBox = new HBox();
        inputBox.setPadding(new Insets(10, 10, 10, 10));
        inputBox.setSpacing(15);
        Label inventory = new Label("Builder Inventory:");
        /* Make everything inside the HBox Center aligned */
        inputBox.setAlignment(Pos.CENTER);
        inputBox.getChildren().addAll(inventory);
        /* Add another textField to the VBox to display previous drawn command
         * Could use a Label instead of a TextField, but I chose not to :P
         */
        // Make the testField look sexy and untouchable ;)
        /* add everything to the left VBox (which is passed as argument) */
        box.getChildren().addAll(menuBar, canvasContainer, inputBox);
    }

    /**
     * Add all the GUI Gridpane Buttons "North,East,South,West"
     */

    public GridPane addGridPane() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 10, 0, 10));
        // Category in column 2, row 1
        north = new Button("north");
        south = new Button("south");
        east = new Button("east");
        west = new Button("west");
        grid.add(west, 0, 1);
        grid.add(north, 1, 0);
        grid.add(south, 1, 2);
        grid.add(east, 2, 1);
        return grid;
    }

    /**
     * Add all the GUI elements to the right container,
     * which consists of all the buttons
     * s
     * the container to add the elements to
     */
    private void addRightSideComponents(VBox box) throws FileNotFoundException {

        /* Add some text to indicate what buttons are for*/
        Text drawText = new Text("Game Commands");
        drawText.setFont(Font.font("Verdana", FontWeight.BOLD, 11));
        drawText.setFill(Color.WHITE);
        GridPane grid = addGridPane();
        grid.setTranslateX(50);
        grid.setDisable(false);
        List<String> actionList = Arrays.asList("move builder", "move block");
        cb = new ChoiceBox<>();
        cb.getItems().addAll(actionList);
        cb.setValue("move builder");
        cb.setTranslateX(80);
        cb.setDisable(false);
        HBox inputBoxx = new HBox();
        inputBoxx.setPadding(new Insets(10, 10, 10, 10));
        inputBoxx.setSpacing(15);
        xInput = new TextField();
        xInput.setPromptText("Drop Index");
        dig = new Button("Dig");
        dig.setTranslateX(10);
        dig.setDisable(false);
        drop = new Button("Drop");
        drop.setDisable(false);
        inputBoxx.getChildren().addAll(drop, xInput);
        box.getChildren().addAll(drawText, grid, cb, dig, inputBoxx);
    }
}


