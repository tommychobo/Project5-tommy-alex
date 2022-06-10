import processing.core.PImage;

import java.util.List;
import java.util.Optional;

public class DinoMother extends Mover{
    private DinoInfection infection;
	private int numSteps;

    public DinoMother(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                      int health, int healthLimit, DinoInfection infection) {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
        this.infection = infection;
		this.numSteps = 0;
    }

    @Override
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
        infection.spread();
        moveTo(null, world, scheduler); //Random walk!
        scheduler.scheduleEvent(this,
								createActivityAction(world, imageStore),
								getActionPeriod());

        if (getHealth() <= 0) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);}
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

    @Override
    public boolean moveTo(Entity e, WorldModel world, EventScheduler scheduler) {
		numSteps++;
        Object[] positions = PathingStrategy.CARDINAL_NEIGHBORS.apply(this.getPosition())
                .filter(world::withinBounds).filter(p -> !world.isOccupied(p)).filter(p -> infection.isInfected(p)).toArray();
        if(positions.length > 0) {
            world.moveEntity(this, (Point) positions[(int) (Math.random() * positions.length)]);
        }
		if (numSteps > WorldModel.DINOMOTHER_STEPS) {
            Object[] pts = PathingStrategy.CARDINAL_NEIGHBORS.apply(this.getPosition())
                    .filter(world::withinBounds).filter(p -> !world.isOccupied(p)).toArray();
            if (pts.length > 0) {
                DinoEgg egg = Factory.createDinoEgg
                        ("egg_" + getId(), (Point) pts[0], infection.getImageStore().getImageList(WorldModel.DINOEGG_KEY, 32),
                                WorldModel.DINOEGG_ACTION_PERIOD,
                                WorldModel.DINOEGG_HEALTH);
                world.addEntity(egg);
                egg.scheduleActions(scheduler, world, infection.getImageStore());
                numSteps = 0;
            }
        }
        return false;
    }

    @Override
    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler){
        return false;
    }
}
