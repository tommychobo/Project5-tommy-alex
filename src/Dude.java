import processing.core.PImage;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class Dude extends Mover{
    private int resourceLimit;
    private int resourceCount;

    public Dude(String id, Point position, List<PImage> images, int resourceLimit, int resourceCount,
                int actionPeriod, int animationPeriod, int health, int healthLimit){
        super(id, position, images, actionPeriod, animationPeriod, health, healthLimit);
        this.resourceCount = resourceCount;
        this.resourceLimit = resourceLimit;
    }

    @Override
    public Point nextPosition(WorldModel world, Point destPos)
    {
        PathingStrategy pStrat = new AStarPathingStrategy();
        Predicate<Point> canPassThrough = (p) -> !(world.isOccupied(p)) || (world.getOccupancyCell(p) instanceof Stump);
        BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> (Functions.adjacent(p1, p2));
        List<Point> path = pStrat.computePath(this.getPosition(), destPos, canPassThrough, withinReach,
											  PathingStrategy.CARDINAL_NEIGHBORS);
        return path.isEmpty() ? this.getPosition() : path.get(0);
		//        int horiz = Integer.signum(destPos.x - getPosition().x);
		//        Point newPos = new Point(getPosition().x + horiz, getPosition().y);
		//        //System.out.println((world.getOccupancyCell(newPos) != null) ? world.getOccupancyCell(newPos).getClass() : "null");
		//        if (horiz == 0 || world.isOccupied(newPos) && !(world.getOccupancyCell(newPos) instanceof Stump)) {
		//            int vert = Integer.signum(destPos.y - getPosition().y);
		//            newPos = new Point(getPosition().x, getPosition().y + vert);
		//
		//            if (vert == 0 || world.isOccupied(newPos) &&  !(world.getOccupancyCell(newPos) instanceof Stump)) {
		//                newPos = getPosition();
		//            }
		//        }
		//
		//        return newPos;
    }

    public int getResourceLimit(){
        return resourceLimit;
    }
    public int getResourceCount(){
        return resourceCount;
    }
    public void setResourceCount(int r){
        resourceCount = r;
    }
}
