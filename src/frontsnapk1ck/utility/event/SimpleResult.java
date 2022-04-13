package frontsnapk1ck.utility.event;

public class SimpleResult<T> implements Result<T> {

    public static final Long DELAY_INTERVAL = 1L;

    private State state;
    private T result;

    public SimpleResult() 
    {
        super();
        this.state = State.WORKING;
    }

    public void finish(T result)
    {
        this.result = result;
        this.state = State.FINISHED;
    }

    @Override
    public boolean isDone()
    {
        return this.state.equals(State.FINISHED);
    }

    @Override
    public T get() throws InterruptedException
    {
        while (!this.state.equals(State.FINISHED))
            Thread.sleep(DELAY_INTERVAL);
        return this.result;
    }

    protected static class State {

        public static final State FINISHED = new State("finished");
        public static final State WORKING = new State("WORKING");
        
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

        public String getId()
        {
            return id;
        }
    }
    
}
