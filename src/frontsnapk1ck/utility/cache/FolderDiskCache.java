package frontsnapk1ck.utility.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class FolderDiskCache <K extends Serializable, H extends Serializable> extends DiskCache<K, H> {


    private Map< UUID, Entry< K , H > > localEntries; 

    public FolderDiskCache(File f) throws IOException 
    {
        super(f);
        addShutdownHook();
    }

    private void addShutdownHook() 
    {
        String name = "Shutdown: " + getLocation().getName();
        Thread t = new Thread( this::handleShutdown , name );
        Runtime.getRuntime().addShutdownHook(t);
    }

    @Override
    protected void init() throws IOException
    {
        if (getLocation().isFile())
            throw new IOException("Provided file " + getLocation().getName() + " needs to be a folder");

        if (!getLocation().exists())
            CacheIO.saveNewFolder(getLocation());

        this.localEntries = loadLocalEntries();
    }

    @Override
    public void put(K key, H value) 
    {
        this.remove(key);
        List<Entry<K, H>> memCache = this.getMemCache();
        memCache.add(new Entry< K, H >(key, value));
        setMemCache(memCache);
    }

    @Override
    public boolean has(K key) 
    {
        List<Entry<K, H>> memCache = getMemCache();
        for (Entry<K,H> entry : memCache) 
        {
            if ( Objects.equals(entry.getKey() , key) )
                return true;
        }

        for (Entry<K,H> entry : this.localEntries.values()) 
        {
            if ( Objects.equals(entry.getKey() , key))
                return true;
        }
        return false;
    }

    @Override
    public H get(K key) 
    {
        List<Entry<K, H>> memCache = getMemCache();
        for (Entry<K,H> entry : memCache) 
        {
            if ( Objects.equals(entry.getKey() , key))
                return entry.getValue();
        }

        for (Entry<K,H> entry : this.localEntries.values()) 
        {
            if ( Objects.equals(entry.getKey() , key) )
                return entry.getValue();
        }
        return null;
    }

    @Override
    public H remove(K key) 
    {
        // memory
        List<Entry<K, H>> memCache = getMemCache();
        Entry<K,H> toRm = null;
        for (Entry<K,H> entry : memCache) 
        {
            if ( Objects.equals(entry.getKey() , key) )
                toRm = entry;    
        }
        if ( toRm != null )
        {
            boolean removed = memCache.remove(toRm);
            this.setMemCache(memCache);
            return removed ? toRm.getValue() : null;
        }

        //File
        UUID mapKey = null;
        File fileToDel = null;
        Set<Map.Entry<UUID, Entry<K, H>>> set = this.localEntries.entrySet();
        {
            for (java.util.Map.Entry<UUID,Entry<K,H>> fileEntry : set) 
            {
                UUID id = fileEntry.getKey();
                Entry<K, H> entry = fileEntry.getValue();
                if ( Objects.equals(entry.getKey() , key) )
                {
                    toRm = entry; 
                    mapKey = id;
                    fileToDel = new File( getLocation() + "\\" + id );
                }
            }
        }
        if ( toRm == null)
            return null;
        
        this.localEntries.remove(mapKey);
        CacheIO.deleteFile(fileToDel);
        return toRm.getValue();
    }

    public void saveMemCacheToFile() 
    {
        List<Entry<K , H>> memCache = new ArrayList<>();
        memCache.addAll( getMemCache() );

        //  Find DUplicates
        Set<Map.Entry<UUID, Entry<K, H>>> localCache = this.localEntries.entrySet();
        Map< UUID , Entry<K , H> > overwrite = new HashMap<>();
        for (Entry< K , H > memEntry : memCache) 
        {
            for (Map.Entry< UUID, Entry<K, H> >  mapEntry : localCache) 
            {
                Entry<K , H > entry = mapEntry.getValue();
                if (entry.getKey().equals(memEntry.getKey()))
                    overwrite.put(mapEntry.getKey(), memEntry);
            }
        }

        //  Overwrite Duplicates
        Set<UUID> fileIds = overwrite.keySet();
        for (UUID uuid : fileIds) 
        {
            File file = new File( getLocation() + "\\" + uuid );
            Entry<K, H> entry = overwrite.get(uuid);
            CacheIO.saveValueToFile(file, entry);
            memCache.remove(entry);    
        }

        // save memory
        for (Entry< K , H > entry : memCache) 
        {
            UUID uuid = UUID.randomUUID();
            File file = new File( getLocation() + "\\" + uuid );
            CacheIO.saveValueToFile(file, entry);
            
        }

    }

    @SuppressWarnings("unchecked")
    private Map< UUID , Entry<K, H>> loadLocalEntries() 
    {
        Map< UUID , Entry<K , H> > localEntries = new HashMap<>();
        String[] keys = CacheIO.readFolderContents(getLocation() + "\\");
        for (String name : keys) 
        {
            UUID id = UUID.fromString(name);
            String path = this.getLocation() + "\\" + name;
            File file = new File( path );
            Entry<K,H> raw = CacheIO.readObjectFromFile(file, Entry.class);
            localEntries.put( id , raw);
        }
        return localEntries;
    }

    @Override protected void loadMemCache() {}
    @Override protected void saveMemCache(List<Entry<K, H>> memCache) {}
    
    private void handleShutdown() 
    {
        // this.cleanDirectory();
        saveMemCacheToFile();   
    }

    private void cleanDirectory() 
    {
        List<File> toDel = new ArrayList<>();
        String[] childFiles = CacheIO.readFolderContents(getLocation() + "\\");
        if (childFiles == null) return;
        for (String file : childFiles) 
            toDel.add( new File( getLocation() + "\\" + file));

        for (File file : toDel)
            CacheIO.deleteFile(file);
    }

    @Override
    public void reset() 
    {
        this.setMemCache(new ArrayList<>());
        this.cleanDirectory();    
    }

}
