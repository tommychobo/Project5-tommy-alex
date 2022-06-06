import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
public final class Fairy extends Mover
{

    public Fairy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod)
    {
        super(id, position, images, actionPeriod, animationPeriod, 0, 0);
    }

    public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<Entity> fairyTarget =
                getPosition().findNearest(world, 2);

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().getPosition();

            if (moveTo(fairyTarget.get(), world, scheduler)) {
                Sapling sapling = Factory.createSapling("sapling_" + getId(), tgtPos,
                        imageStore.getImageList(WorldModel.SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                getActionPeriod());
    }


    @Override
    public boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler) {
        world.removeEntity(e);
        scheduler.unscheduleAllEvents(e);
        return true;
    }

    @Override
    public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
        return false;
    }
}
