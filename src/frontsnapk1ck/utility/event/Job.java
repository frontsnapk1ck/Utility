package frontsnapk1ck.utility.event;

public abstract class Job {

    private String id;
	private boolean timeSet;
    private long timeExecuted;

    private boolean toExecute;

    /** Create a job */
	public Job()
	{
		this( null );
	}

    /** Create a job with given id */
	public Job( String id )
	{
        this.id = id;
        this.enable();
	}

    public abstract <R> Result<R>  execute();


    /** Returns the ID of this job */
    public String getID() 
    {
        return this.id;
    }
	
	/** Returns true if this job has an ID */
    public boolean hasID() 
    {
        return this.getID() != null; 
    }
	
	/** Returns true if this job's ID is the same as the given ID */
	public boolean idEquals( String id )
	{
		return	this.hasID() &&
                this.getID().equals( id );
	}
	
	public boolean setTimeExecuted() 
    {
        if (!timeSet)
        {
            this.timeExecuted = System.currentTimeMillis();
            timeSet = true;
            return true;
        }
        else
            return false;
        
    }
    
    public void enable()
    {
        this.toExecute = true;
    }

    public void disable()
    {
        this.toExecute = false;
    }

    public boolean toggle()
    {
        this.toExecute = this.toExecute ? false : true;
        return this.toExecute;
    }

    public long getTimeExecuted() 
    {
        return timeExecuted;
    }

    public boolean isTimeSet() {
        return timeSet;
    }

    public boolean toExecute() 
    {
        return toExecute;
    }

    @Override
    public String toString()
	{
		return	"Job[id = " + this.getID() + "]";
	}
    
}
