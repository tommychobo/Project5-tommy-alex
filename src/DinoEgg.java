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
		super(id, position, images,0, 0, health, healthLimit); }

	@Override
	public void executeActivity(WorldModel world, ImageStore imageStore, EventScheduler scheduler) {

	}

	public boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore) {

		//Dino dino = new Dino();
		
		return true;
	}

}
