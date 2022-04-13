package frontsnapk1ck.utility.logger;

import static frontsnapk1ck.utility.logger.Level.DEBUG;
import static frontsnapk1ck.utility.logger.Level.ERROR;
import static frontsnapk1ck.utility.logger.Level.INFO;
import static frontsnapk1ck.utility.logger.Level.WARN;

import org.slf4j.LoggerFactory;

public class Logger implements Loggable {

    private org.slf4j.Logger logger;
    private String className;

    public Logger() 
    {
        this(null);
    }

    public Logger(String className) 
    {
        this.className = className;
    }

    /**
     * Must have initiated with a Class Name
     * @param error the message to be displayed at the warning level
     * @throws RuntimeException if called with no provided classname
     */
    public void warn( String error )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.warn(this.className, error);
    }

    @Override
    public void warn(String className, String error) 
    {
        onReceive(className, error, WARN);
        logger = LoggerFactory.getLogger(className);
        logger.warn(error);
        logger = null;
    }

    /**
     * Must have initiated with a Class Name
     * @param error the throwable that caused the error
     * @throws RuntimeException if called with no provided classname
     */
    public void error( Throwable error )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.error(this.className, error);
    }

    public void error(String className, Throwable e) 
    {
        this.error(className, e.getMessage());
    }

    /**
     * Must have initiated with a Class Name
     * @param error the message to be displayed at the error level
     * @throws RuntimeException if called with no provided classname
     */
    public void error( String error )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.error(this.className, error);
    }

    public void error( String className, String message ) 
    {
        onReceive(className, message, ERROR);
        logger = LoggerFactory.getLogger(className);
        logger.error(message);
        logger = null;
    }

    /**
     * Must have initiated with a Class Name
     * @param message the message to be displayed at the debug level
     * @throws RuntimeException if called with no provided classname
     */
    public void debug( String message )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.debug(this.className, message);
    }

    @Override
    public void debug(String className, String message) 
    {
        onReceive(className, message, DEBUG);
        logger = LoggerFactory.getLogger(className);
        logger.debug(message);
        logger = null;
    }

    /**
     * Must have initiated with a Class Name
     * @param e the exception that caused an error
     * @throws RuntimeException if called with no provided classname
     */
    public void debug( Exception e )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.debug(this.className, e);
    }

    public void debug(String className, Exception e) 
    {
        this.debug(className, e.getMessage());
	}

    /**
     * Must have initiated with a Class Name
     * @param message the message to be displayed at the information level
     * @throws RuntimeException if called with no provided classname
     */
    public void info( String message )
    {
        if (this.className == null)
            throw new RuntimeException("No ClassName Provided");
        this.info(this.className, message);
    }

    @Override
    public void info(String className, String message) 
    {
        onReceive(className , message , INFO);
        logger = LoggerFactory.getLogger(className);
        logger.info(message);
        logger = null;
    }
    
    private void onReceive(String className, String message, Level level)
    {
        Thread t = Thread.currentThread();
        onReceive(className, message, null , level , t);
    }
    
    protected void onReceive(String className, String message, Throwable error , Level level, Thread t) 
    {
        // nothing to do yet
    }
}
