package frontsnapk1ck.utility.cache;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class FileDiskCache< K extends Serializable , H extends Serializable > extends DiskCache< K , H > {

    public FileDiskCache(File f) throws IOException 
    {
        super(f);
    }

    @Override
    protected void init() throws IOException 
    {
        if ( !getLocation().exists() )
        {
            CacheIO.saveNewFile(this.getLocation());
            CacheIO.saveValueToFile(getLocation(), new ArrayList<Entry<K, H>>());
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void loadMemCache()
    {
        List<Entry<K, H>> object = CacheIO.readObjectFromFile( getLocation() , List.class );
        setMemCache( object );
    }

    @Override
    protected void saveMemCache(List<Entry<K, H>> memCache) 
    {
        if (!CacheIO.saveValueToFile( this.getLocation() , this.getMemCache() ))
            System.err.println("Error saving to file");
    }

    @Override
    public void reset() 
    {
        CacheIO.saveValueToFile(this.getLocation(), new ArrayList<>());
    }
    
}
