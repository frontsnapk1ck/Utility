package frontsnapk1ck.utility.time;

public class IncludeType
{
    public static final IncludeType ALL          = new IncludeType("ALL");
    public static final IncludeType YEAR         = new IncludeType("YEAR");
    public static final IncludeType MONTH        = new IncludeType("MONTH");
    public static final IncludeType DAY          = new IncludeType("DAY");
    public static final IncludeType HOUR         = new IncludeType("HOUR");
    public static final IncludeType MIN          = new IncludeType("MIN");
    public static final IncludeType SEC          = new IncludeType("MIN");
    public static final IncludeType MILI         = new IncludeType("MILI");
    
    private String id;

    public IncludeType(String string) 
    {
        this.id = string;
    }

    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof IncludeType)
            return ((IncludeType)(obj)).getId().equals(this.id);
        return false;
    }

    private String getId() {
        return id;
    }
}