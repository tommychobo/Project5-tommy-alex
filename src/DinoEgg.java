import processing.core.PImage;
import java.util.*;
import java.util.function.*;

public class DinoEgg extends Healthy {

	public DinoEgg
		(String id,
		 Point position,
		 List<PImage> images,
		 int health,
		 int healthLimit) {
		super(id, position, images, health, healthLimit); }

	public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {
		
	}

}
