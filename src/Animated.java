import processing.core.PImage;

import java.util.List;

public abstract class Animated extends Entity{
    private int actionPeriod;
    private int animationPeriod;

    public Animated(String id, Point position, List<PImage> images, int actionPeriod, int animationPeriod){
        super(id, position, images);
        this.actionPeriod = actionPeriod;
        this.animationPeriod = animationPeriod;
    }
    public int getAnimationPeriod() {
        return animationPeriod;
    }
    public int getActionPeriod(){
        return actionPeriod;
    }
    public Action createAnimationAction(int repeatCount) {
        return new AnimationAction(this, null, null,
                repeatCount);
    }
    public void scheduleActions(EventScheduler scheduler, WorldModel world, ImageStore imageStore)
    {
        scheduler.scheduleEvent(this,
                createAnimationAction(0),
                getAnimationPeriod());

    }
}