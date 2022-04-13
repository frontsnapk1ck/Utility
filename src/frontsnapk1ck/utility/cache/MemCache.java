package frontsnapk1ck.utility.cache;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class MemCache< K , H > implements Cache< K , H > {

    public static final long DEFAULT_TIME = 5000L;
    public static final long DEFAULT_INTERVAL = 50L;
    public static final int  DEFAULT_MAX_ITEMS = 1000;
    
    public static final long FOREVER = -1L;
    
    public static final String  DEFAULT_NAME = "Unnamed Cache";

    private long keepTime;
    private long interval;
    private Map< K , CacheObject<H> > cacheMap;
    private Thread cacheThread;

    private boolean running;
    private String name;

    public MemCache() 
    {
        this(DEFAULT_TIME);
    }

    public MemCache(String name ) 
    {
        this(name , DEFAULT_TIME);
    }

    public MemCache(long keepTime) 
    {
        this(keepTime , DEFAULT_INTERVAL);
    }

    public MemCache(String name , long keepTime) 
    {
        this(name , keepTime , DEFAULT_INTERVAL);
    }

    public MemCache(String name , long keepTime, int maxItems) 
    {
        this(name , keepTime, DEFAULT_INTERVAL, maxItems);
    }
 
    public MemCache(long keepTime, int maxItems) 
    {
        this(DEFAULT_NAME , keepTime, DEFAULT_INTERVAL, maxItems);
    }

    public MemCache(String name , long keepTime, long interval) 
    {
        this(name , keepTime, interval , DEFAULT_MAX_ITEMS);
    }
 
    public MemCache(long keepTime, long interval) 
    {
        this(DEFAULT_NAME , keepTime, interval , DEFAULT_MAX_ITEMS);
    }
 
    public MemCache(String name , long keepTime, long interval, int maxItems) 
    {
        this.keepTime = keepTime;
        this.interval = interval;
        this.cacheMap = new HashMap< K, CacheObject<H> >(maxItems);
        this.name = name;
        this.cacheThread = makeCacheThread();
        this.start();
    }

    private void start() 
    {
        this.running = true;
        this.cacheThread.start();
    }

    protected Thread makeCacheThread() 
    {
        Thread t = new Thread( new Runnable()
        {
            public void run() 
            {
                cacheLoop();
            };
        } );
        t.setDaemon( true );
        t.setName(this.name + " Cache");
        return t;
    }

    protected void cacheLoop() 
    {
        while (this.running)
        {
            cooldown();
            cleanup();
        }
    }

    private void cooldown() 
    {
        try 
        {
            Thread.sleep(interval);
        }
        catch (InterruptedException e){
        }
    }

    public void cleanup() 
    {
        long now = System.currentTimeMillis();

        List<K> toRm = new ArrayList<K>();

        synchronized (cacheMap) 
        {
            Set<K> keys = cacheMap.keySet();
            for (K key : keys)
            {
                CacheObject<H> c = cacheMap.get(key);
                long rmTime = c == null  ? 0 : c.getKeepTime() + c.getLastAccessed();
                boolean rm = now > rmTime &&
                                c.getKeepTime() != FOREVER;
                                
                if (rm)
                    toRm.add(key);
            }
        }

        for (K key : toRm) 
        {
            System.err.println(key);
            synchronized (cacheMap) 
            {
                CacheObject<H> removed = cacheMap.remove(key);
                removed(removed.getValue());
            }
        }
    }

    protected void removed(H value) 
    {
        //nothing to do yet
    }

    @Override
    public void put(K key, H value) 
    {
        this.put(key, value, keepTime);
    }

    public void put(K key, H value , long keepTime) 
    {
        synchronized (cacheMap) 
        {
            cacheMap.put(key, new CacheObject<H>(value , keepTime));
        }
    }

    @Override
    public H get(K key) 
    {
        synchronized (cacheMap) 
        {
            CacheObject<H> c = (CacheObject<H>) cacheMap.get(key);
 
            if (c == null)
                return null;
            else 
                return c.getValue();
        }
    }

    @Override
    public Set<K> keySet() 
    {
        return this.cacheMap.keySet();
    }

    @Override
    public H remove(K key) 
    {
        synchronized (cacheMap) 
        {
            return cacheMap.remove(key).getValue();
        }
    }

    @Override
    public int size() 
    {
        synchronized (cacheMap) 
        {
            return cacheMap.size();
        }
    }

    @Override
    public boolean has(K key) 
    {
        synchronized (cacheMap) 
        {
            return cacheMap.containsKey(key);
        }
    }

    public void stop()
    {
        this.running = false;
    }
 
    /**
     * make sure you are careful and dont trigger any concurrent modification 
     * errors.
     * 
     * @return the {@link Map} of a parameterized type to the {@link CacheObject} <H>. 
     */
    protected Map<K, CacheObject<H>> getCacheMap() 
    {
        return cacheMap;
    }
 
}
