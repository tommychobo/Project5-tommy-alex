import processing.core.PImage;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * Represents the 2D World in which this simulation is running.
 * Keeps track of the size of the world, the background image for each
 * location in the world, and the entities that populate the world.
 */
public final class WorldModel
{
    public static final int PROPERTY_KEY = 0;

    public static final List<String> PATH_KEYS = new ArrayList<>(Arrays.asList("bridge", "dirt", "dirt_horiz", "dirt_vert_left", "dirt_vert_right",
            "dirt_bot_left_corner", "dirt_bot_right_up", "dirt_vert_left_bot"));

    public static final String SAPLING_KEY = "sapling";
    public static final int SAPLING_HEALTH_LIMIT = 5;
    public static final int SAPLING_ACTION_ANIMATION_PERIOD = 1000; // have to be in sync since grows and gains health at same time
    public static final int SAPLING_NUM_PROPERTIES = 4;
    public static final int SAPLING_ID = 1;
    public static final int SAPLING_COL = 2;
    public static final int SAPLING_ROW = 3;
    public static final int SAPLING_HEALTH = 4;

    public static final String BGND_KEY = "background";
    public static final int BGND_NUM_PROPERTIES = 4;
    public static final int BGND_ID = 1;
    public static final int BGND_COL = 2;
    public static final int BGND_ROW = 3;

    public static final String OBSTACLE_KEY = "obstacle";
    public static final int OBSTACLE_NUM_PROPERTIES = 5;
    public static final int OBSTACLE_ID = 1;
    public static final int OBSTACLE_COL = 2;
    public static final int OBSTACLE_ROW = 3;
    public static final int OBSTACLE_ANIMATION_PERIOD = 4;

    public static final String DUDE_KEY = "dude";
    public static final int DUDE_NUM_PROPERTIES = 7;
    public static final int DUDE_ID = 1;
    public static final int DUDE_COL = 2;
    public static final int DUDE_ROW = 3;
    public static final int DUDE_LIMIT = 4;
    public static final int DUDE_ACTION_PERIOD = 5;
    public static final int DUDE_ANIMATION_PERIOD = 6;
	public static final int DUDE_HEALTH = 10;
	
	public static final String DINODUDE_KEY = "dinodude";
    public static final int DINODUDE_NUM_PROPERTIES = 7;
    public static final int DINODUDE_ID = 1;
    public static final int DINODUDE_COL = 2;
    public static final int DINODUDE_ROW = 3;
    public static final int DINODUDE_ANIMATION_PERIOD = 4;
    public static final int DINODUDE_ACTION_PERIOD = 5;
    public static final int DINODUDE_HEALTH = 6;

	public static final String DINO_KEY = "dino";
    public static final int DINO_NUM_PROPERTIES = 7;
    public static final int DINO_ID = 1;
    public static final int DINO_COL = 2;
    public static final int DINO_ROW = 3;
    public static final int DINO_ANIMATION_PERIOD = 4;
    public static final int DINO_ACTION_PERIOD = 5;
    public static final int DINO_HEALTH = 6;
	
	public static final String DINOMOTHER_KEY = "dinomother";
    public static final int DINOMOTHER_NUM_PROPERTIES = 7;
    public static final int DINOMOTHER_ID = 1;
    public static final int DINOMOTHER_COL = 2;
    public static final int DINOMOTHER_ROW = 3;
    public static final int DINOMOTHER_ANIMATION_PERIOD = 4;
    public static final int DINOMOTHER_ACTION_PERIOD = 5;
    public static final int DINOMOTHER_HEALTH = 6;
	public static final int DINOMOTHER_STEPS = 6; // steps before laying egg

    public static final String DINOEGG_KEY = "dino_egg";
	public static final int DINOEGG_ACTION_PERIOD = 10;
	public static final int DINOEGG_HEALTH = 20;
	
    public static final String HOUSE_KEY = "house";
    public static final int HOUSE_NUM_PROPERTIES = 4;
    public static final int HOUSE_ID = 1;
    public static final int HOUSE_COL = 2;
    public static final int HOUSE_ROW = 3;

    public static final String FAIRY_KEY = "fairy";
    public static final int FAIRY_NUM_PROPERTIES = 6;
    public static final int FAIRY_ID = 1;
    public static final int FAIRY_COL = 2;
    public static final int FAIRY_ROW = 3;
    public static final int FAIRY_ANIMATION_PERIOD = 4;
    public static final int FAIRY_ACTION_PERIOD = 5;

    public static final String STUMP_KEY = "stump";

