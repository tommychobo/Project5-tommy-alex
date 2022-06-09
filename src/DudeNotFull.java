import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeNotFull extends Dude
{
    public DudeNotFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
					   int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }


    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> target =
			getPosition().findNearest(world, 1);

        if (target.isPresent() && moveTo(target.get(), world, scheduler))			
			this.transform(world, scheduler, imageStore);
        else 
            scheduler.scheduleEvent(this,
									createActivityAction(world, imageStore),
									getActionPeriod());
	}

    //     if (!target.isPresent() || !moveTo(target.get(), world, scheduler)
	// 		|| !transform(world, scheduler, imageStore))
	// 		{
	// 			scheduler.scheduleEvent(this,
	// 									createActivityAction(world, imageStore),
	// 									getActionPeriod());
	// 		}


    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)   {

		if (world.getInfection().isInfected(getPosition())) {
			DinoDude dinodude = Factory.createDinoDude
				("dinodude_" + getId(),
				 getPosition(),
				 imageStore.getImageList(WorldModel.DINODUDE_KEY, 32),
				 WorldModel.DINODUDE_ACTION_PERIOD,
				 WorldModel.DINODUDE_ANIMATION_PERIOD,
				 WorldModel.DINODUDE_HEALTH);

			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);

			world.addEntity(dinodude);
			dinodude.scheduleActions(scheduler, world, imageStore);

			return true; }
			
		if (getResourceCount() >= getResourceLimit()) {
			DudeFull miner = Factory.createDudeFull
				(getId(),
				 getPosition(), getActionPeriod(),
				 getAnimationPeriod(),
				 getResourceLimit(),
				 getImages());

			world.removeEntity(this);
			scheduler.unscheduleAllEvents(this);

			world.addEntity(miner);
			miner.scheduleActions(scheduler, world, imageStore);  

			return true; }

		return false;
    }
	
    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler){
        if(e instanceof Healthy) { 
			if (!(e instanceof Dino))  // only attack dino
				setResourceCount(getResourceCount()+1);
            ((Healthy) e).setHealth(((Healthy) e).getHealth() - 1);   }
		
        return true;
    }


}
