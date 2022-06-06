import processing.core.PImage;

import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Mover extends Healthy{

    public Mover(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                 int health, int healthLimit){
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
    }
    public boolean moveTo(Entity e, WorldModel world, EventScheduler scheduler)
    {
        if (Functions.adjacent(e.getPosition(), getPosition())) {
            return _moveToHelper(e, world, scheduler);
        }
        else {
            Point nextPos = nextPosition(world, e.getPosition());
            if (!getPosition().equals(nextPos)) {
                Optional<Entity> occupant = world.getOccupant(nextPos);
                if (occupant.isPresent()) {

                    scheduler.unscheduleAllEvents(occupant.get());
                }
                world.moveEntity(this, nextPos);
            }
            return false;
        }
    }

    public abstract boolean _moveToHelper(Entity e, WorldModel world, EventScheduler scheduler);

    public Point nextPosition(WorldModel world, Point destPos)
    {
        PathingStrategy pStrat = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = (p) -> !(world.isOccupied(p));
        BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> (Functions.adjacent(p1, p2));
        List<Point> path = pStrat.computePath(this.getPosition(), destPos, canPassThrough, withinReach,
                PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? this.getPosition() : path.get(0);
//        int horiz = Integer.signum(destPos.x - getPosition().x);
//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
//
//        if (horiz == 0 || world.isOccupied(newPos)) {
//            int vert = Integer.signum(destPos.y - getPosition().y);
//            newPos = new Point(getPosition().x, getPosition().y + vert);
//
//            if (vert == 0 || world.isOccupied(newPos)) {
//                newPos = getPosition();
//            }
//        }

    }
}
