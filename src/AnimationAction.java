public class AnimationAction extends Action{
    private Animated anim;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public AnimationAction(
            Animated anim,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.anim = anim;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }

    public void executeAction(EventScheduler scheduler)
    {
        anim.nextImage();

        if (repeatCount != 1) {
            scheduler.scheduleEvent(anim,
                    anim.createAnimationAction(Math.max(repeatCount - 1, 0)),
                    anim.getAnimationPeriod());
        }
    }

}
