/*import processing.core.PImage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * An entity that exists in the world. See EntityKind for the
 * different kinds of entities that exist.
 */
/*public final class EntityOld
{
    private EntityKind kind;
    private String id;
    private Point position;
    private List<PImage> images;
    private int imageIndex;
    private int resourceLimit;
    private int resourceCount;
    private int actionPeriod;
    private int animationPeriod;
    private int health;
    private int healthLimit;

    public EntityOld(
            EntityKind kind,
            String id,
            Point position,
            List<PImage> images,
            int resourceLimit,
            int resourceCount,
            int actionPeriod,
            int animationPeriod,
            int health,
            int healthLimit)
    {
        this.kind = kind;
        this.id = id;
        this.position = position;
        this.images = images;
        this.imageIndex = 0;
        this.resourceLimit = resourceLimit;
        this.resourceCount = resourceCount;
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public int getAnimationPeriod() {
        switch (kind) {
            case DUDE_FULL:
            case DUDE_NOT_FULL:
            case OBSTACLE:
            case FAIRY:
            case SAPLING:
            case TREE:
                return animationPeriod;
            default:
                throw new UnsupportedOperationException(
                        String.format("getAnimationPeriod not supported for %s",
                                kind));
        }
    }

    public void nextImage() {
        imageIndex = (imageIndex + 1) % images.size();
    }

    public void executeSaplingActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        health++;
        if (!transformPlant(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    public void executeTreeActivity(WorldModel world, ImageStore imageStore,  EventScheduler scheduler)
    {

        if (!transformPlant(world, scheduler, imageStore)) {

            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    public void executeFairyActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityOld> fairyTarget =
                position.findNearest(world, new ArrayList<>(Arrays.asList(EntityKind.STUMP)));

        if (fairyTarget.isPresent()) {
            Point tgtPos = fairyTarget.get().position;

            if (fairyTarget.get().moveToFairy(this, world, scheduler)) {
                EntityOld sapling = Factory.createSapling("sapling_" + id, tgtPos,
                        imageStore.getImageList(WorldModel.SAPLING_KEY));

                world.addEntity(sapling);
                sapling.scheduleActions(scheduler, world, imageStore);
            }
        }

        scheduler.scheduleEvent(this,
                createActivityAction(world, imageStore),
                actionPeriod);
    }

    public void executeDudeNotFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityOld> target =
                position.findNearest(world, new ArrayList<>(Arrays.asList(EntityKind.TREE, EntityKind.SAPLING)));

        if (!target.isPresent() || !target.get().moveToNotFull(this, world, scheduler)
                || !transformNotFull(world, scheduler, imageStore))
        {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    public void executeDudeFullActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler)
    {
        Optional<EntityOld> fullTarget =
                position.findNearest(world, new ArrayList<>(Arrays.asList(EntityKind.HOUSE)));

        if (fullTarget.isPresent() && fullTarget.get().moveToFull(this, world, scheduler))
        {
            this.transformFull(world, scheduler, imageStore);
        }
        else {
            scheduler.scheduleEvent(this,
                    createActivityAction(world, imageStore),
                    actionPeriod);
        }
    }

    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        switch (kind) {
            case DUDE_FULL:
                scheduler.scheduleEvent(this,
                        createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case DUDE_NOT_FULL:
                scheduler.scheduleEvent(this,
                        createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case OBSTACLE:
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case FAIRY:
                scheduler.scheduleEvent(this,
                        createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case SAPLING:
                scheduler.scheduleEvent(this,
                        createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            case TREE:
                scheduler.scheduleEvent(this,
                        createActivityAction(world, imageStore),
                        actionPeriod);
                scheduler.scheduleEvent(this,
                        createAnimationAction(0),
                        getAnimationPeriod());
                break;

            default:
        }
    }

    public Action createAnimationAction(int repeatCount) {
        return new AnimationAction(this, null, null,
                repeatCount);
    }

    public Action createActivityAction(WorldModel world, ImageStore imageStore)
    {
        return new ActivityAction(this, world, imageStore, 0);
    }


    public boolean transformNotFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (resourceCount >= resourceLimit) {
            EntityOld miner = Factory.createDudeFull(id,
                    position, actionPeriod,
                    animationPeriod,
                    resourceLimit,
                    images);

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(miner);
            miner.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public void transformFull(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        EntityOld miner = Factory.createDudeNotFull(id,
                position, actionPeriod,
                animationPeriod,
                resourceLimit,
                images);

        world.removeEntity(this);
        scheduler.unscheduleAllEvents(this);

        world.addEntity(miner);
        miner.scheduleActions(scheduler, world, imageStore);
    }


    public boolean transformPlant(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (kind == EntityKind.TREE)
        {
            return transformTree(world, scheduler, imageStore);
        }
        else if (kind == EntityKind.SAPLING)
        {
            return transformSapling(world, scheduler, imageStore);
        }
        else
        {
            throw new UnsupportedOperationException(
                    String.format("transformPlant not supported for %s", this));
        }
    }

    public boolean transformTree(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (health <= 0) {
            EntityOld stump = Factory.createStump(id,
                    position,
                    imageStore.getImageList(WorldModel.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }

        return false;
    }

    public boolean transformSapling(WorldModel world, EventScheduler scheduler, ImageStore imageStore)
    {
        if (health <= 0) {
            EntityOld stump = Factory.createStump(id,
                    position,
                    imageStore.getImageList(WorldModel.STUMP_KEY));

            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);

            world.addEntity(stump);
            stump.scheduleActions(scheduler, world, imageStore);

            return true;
        }
        else if (health >= healthLimit)
        {
            EntityOld tree = Factory.createTree("tree_" + id,
                    position,
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

    public boolean moveToFairy(EntityOld fairy, WorldModel world, EventScheduler scheduler)
    {
        if (Functions.adjacent(fairy.position, this.position)) {
            world.removeEntity(this);
            scheduler.unscheduleAllEvents(this);
            return true;
        }
        else {
            Point nextPos = fairy.nextPositionFairy(world, this.position);

            if (!fairy.position.equals(nextPos)) {
                Optional<EntityOld> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(fairy, nextPos);
            }
            return false;
        }
    }

    public boolean moveToNotFull(EntityOld dude, WorldModel world, EventScheduler scheduler)
    {
        if (Functions.adjacent(dude.position, this.position)) {
            dude.resourceCount += 1;
            this.health--;
            return true;
        }
        else {
            Point nextPos = dude.nextPositionDude(world, this.position);

            if (!dude.position.equals(nextPos)) {
                Optional<EntityOld> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(dude, nextPos);
            }
            return false;
        }
    }

    //Object that this acts on is the target
    public boolean moveToFull(EntityOld dude, WorldModel world, EventScheduler scheduler)
    {
        if (Functions.adjacent(dude.position, this.position)) {
            return true;
        }
        else {
            Point nextPos = dude.nextPositionDude(world, this.position);

            if (!dude.position.equals(nextPos)) {
                Optional<EntityOld> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {
                    scheduler.unscheduleAllEvents(occupant.get());
                }

                world.moveEntity(dude, nextPos);
            }
            return false;
        }
    }

    public Point nextPositionFairy(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz, position.y);

        if (horiz == 0 || world.isOccupied(newPos)) {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);

            if (vert == 0 || world.isOccupied(newPos)) {
                newPos = position;
            }
        }

        return newPos;
    }

    public Point nextPositionDude(WorldModel world, Point destPos)
    {
        int horiz = Integer.signum(destPos.x - position.x);
        Point newPos = new Point(position.x + horiz, position.y);

        if (horiz == 0 || world.isOccupied(newPos) && world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
            int vert = Integer.signum(destPos.y - position.y);
            newPos = new Point(position.x, position.y + vert);

            if (vert == 0 || world.isOccupied(newPos) &&  world.getOccupancyCell(newPos).kind != EntityKind.STUMP) {
                newPos = position;
            }
        }

        return newPos;
    }



    public EntityKind getKind() {
        return kind;
    }

    public String getId() {
        return id;
    }

    public Point getPosition() {
        return position;
    }

    public void setPosition(Point p){
        position = p;
    }

    public List<PImage> getImages() {
        return images;
    }

    public int getImageIndex() {
        return imageIndex;
    }

    public int getHealth() {
        return health;
    }
}*/
