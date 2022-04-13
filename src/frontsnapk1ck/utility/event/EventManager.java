package frontsnapk1ck.utility.event;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import frontsnapk1ck.utility.logger.Logger;

public class EventManager {

    public static final Long COOLDOWN_INTERVAL = 50l;
    public static final int DEFAULT_WORKERS = 6;

    private Logger logger;

    private PriorityBlockingQueue<ScheduledJob> jobQueue;
    private List<Worker> workers;

    private boolean running;
    private Thread eventThread;
    private int numWorkers;

    public EventManager() 
    {
        this(DEFAULT_WORKERS);
    }

    public EventManager(int numWorkers) 
    {
        this.numWorkers = numWorkers;
        this.jobQueue = new PriorityBlockingQueue<ScheduledJob>();
        this.workers = new ArrayList<Worker>();
        init();
    }



    private void init() 
    {
        this.logger = new Logger();
        this.makeWorkers();
        this.configThread();
    }

    private void makeWorkers() 
    {
        for (int i = 0; i < numWorkers; i++) 
        {
            
            Worker worker = new Worker("" + i);
            worker.setLogger(new Logger());
            this.workers.add(worker);
        }
    }

    private void configThread() 
    {
        this.eventThread = new Thread(new Runnable() {
            @Override
            public void run() {
                eventLoop();
            }
        });

        this.eventThread.setName("EventManager Thread");
        this.eventThread.setDaemon(true);
        this.eventThread.start();
    }

    public void queueIn(Job action, long offset) 
    {
        long time = System.currentTimeMillis();
        time += offset;
        this.queueAt(time, action);
    }

    public void queue(Job action) 
    {
        long time = System.currentTimeMillis();
        this.queueAt(time, action);
    }

    public void queueAt(long time, Job action) 
    {
        ScheduledJob scheduled = new ScheduledJob(time, action);
        this.jobQueue.add(scheduled);
    }

    public boolean unQueue(Job job) 
    {
        ScheduledJob toRm = null;
        for (ScheduledJob sJob : jobQueue) 
        {
            if (sJob.job == job)
                toRm = sJob; 
        }
        if (toRm != null)
        {
            synchronized ( this.jobQueue ) 
            {
                return this.jobQueue.remove(toRm);
            }
        }
        return false;
	}

    protected void eventLoop() 
    {
        this.running = true;
        while (this.running) {
            // Look at next job
            ScheduledJob next = this.jobQueue.peek();

            // Check time
            Long time = System.currentTimeMillis();

            if ( next != null && next.time <= time )
                this.executeNextJob();

            // Otherwise, pause if no new jobs...
            // - if you still have jobs and its time
            // for them to execute we dont' want to
            // pause, we want to run all of them.
            else
                sleep();
        }
    }

    protected void sleep() {
        try {
            Thread.sleep(COOLDOWN_INTERVAL);
        } catch (InterruptedException e) {
        }
    }

    private void executeNextJob() 
    {
        try 
        {
            // Remove the next job from queue
            // - same one you just "peeked" at.
            ScheduledJob job = this.jobQueue.take();

            if (job.job.toExecute())
            {
                // get next worker
                Worker worker = getWorker();

                if (worker == null)
                {
                    logger.warn("EventManger", "The Event manager is out of workers");
                    //come back to this job later
                    return;
                }

                // execute the job
                worker.execute(job.job);

            }
            if (job.job instanceof RepeatingJob)
                this.queueIn(job.job,((RepeatingJob)job.job).getRepTime());
        }
        catch (InterruptedException ex)
        {
            logger.debug("EventManager", ex );
        }
        catch (RuntimeException ex)
        {
            logger.warn("EventManager", ex.getMessage());
        }
    }

    private Worker getWorker() 
    {
        for (Worker w : workers) 
        {
            if (w.isFinished() || w.isWaiting())
                return w;
        }
        return null;
    }

    public boolean isRunning() 
    {
        return running;
    }

    public void stop()
    {
        this.running = false;
    }

    public PriorityBlockingQueue<ScheduledJob> getJobQueue() 
    {
        return jobQueue;
    }

    public List<Worker> getWorkers() 
    {
		return this.workers;
	}

    public void setJobQueue(PriorityBlockingQueue<ScheduledJob> jobQueue) 
    {
        this.jobQueue = jobQueue;
    }

    public static ScheduledJob newScheduledJob(Long time, Job job) 
    {
        return new ScheduledJob(time , job);
    }

    // ====================================================================
    
    public static class ScheduledJob implements Comparable<ScheduledJob> {
		
		public Long time;
		public Job job;
		
        
		public ScheduledJob( Long t, Job j )
		{
			this.time = t;
			this.job = j;
		}
		
		@Override public int compareTo( ScheduledJob other )
		{
			// - Return a negative value if THIS job 
			//		should come first
			//		ie; 	other.time > this.time
			// - Return a positive value if THIS job
			//		should come after
			// - Return zero if same time;
			
			return		(int) (this.time - other.time);
		}
	}
}
