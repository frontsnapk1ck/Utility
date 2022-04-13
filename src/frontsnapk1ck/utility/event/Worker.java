package frontsnapk1ck.utility.event;

import java.math.BigInteger;

import frontsnapk1ck.utility.logger.Logger;

public class Worker {

    public static final int DEFAULT_MAX_ATTEMPTS = 10;

    private Logger logger = new Logger();

    private State state;
    private Thread workerThread;
    protected Job job;
    private BigInteger count;
    private String id;
    private String name;
    private int maxAttempts;

    private int fails = 0;

    public Worker(String id ) 
    {
        this( id , DEFAULT_MAX_ATTEMPTS);
    }

    public Worker(String id, int maxAttempts)
    {
        super();
        this.id = id;
        this.maxAttempts = maxAttempts;
        setState( State.WAITING );
        this.count = BigInteger.ZERO;
        config();
    }

    public void setLogger(Logger logger) 
    {
        this.logger = logger;
    }

    private void config() 
    {
        Runnable r = new Runnable(){
            @Override
            public void run() 
            {
                eventLoop();
            }
        };
        this.name = "Worker [" + id + "]";
        this.workerThread = new Thread( r , this.name );
        this.workerThread.start();
    }

    protected void eventLoop() 
    {
        while (getState() != State.STOP)
        {
            try
            {
                iteration();
            }
            catch(Exception e)
            {
                this.fails ++;
                logger.warn("Worker", "Worker " + id + " ran into a " + e.getClass().getSimpleName() + " while operating on a job with the error " + e.getMessage());
                if ( this.fails >= maxAttempts )
                {
                    logger.warn("Worker", getName() + " has stopped because `execute()` failed too many times");
                    setState(State.STOP);
                }
            }
        }
    }

    private void iteration() 
    {
        if ( this.job == null)
            cooldown(EventManager.COOLDOWN_INTERVAL);
        else
        {
            setState( State.WORKING );
            this.job.execute();
            this.count = this.count.add(BigInteger.ONE);
            setState( State.FINISHED );
            this.job = null;
        }
    }

    private void cooldown(Long time) 
    {
        try 
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e) {
        }
    }

    public void execute(Job j)
    {
        if (getState() == State.WORKING)
            throw new RuntimeException("Worker not finished");

        this.job = j;
    }

    public boolean isFinished() 
    {
        return state == State.FINISHED;
    }

    public boolean isWaiting()
    {
        return state == State.WAITING;
    }

    public boolean isWorking() 
    {
		return getState() == State.WORKING;
	}

	public Job getJob() 
    {
		return job;
	}

    public State getState() 
    {
        return state;
    }

    public void setState(State state) 
    {
        this.state = state;
        if ( this.workerThread == null )
            return;
        this.workerThread.setName(this.name + " [" + state + "]");
    }

	public String getName() 
    {
		return this.workerThread.getName();
	}
    public BigInteger getCompleted() 
    {
        return this.count;
    }

    protected static class State {

        public static final State WAITING   = new State("WAITING");
        public static final State FINISHED  = new State("FINISHED");
        public static final State WORKING   = new State("WORKING");
        public static final State STOP      = new State("STOP");

        
        private String id;
        
        public State(String id) 
        {
            this.id = id;
        }

        @Override
        public boolean equals(Object obj)
        {
            if (obj instanceof SimpleResult.State)
                return ((SimpleResult.State) obj).getId().equals(this.getId());
        
            return false;
        }

        @Override
        public String toString()
        {
            return getId();
        }

        public String getId()
        {
            return id;
        }

        public State parseState ( String state )
        {
            if (state.toUpperCase().equals("WAITING"))
                return WAITING;
            if (state.toUpperCase().equals("FINISHED"))
                return FINISHED;
            if (state.toUpperCase().equals("WORKING"))
                return WORKING;
            if (state.toUpperCase().equals("STOP"))
                return STOP;
            return null;
        }
    }

}