    public static final String TREE_KEY = "tree";
    public static final int TREE_NUM_PROPERTIES = 7;
    public static final int TREE_ID = 1;
    public static final int TREE_COL = 2;
    public static final int TREE_ROW = 3;
    public static final int TREE_ANIMATION_PERIOD = 4;
    public static final int TREE_ACTION_PERIOD = 5;
    public static final int TREE_HEALTH = 6;

    public static final int TREE_ANIMATION_MAX = 600;
    public static final int TREE_ANIMATION_MIN = 50;
    public static final int TREE_ACTION_MAX = 1400;
    public static final int TREE_ACTION_MIN = 1000;
    public static final int TREE_HEALTH_MAX = 3;
    public static final int TREE_HEALTH_MIN = 1;

    private int numRows;
    private int numCols;
    private Background background[][];
    private Entity occupancy[][];
    private Set<Entity> entities;
    private DinoInfection infection;
    private ImageStore imageStore;

    public WorldModel(ImageStore imageStore, int numRows, int numCols, Background defaultBackground) {
        this.imageStore = imageStore;
        this.numRows = numRows;
        this.numCols = numCols;
        this.background = new Background[numRows][numCols];
        this.occupancy = new Entity[numRows][numCols];
        this.entities = new HashSet<>();
        this.infection = new DinoInfection(this, imageStore);
        for (int row = 0; row < numRows; row++) {
            Arrays.fill(this.background[row], defaultBackground);
        }
    }


    public PImage getCurrentImage(Object entity) {
        if (entity instanceof Background) {
            return ((Background)entity).getImages().get(
                    ((Background)entity).getImageIndex());
        }
        else if (entity instanceof Entity) {
            return ((Entity)entity).getImages().get(((Entity)entity).getImageIndex());
        }
        else {
            throw new UnsupportedOperationException(
                    String.format("getCurrentImage not supported for %s",
                            entity));
        }
    }

    public void load(Scanner in, ImageStore imageStore)
    {
        int lineNumber = 0;
        while (in.hasNextLine()) {
            try {
                if (!processLine(in.nextLine(), imageStore)) {
                    System.err.println(String.format("invalid entry on line %d",
                            lineNumber));
                }
            }
            catch (NumberFormatException e) {
                System.err.println(
                        String.format("invalid entry on line %d", lineNumber));
            }
            catch (IllegalArgumentException e) {
                System.err.println(
                        String.format("issue on line %d: %s", lineNumber,
                                e.getMessage()));
            }
            lineNumber++;
        }
    }

    public boolean processLine(
            String line, ImageStore imageStore)
    {
        String[] properties = line.split("\\s");
        if (properties.length > 0) {
            switch (properties[PROPERTY_KEY]) {
                case BGND_KEY:
                    return parseBackground(properties, imageStore);
                case DUDE_KEY:
                    return parseDude(properties, imageStore);
                case OBSTACLE_KEY:
                    return parseObstacle(properties, imageStore);
                case FAIRY_KEY:
                    return parseFairy(properties, imageStore);
                case HOUSE_KEY:
                    return parseHouse(properties, imageStore);
                case TREE_KEY:
                    return parseTree(properties, imageStore);
                case SAPLING_KEY:
                    return parseSapling(properties, imageStore);
            }
        }

        return false;
    }

