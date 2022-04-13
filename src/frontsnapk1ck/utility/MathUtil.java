package frontsnapk1ck.utility;

public class MathUtil {

    public static long round ( double d )
    {
        return (long) MathUtil.round( d , 0);
    }

    public static double round(double d, int places) 
    {
        if ( places < 0)
            throw new RuntimeException("Cannot round to negative digits: " + places);
        
        if ( places == 0)
            return Math.round(d);
        
        int mult = (int) Math.pow(10, places);
        double newVal = d * mult;
        long roundNewValue = MathUtil.round(newVal);
        double finalRounded = roundNewValue / mult;
        return finalRounded;
    }

    
}