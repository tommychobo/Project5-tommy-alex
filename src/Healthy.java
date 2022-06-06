import processing.core.PImage;

import java.util.List;

public abstract class Healthy extends Actor{
    private int health;
    private int healthLimit;

    public Healthy(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod,
                   int health, int healthLimit){
        super(id, position, images, actionPeriod, animationPeriod);
        this.health = health;
        this.healthLimit = healthLimit;
    }

    public abstract boolean transform(WorldModel world, EventScheduler scheduler, ImageStore imageStore);

    public int getHealth() {
        return health;
    }

    public void setHealth(int h) {
        health = h;
    }

    public int getHealthLimit(){
        return healthLimit;
    }
}
