package frontsnapk1ck.utility.cache;

import java.util.List;

public abstract class AbstractCollection<T> implements Cacheable {

    private List<T> data;

    public AbstractCollection(List<T> data) 
    {
        super();
        this.data = data;
    }

    public List<T> getList()
    {
        return data;
    }
    
}
