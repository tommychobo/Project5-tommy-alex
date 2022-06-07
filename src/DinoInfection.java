
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class DinoInfection {
    private boolean[][] infected;
    private HashSet<Point> nextInfected;
    private WorldModel world;
    private ImageStore imageStore;

    public DinoInfection(WorldModel world, ImageStore imageStore){
        this.world = world;
        this.imageStore = imageStore;
        infected = new boolean[world.getNumRows()][world.getNumCols()];
        nextInfected = new HashSet<>();
    }

    public void motherDinoInfection(EventScheduler scheduler, Point pressed){
        DinoMother mom = new DinoMother("dino_mother_"+pressed.x+"_"+pressed.y, pressed,
                imageStore.getImageList("dino_mother", 32),
                20, 15, 8, 8, this);
        infect(pressed);
        world.addEntity(mom);
        mom.scheduleActions(scheduler, world, imageStore);
    }

    public void spread(){
        Object[] nextList = nextInfected.toArray();
        if(nextList.length > 0)
            infect((Point)(nextList[(int)(Math.random()*nextList.length)]));
    }

    public boolean isInfected(Point pos){
        if(world.withinBounds(pos)){
            return infected[pos.y][pos.x];
        }
        return false;
    }

    public void infect(Point pos){
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

}
