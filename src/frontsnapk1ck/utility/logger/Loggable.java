package frontsnapk1ck.utility.logger;

public interface Loggable {

    public void warn(String className, String error);

    public void error( String className, String message );

    public void debug(String className, String message);

    public void info(String className, String message);

}
