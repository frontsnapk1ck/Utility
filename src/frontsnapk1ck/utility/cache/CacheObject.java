package frontsnapk1ck.utility.cache;

public class CacheObject<H>
{
    private long lastAccessed;
    private H value;
    private long keepTime;
     
    public CacheObject(H value, long keepTime) 
    {
        this.value = value;
        this.lastAccessed = System.currentTimeMillis();
        this.keepTime = keepTime;
    }

    public long getKeepTime() 
    {
        return keepTime;
    }

    public long getLastAccessed() 
    {
        return lastAccessed;
    }

    public H getValue() 
    {
        this.lastAccessed = System.currentTimeMillis();
        return getValueRaw();
    }

    public H getValueRaw()
    {
        return value;
    }
}