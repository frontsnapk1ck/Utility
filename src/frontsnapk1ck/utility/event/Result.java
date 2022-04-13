package frontsnapk1ck.utility.event;

public interface Result<T> {

    public boolean isDone();

    public T get() throws InterruptedException;

}
