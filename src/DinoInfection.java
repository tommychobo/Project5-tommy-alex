
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DinoInfection {
    private boolean[][] infected;
    private HashSet<Point> nextInfected;
    private WorldModel world;
    private ImageStore imageStore;
    private final int INITIAL_INFECTION = 15;
    private final double MAX_WORLD_INFECTION = 0.3;
    private static int infectionTotal;

    public DinoInfection(WorldModel world, ImageStore imageStore){
        this.world = world;
        this.imageStore = imageStore;
        infected = new boolean[world.getNumRows()][world.getNumCols()];
        nextInfected = new HashSet<>();
        infectionTotal = 0;
    }

    public void motherDinoInfection(EventScheduler scheduler, Point pressed){
        DinoMother mom = new DinoMother("dino_mother_"+pressed.x+"_"+pressed.y, pressed,
                imageStore.getImageList(WorldModel.DINOMOTHER_KEY, 32),
                WorldModel.DINOMOTHER_ACTION_PERIOD, WorldModel.DINOMOTHER_ANIMATION_PERIOD,
                8, 8, this);
        infect(pressed);
        for(int i = 1; i < INITIAL_INFECTION; i++){
            spread();
        }
        world.addEntity(mom);
        mom.scheduleActions(scheduler, world, imageStore);
    }

    public void spread(){
        if(infectionTotal*1.0/(world.getNumRows()*world.getNumCols()) < MAX_WORLD_INFECTION) {
            Object[] nextList = nextInfected.toArray();
            if (nextList.length > 0)
                infect((Point) (nextList[(int) (Math.random() * nextList.length)]));
        }
    }

    public boolean isInfected(Point pos){
        if(world.withinBounds(pos)){
            return infected[pos.y][pos.x];
        }
        return false;
    }

    public void infect(Point pos){
        infectionTotal++;
        world.setBackground(pos, new Background("background_dino", imageStore.getImageList("dino_dirt")));
        infected[pos.y][pos.x] = true;
        nextInfected.remove(pos);
        List<Point> neighbors = Arrays.asList(
                new Point(pos.x-1, pos.y),
                new Point(pos.x+1, pos.y),
                new Point(pos.x, pos.y-1),
                new Point(pos.x, pos.y+1));
        for(Point n : neighbors){
            if(world.withinBounds(n) && !isInfected(n)){
                nextInfected.add(n);
            }
        }
    }

    public void uninfect(Point pos){
        infectionTotal--;
        world.setBackground(pos, new Background("default_background", imageStore.getImageList("grass")));
        infected[pos.y][pos.x] = false;
        List<Point> neighbors = Arrays.asList(
                new Point(pos.x-1, pos.y),
                new Point(pos.x+1, pos.y),
                new Point(pos.x, pos.y-1),
                new Point(pos.x, pos.y+1));
        for(Point n : neighbors){
            if(world.withinBounds(n) && isInfected(n)){
                nextInfected.add(pos);
                break;
            }
        }
    }

    public ImageStore getImageStore(){
        return imageStore;
    }

}
