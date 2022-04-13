package frontsnapk1ck.utility.time;

public class NumConstraint {
    
    private int num;

    public NumConstraint(int num) 
    {
        this.num = num;
    }

    public int getNum() {
        return num;
    }


    @Override
    public boolean equals(Object obj) 
    {
        if (obj instanceof NumConstraint)
            return ((NumConstraint)(obj)).getNum() == this.num;
        return false;
    }
}
