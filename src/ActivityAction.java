public class ActivityAction extends Action{
    private Actor actor;
    private WorldModel world;
    private ImageStore imageStore;
    private int repeatCount;

    public ActivityAction(
            Actor actor,
            WorldModel world,
            ImageStore imageStore,
            int repeatCount)
    {
        this.actor = actor;
        this.world = world;
        this.imageStore = imageStore;
        this.repeatCount = repeatCount;
    }
    public void executeAction(EventScheduler scheduler) {
        actor.executeActivity(world, imageStore, scheduler);
    }
}
