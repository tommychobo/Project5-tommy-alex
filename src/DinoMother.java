import processing.core.PImage;

import java.util.List;

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

        scheduler.scheduleEvent(this,
								createActivityAction(world, imageStore),
								getActionPeriod());
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }

    @Override
    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler) {
		numSteps++;

		if (numSteps > WorldModel.DINOMOTHER_STEPS) {
			DinoEgg egg = Factory.createDinoEgg
				("egg_" + getId(),
				 this.getPosition()+1,
				 WorldModel.DINOEGG_ACTION_PERIOD,
				 WorldModel.DINOEGG_HEALTH);
			numSteps = 0;  }
		
        return false;
    }
}
