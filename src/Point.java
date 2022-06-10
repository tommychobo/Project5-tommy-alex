import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * A simple class representing a location in 2D space.
 */
public final class Point
{
    public final int x;
    public final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return "(" + x + "," + y + ")";
    }

    public boolean equals(Object other) {
        return other instanceof Point && ((Point)other).x == this.x
			&& ((Point)other).y == this.y;
    }

    public int hashCode() {
        int result = 17;
        result = result * 31 + x;
        result = result * 31 + y;
        return result;
    }
    public Optional<Entity> nearestEntity(List<Entity> entities)
    {
        if (entities.isEmpty()) {
            return Optional.empty();
        }
        else {
            Entity nearest = entities.get(0);
            int nearestDistance = distanceSquared(nearest.getPosition());

            for (Entity other : entities) {
                int otherDistance = distanceSquared(other.getPosition());

                if (otherDistance < nearestDistance) {
                    nearest = other;
                    nearestDistance = otherDistance;
                }
            }

            return Optional.of(nearest);
        }
    }

    public int distanceSquared(Point p1) {
        int deltaX = p1.x - this.x;
        int deltaY = p1.y - this.y;

        return deltaX * deltaX + deltaY * deltaY;
    }

    public Optional<Entity> findNearest(WorldModel world, int searchCode)
    {
        /*
		  coding is hard.
		  1 = sapling and tree
		  2 = stump
		  3 = house
		  4 = dude
		  5 = dino
		*/
        List<Entity> ofType = new LinkedList<>();
        for (Entity entity : world.getEntities()) {
            switch(searchCode) {
			case 1:
				if (/*entity instanceof Sapling || */entity instanceof Tree) {
					ofType.add(entity);
				}
				break;
			case 2:
				if (entity instanceof Stump) {
					ofType.add(entity);
				}
				break;
			case 3:
				if (entity instanceof House) {
					ofType.add(entity);
				}
				break;
			case 4:
				if (entity instanceof Dude)
					ofType.add(entity);
				break;
			case 5:
				if (entity instanceof Dino || entity instanceof DinoMother)
					ofType.add(entity);
                break;
            }
        }
        return nearestEntity(ofType);
    }
}
