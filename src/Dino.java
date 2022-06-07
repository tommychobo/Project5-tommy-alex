import processing.core.PImage;
import java.util.*;
import java.util.function.*;

public class Dino extends Mover {

	public Dino
		(String id,
		 Point position,
		 List<PImage> images,
		 int actionPeriod,
		 int animationPeriod,
		 int health,
		 int healthLimit) {
		super(id, position, images, actionPeriod, animationPeriod, health, healthLimit); }

    @Override
    public Point nextPosition(WorldModel world, Point destPos)    {
		
        PathingStrategy pStrat = new AStarPathingStrategy();

		Predicate<Point> canPassThrough = (p) ->
			!(world.isOccupied(p)) ||
			(world.getOccupancyCell(p) instanceof Stump) ||
			(world.getBackgroundCell(p).getImageIndex() > 9 &&
			 world.getBackgroundCell(p).getImageIndex() < 17);

        BiPredicate<Point, Point> withinReach = (Point p1, Point p2) -> (Functions.adjacent(p1, p2));

        List<Point> path = pStrat.computePath
			(this.getPosition(),
			 destPos,
			 canPassThrough,
			 withinReach,
			 PathingStrategy.CARDINAL_NEIGHBORS);
		
        return path.isEmpty() ? this.getPosition() : path.get(0); }

	public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		
	}

	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {
		
	}
}