    public boolean parseBackground(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == BGND_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[BGND_COL]),
                    Integer.parseInt(properties[BGND_ROW]));
            String id = properties[BGND_ID];
            setBackground(pt, new Background(id, imageStore.getImageList(id)));
        }

        return properties.length == BGND_NUM_PROPERTIES;
    }

    public boolean parseSapling(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == SAPLING_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[SAPLING_COL]),
                    Integer.parseInt(properties[SAPLING_ROW]));
            String id = properties[SAPLING_ID];
            int health = Integer.parseInt(properties[SAPLING_HEALTH]);
            Entity entity = new Sapling(id, pt, imageStore.getImageList(SAPLING_KEY),
                    SAPLING_ACTION_ANIMATION_PERIOD, SAPLING_ACTION_ANIMATION_PERIOD, health, SAPLING_HEALTH_LIMIT);
            tryAddEntity(entity);
        }

        return properties.length == SAPLING_NUM_PROPERTIES;
    }

    public boolean parseDude(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == DUDE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[DUDE_COL]),
                    Integer.parseInt(properties[DUDE_ROW]));
            Entity entity = Factory.createDudeNotFull(properties[DUDE_ID],
                    pt,
                    Integer.parseInt(properties[DUDE_ACTION_PERIOD]),
                    Integer.parseInt(properties[DUDE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[DUDE_LIMIT]),
                    imageStore.getImageList(DUDE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == DUDE_NUM_PROPERTIES;
    }

    public boolean parseFairy(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == FAIRY_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[FAIRY_COL]),
                    Integer.parseInt(properties[FAIRY_ROW]));
            Entity entity = Factory.createFairy(properties[FAIRY_ID],
                    pt,
                    Integer.parseInt(properties[FAIRY_ACTION_PERIOD]),
                    Integer.parseInt(properties[FAIRY_ANIMATION_PERIOD]),
                    imageStore.getImageList(FAIRY_KEY));
            tryAddEntity(entity);
        }

        return properties.length == FAIRY_NUM_PROPERTIES;
    }

    public boolean parseTree(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == TREE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[TREE_COL]),
                    Integer.parseInt(properties[TREE_ROW]));
            Entity entity = Factory.createTree(properties[TREE_ID],
                    pt,
                    Integer.parseInt(properties[TREE_ACTION_PERIOD]),
                    Integer.parseInt(properties[TREE_ANIMATION_PERIOD]),
                    Integer.parseInt(properties[TREE_HEALTH]),
                    imageStore.getImageList(TREE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == TREE_NUM_PROPERTIES;
    }

    public boolean parseObstacle(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == OBSTACLE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[OBSTACLE_COL]),
                    Integer.parseInt(properties[OBSTACLE_ROW]));
            Entity entity = Factory.createObstacle(properties[OBSTACLE_ID], pt,
                    Integer.parseInt(properties[OBSTACLE_ANIMATION_PERIOD]),
                    imageStore.getImageList(OBSTACLE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == OBSTACLE_NUM_PROPERTIES;
    }

    public boolean parseHouse(
            String[] properties, ImageStore imageStore)
    {
        if (properties.length == HOUSE_NUM_PROPERTIES) {
            Point pt = new Point(Integer.parseInt(properties[HOUSE_COL]),
                    Integer.parseInt(properties[HOUSE_ROW]));
            Entity entity = Factory.createHouse(properties[HOUSE_ID], pt,
                    imageStore.getImageList(HOUSE_KEY));
            tryAddEntity(entity);
        }

        return properties.length == HOUSE_NUM_PROPERTIES;
    }

    public void tryAddEntity(Entity entity) {
        if (isOccupied(entity.getPosition())) {
            // arguably the wrong type of exception, but we are not
            // defining our own exceptions yet
            throw new IllegalArgumentException("position occupied");
        }

        addEntity(entity);
    }

    public boolean withinBounds(Point pos) {
        return pos.y >= 0 && pos.y < numRows && pos.x >= 0
                && pos.x < numCols;
    }

    public boolean isOccupied(Point pos) {
        return withinBounds(pos) && getOccupancyCell(pos) != null;
    }

    /*
       Assumes that there is no entity currently occupying the
       intended destination cell.
    */
    public void addEntity(Entity entity) {
        if (withinBounds(entity.getPosition())) {
            setOccupancyCell(entity.getPosition(), entity);
            entities.add(entity);
        }
    }

    public void moveEntity(Entity entity, Point pos) {
        Point oldPos = entity.getPosition();
        if (withinBounds(pos) && !pos.equals(oldPos)) {
            setOccupancyCell(oldPos, null);
            removeEntityAt(pos);
            setOccupancyCell(pos, entity);
            entity.setPosition(pos);

        }
    }

    public void removeEntity(Entity entity) {
        removeEntityAt(entity.getPosition());
    }

    public void removeEntityAt(Point pos) {
        if (withinBounds(pos) && getOccupancyCell(pos) != null) {
            Entity entity = getOccupancyCell(pos);

            /* This moves the entity just outside of the grid for
             * debugging purposes. */
            entity.setPosition(new Point(-1, -1));
            entities.remove(entity);
            setOccupancyCell(pos, null);
        }
    }

    public Optional<PImage> getBackgroundImage(Point pos)
    {
        if (withinBounds(pos)) {
            return Optional.of(getCurrentImage(getBackgroundCell(pos)));
        }
        else {
            return Optional.empty();
        }
    }

    public void setBackground(Point pos, Background background)
    {
        if (withinBounds(pos)) {
            setBackgroundCell(pos, background);
        }
    }



    public Optional<Entity> getOccupant(Point pos) {
        if (isOccupied(pos)) {
            return Optional.of(getOccupancyCell(pos));
        }
        else {
            return Optional.empty();
        }
    }

    public Entity getOccupancyCell(Point pos) {
        return occupancy[pos.y][pos.x];
    }

    public void setOccupancyCell(Point pos, Entity entity)
    {
        occupancy[pos.y][pos.x] = entity;
    }

    public Background getBackgroundCell(Point pos) {
        return background[pos.y][pos.x];
    }

    public void setBackgroundCell(Point pos, Background background)
    {
        this.background[pos.y][pos.x] = background;
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Set<Entity> getEntities() {
        return entities;
    }

    public DinoInfection getInfection(){
        return infection;
    }
}
