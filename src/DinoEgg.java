import processing.core.PImage;
import java.util.*;
import java.util.function.*;

public class DinoEgg extends Healthy {

	public DinoEgg
		(String id,
		 Point position,
		 List<PImage> images,
		 int actionPeriod,
		 int animationPeriod,
		 int health,
		 int healthLimit) {
		super(id, position, images, actionPeriod, animationPeriod, health, healthLimit); }

	@Override
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		setHealth(getHealth()+1);

		if (!transform(world, scheduler, imageStore))
            scheduler.scheduleEvent
				(this,
				 createActivityAction(world, imageStore),
				 getActionPeriod());
	}

	public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {

		if (getHealth() <= 0) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            return true;  }
		
        else if (getHealth() >= getHealthLimit()) {
			Dino dino = Factory.createDino
				("dino_" + getId(),
				 getPosition(),
				 imageStore.getImageList(WorldModel.DINO_KEY),
				 Functions.getNumFromRange(WorldModel.DINO_ACTION_MAX, WorldModel.DINO_ACTION_MIN),
				 Functions.getNumFromRange(WorldModel.DINO_ANIMATION_MAX, WorldModel.DINO_ANIMATION_MIN),
				 Functions.getNumFromRange(WorldModel.DINO_HEALTH_MAX, WorldModel.DINO_HEALTH_MIN));

			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);

			world.addEntity(dino);
			dino.scheduleActions(scheduler, world, imageStore);

			return true; }
		
        return false;
	}
}
