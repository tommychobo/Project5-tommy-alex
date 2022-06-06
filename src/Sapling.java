import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Sapling extends Healthy
{

    public Sapling(String id, Point position, List<PImage> images, int actionPeriod,
                   int animationPeriod, int health, int healthLimit)
    {
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }
    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        setHealth(getHealth()+1);
        if (!transform(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    getActionPeriod());
        }
    }

    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (getHealth() <= 0) {
            Stump stump = Factory.createStump(getId(),
                    getPosition(),
                    imageStore.getImageList(WorldModel.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            //stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (getHealth() >= getHealthLimit())
        {
            Tree tree = Factory.createTree("tree_" + getId(),
                    getPosition(),
                    Functions.getNumFromRange(WorldModel.TREE_ACTION_MAX, WorldModel.TREE_ACTION_MIN),
                    Functions.getNumFromRange(WorldModel.TREE_ANIMATION_MAX, WorldModel.TREE_ANIMATION_MIN),
                    Functions.getNumFromRange(WorldModel.TREE_HEALTH_MAX, WorldModel.TREE_HEALTH_MIN),
                    imageStore.getImageList(WorldModel.TREE_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(tree);
            tree.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        return false;
    }
}
