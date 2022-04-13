package frontsnapk1ck.utility.cache;

import java.io.Serializable;

class Entry< K extends Serializable , H extends Serializable > implements Serializable {
    
    private H value;
    private K key;

    public Entry(K key, H value) 
    {
        this.value = value;
        this.key = key;
    }

    public K getKey() {
        return key;
    }

    public H getValue() {
        return value;
    }

    @Override
    @SuppressWarnings("rawtypes")
    public boolean equals(Object obj) 
    {
        if ( obj instanceof Entry ) 
            return ((Entry)obj).getKey().equals(getKey());
        return super.equals(obj);
    }
}