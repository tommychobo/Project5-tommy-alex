import processing.core.PImage;

import java.util.List;

public class DinoMother extends Mover{

    public DinoMother(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod, int health, int healthLimit) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

    @Override
    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler) {
        return false;
    }
}
