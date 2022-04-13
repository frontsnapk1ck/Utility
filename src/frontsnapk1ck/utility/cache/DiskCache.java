package frontsnapk1ck.utility.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class DiskCache< K extends Serializable , H extends Serializable > implements Cache< K , H > , Serializable {

    private List< Entry<K, H> > memCache;
    private File location;

    public DiskCache( File f ) throws IOException 
    {
        this.location = f;
        this.memCache = new ArrayList<>();
        this.init();
    }

    /**
     * called on start
     * @throws IOException
     */
    protected abstract void init() throws IOException;
    
    /**
     * used to load all of the cached objects into memCache
     */
    protected abstract void loadMemCache();

    /**
     * called to save all of the cached files to a location
     * @param memCache
     */
    protected abstract void saveMemCache(List<Entry<K, H>> memCache);

    /**
     * called to reset the contents of the cache
     */
    public abstract void reset();


    @Override
    public void put(K key, H value) 
    {
        if ( keySet().contains(key ) ) 
            remove( key );
        
        Entry<K, H> entry = new Entry<K, H>(key , value);
        this.getMemCache().add(entry);
        this.saveMemCache();
    }

    @Override
    public H get(K key)
    {
        for (Entry<K, H> entry : this.getMemCache()) 
        {
            if ( entry.getKey().equals(key))
                return entry.getValue();    
        }

        this.loadMemCache();

        for (Entry<K, H> entry : this.getMemCache()) 
        {
            if ( entry.getKey().equals(key))
                return entry.getValue();    
        }

        return null;
    }

    @Override
    public H remove(K key) 
    {
        this.loadMemCache();
        Entry<K,H> toRm = null;
        List<Entry<K, H>> list = getMemCache();
        for (Entry<K,H> entry : list) 
        {
            if (entry.getKey().equals(key))
                toRm = entry;
        }
        boolean removed = list.remove(toRm);
        setMemCache(list);
        H out = removed ? toRm.getValue() : null;
        return out;
    }
    
    @Override
    public Set<K> keySet() 
    {
        this.loadMemCache();
        Set<K> keys = new HashSet<>();
        for (Entry<K, H> e : this.memCache)
            keys.add(e.getKey());
        return keys;
    }

    @Override
    public int size() 
    {
        return this.keySet().size();
    }

    @Override
    public boolean has(K key)
    {
        boolean has = this.keySet().contains(key);
        return has;
    }

    public File getLocation() 
    {
        return location;
    }

    public List<Entry<K, H>> getMemCache() 
    {
        return memCache;
    }

    public void setMemCache(List<Entry<K, H>> memCache) 
    {
        if ( memCache == null) 
            memCache = new ArrayList<>();
        this.memCache = memCache;
        saveMemCache(this.memCache);
    }

    public void saveMemCache() 
    {
        saveMemCache(this.memCache);
    }
   
}
