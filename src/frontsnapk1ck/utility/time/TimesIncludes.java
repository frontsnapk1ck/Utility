package frontsnapk1ck.utility.time;

import java.util.ArrayList;
import java.util.List;

public class TimesIncludes {

    private List<IncludeType> includes;
    private List<NumConstraint> constraints;

    public TimesIncludes() 
    {
        this.includes = new ArrayList<IncludeType>();
        this.constraints = new ArrayList<NumConstraint>();
    }

    public boolean addYear()
    {
        IncludeType toAdd = IncludeType.YEAR;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addMonth()
    {
        IncludeType toAdd = IncludeType.MONTH;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addDay()
    {
        IncludeType toAdd = IncludeType.DAY;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addHour()
    {
        IncludeType toAdd = IncludeType.HOUR;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addMin()
    {
        IncludeType toAdd = IncludeType.MIN;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addSec()
    {
        IncludeType toAdd = IncludeType.SEC;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean addMilis()
    {
        IncludeType toAdd = IncludeType.MILI;
        if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
    }

    public boolean add(IncludeType toAdd) 
    {
		if (!this.includes.contains(toAdd))
        {
            this.includes.add(toAdd);
            return true;
        }
        return false;
	}

    public boolean addAll()
    {
        boolean b = true;
        b = b && this.addYear();
        b = b && this.addMonth();
        b = b && this.addDay();
        b = b && this.addHour();
        b = b && this.addMin();
        b = b && this.addSec();
        b = b && this.addMilis();
        return b;
    }

    public boolean limit(int num)
    {
        NumConstraint cont = new NumConstraint(num);
        if (!this.constraints.contains(cont))
        {
            this.constraints.add(cont);
            return true;
        }
        return false;
    }

    public boolean has(IncludeType type) 
    {
		return this.includes.contains(type);
    }
    
    public List<NumConstraint> getConstraints() {
        return constraints;
    }
    
}
