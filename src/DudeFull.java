import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class DudeFull extends Dude
{

    public DudeFull(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                    int actionPeriod, int animationPeriod, int health, int healthLimit)
    {
        super(id, position, images, resourceLimit, resourceCount, actionPeriod, animationPeriod, health, healthLimit);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fullTarget =
			getPosition().findNearest(world, 3);
        if (fullTarget.isPresent() && moveTo(fullTarget.get(), world, scheduler))
			{
				this.transform(world, scheduler, imageStore);

			}
        else {
            scheduler.scheduleEvent(this,
									createActivityAction(world, imageStore),
									getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        DudeNotFull miner = Factory.createDudeNotFull(getId(), //Changed to dudenotfull ??
													  getPosition(), getActionPeriod(),
													  getAnimationPeriod(),
													  getResourceLimit(),
													  getImages());

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);

        return true;
    }

    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler){
		if (e instanceof Dino)
			((Healthy) e).setHealth(((Healthy) e).getHealth() - 1);   

        return true;
    }
}
