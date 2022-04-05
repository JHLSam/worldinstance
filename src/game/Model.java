import worldinstance.block.world.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * The Model class of the MVC Model.
 * <p>
 * This class defines the behaviour of the game, which is reflected to the GUI
 * via the View class
 */
public class Model {

    private WorldMap worldMap;
    private final String[] EXITS = {"north", "east", "south", "west"};
    private final int[] DIRECTIONS_X = {0, 45, 0, -45};
    private final int[] DIRECTIONS_Y = {-45, 0, 45, 0};
    private static boolean LOADSUCCESS = false;

    public Model() {

    }

    public void moveBuilder(String exit) throws NoExitException {
        if (!Model.LOADSUCCESS)
            return;

        Tile toMove = worldMap.getBuilder().getCurrentTile().getExits()
                .getOrDefault(exit, null);
        if (toMove != null) {
            worldMap.getBuilder().moveTo(toMove);
        }
        // worldMap.getBuilder().moveTo(worldMap.getBuilder().getCurrentTile().getExits().getOrDefault(exit, null));
    }

    public void moveBlock(String exit) throws NoExitException,
            InvalidBlockException, TooHighException {
        if (!Model.LOADSUCCESS)
            return;
        worldMap.getBuilder().getCurrentTile().moveBlock(exit);
    }

    public void dig() throws InvalidBlockException, TooLowException {
        if (!Model.LOADSUCCESS)
            return;
        worldMap.getBuilder().digOnCurrentTile();
    }

    public void drop(int index) throws TooHighException, InvalidBlockException {
        if (!Model.LOADSUCCESS)
            return;
        worldMap.getBuilder().dropFromInventory(index);
    }

    public void loadMap(String fileName) throws WorldMapInconsistentException,
            WorldMapFormatException, FileNotFoundException {

        worldMap = new WorldMap(fileName);
        Model.LOADSUCCESS = true;
    }

    /*public LinkedList<String> getTileType() {
        String file = new File();
    }*/

    public List<String> getBuilderInventory() {
        if (!Model.LOADSUCCESS)
            return null;
        List<String> strings = new ArrayList<>();
        for (Block block : worldMap.getBuilder().getInventory()) {
            strings.add(block.getBlockType());
        }
        return strings;
    }

    public void saveMap(String fileName) throws IOException {
        if (!Model.LOADSUCCESS)
            return;
        worldMap.saveMap(fileName);
    }

    public Map<Tile, Position> getTilePositionMap() {
        if (!Model.LOADSUCCESS)
            return null;

        Map<Tile, Position> tilePositionHashMap = new HashMap<>();

        tilePositionHashMap.put(worldMap.getTiles().get(0),
                new Position(225, 225));
        Set<Tile> visited = new HashSet<>();
        List<Tile> queue = new ArrayList<>();
        queue.add(worldMap.getTiles().get(0));

        while (!queue.isEmpty()) {
            Tile visit = queue.remove(0);
            Position position = tilePositionHashMap.get(visit);

            for (int i = 0; i < DIRECTIONS_X.length; i++) {
                Tile neighbour = visit.getExits()
                        .getOrDefault(EXITS[i], null);
                if (neighbour == null) continue;
                if (visited.contains(neighbour)) continue;
                if (queue.contains(neighbour)) continue;
                Position position1 = new Position(position.getX()
                        + DIRECTIONS_X[i],
                        position.getY() + DIRECTIONS_Y[i]);
                tilePositionHashMap.put(neighbour, position1);
                queue.add(neighbour);
            }
            visited.add(visit);
        }
        return tilePositionHashMap;
    }

    public Position getBPos() {
        if (!Model.LOADSUCCESS)
            return null;
        return getTilePositionMap().get(worldMap.getBuilder().getCurrentTile());
    }
}



