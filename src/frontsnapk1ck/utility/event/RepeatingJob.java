package frontsnapk1ck.utility.event;

public abstract class RepeatingJob extends Job {

    private long repTime;

    public RepeatingJob(long repTime)
    {
        this.repTime = repTime;
    }

    public long getRepTime() {
        return repTime;
    }
    
}
