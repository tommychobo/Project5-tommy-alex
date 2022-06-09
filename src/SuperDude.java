import processing.core.PImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class SuperDude extends Mover {

	public SuperDude
		(String id,
		 Point position,
		 List<PImage> images,
		 int actionPeriod,
		 int animationPeriod,
		 int health,
		 int healthLimit) {
		super(id, position, images, actionPeriod, animationPeriod, health, healthLimit); }
		
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)   {

        Optional<Entity> target = getPosition().findNearest(world, 5);  // attack dinos

		if (!target.isPresent()	||
			!moveTo(target.get(), world, scheduler)	||
			!transform(world, scheduler, imageStore)) 

			scheduler.scheduleEvent(this,
									createActivityAction(world, imageStore),
									getActionPeriod());
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		if (getHealth() <= 0) {
			world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);}

		return false;
	}

    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler){
        if (e instanceof Dino || e instanceof DinoMother)  
            ((Healthy) e).setHealth(((Healthy) e).getHealth() - 3);

        return true;
    }
}
