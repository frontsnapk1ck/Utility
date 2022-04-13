package frontsnapk1ck.utility.time;

import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import frontsnapk1ck.utility.Util;

public class TimeUtil {

    public static final long SECOND_MILLIS   = 1000;
    public static final long MINUTE_MILLIS   = 60 * SECOND_MILLIS;
    public static final long HOUR_MILLIS     = 60 * MINUTE_MILLIS;
    public static final long DAY_MILLIS      = 24 * HOUR_MILLIS;
    public static final long WEEK_MILLIS     = 7  * DAY_MILLIS;

    private static final SimpleDateFormat timestamp = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    private static final SimpleDateFormat ymdFormat = new SimpleDateFormat("yyyy/MM/dd");
    private static final Map<Character, Long> TIME_SYMBOLS = new HashMap<>();

    static {
        TIME_SYMBOLS.put('w', WEEK_MILLIS);
        TIME_SYMBOLS.put('d', DAY_MILLIS);
        TIME_SYMBOLS.put('h', HOUR_MILLIS);
        TIME_SYMBOLS.put('m', MINUTE_MILLIS);
        TIME_SYMBOLS.put('s', SECOND_MILLIS);
    }

    public static String getTimestampFormat() 
    {
        return getTimestampFormat(System.currentTimeMillis());
    }

    public static String getTimestampFormat(long time) 
    {
        try 
        {
            return getTimestampFormat( new Date( time ) );
        }
        catch (Exception e) 
        {
            return "cant figure out (" + time + ")";
        }
    }

    private static String getTimestampFormat(Date date) 
    {
        return timestamp.format( date );
    }

    public static String formatYMD() 
    {
        return formatYMD(System.currentTimeMillis());
    }

    public static String formatYMD(long time) 
    {
        try 
        {
            return getTimestampFormat( new Date( time ) );
        }
        catch (Exception e) 
        {
            return "cant figure out (" + time + ")";
        }
    }

    public static String formatYMD(Date date) 
    {
        return ymdFormat.format(date);
    }

    public static long toMillis(String s) 
    {
        s = s.toLowerCase();
        long val = 0;
        String working = "";
        for (int i = 0; i < s.length(); i++) 
        {
            char c = s.charAt(i);
            if (Character.isDigit(c))
                working += c;
            else if (TIME_SYMBOLS.containsKey(c) && working.length() > 0)
                val += Util.parseInt(working.toString(), 0) * TIME_SYMBOLS.get(c);
        }
        if (val == 0 && working.length() != 0)
            val += Util.parseInt(working.toString(), 0);
        return val;
    }

    public static String getRelativeTime(long time) 
    {
        return getRelativeTime(time, true);
    }

    public static String getRelativeTime(long time, boolean shortText) 
    {
        return getRelativeTime(time, shortText, false);
    }

    public static String getRelativeTime(long time, boolean shortText, boolean offsetNow) 
    {
        long usedTime = time;
        boolean future = false;
        String chronology = "ago";
        long now = System.currentTimeMillis();
        if (usedTime <= 0)
            return "???";

        long diff = time;
        if (usedTime > now && !offsetNow) 
        {
            diff = usedTime - now;
            chronology = "from now";
            future = true;
        } else if (!offsetNow)
            diff = now - usedTime;

        if (diff < MINUTE_MILLIS)
            return (diff / SECOND_MILLIS) + (shortText ? "s" : " seconds " + chronology);
        else if (diff < 2 * MINUTE_MILLIS)
            return shortText ? "~1m" : "about a minute " + chronology;
        else if (diff < 50 * MINUTE_MILLIS)
            return diff / MINUTE_MILLIS + (shortText ? "m" : " minutes " + chronology);
        else if (diff < 90 * MINUTE_MILLIS)
            return shortText ? "~1h" : "about an hour " + chronology;
        else if (diff < 24 * HOUR_MILLIS)
            return diff / HOUR_MILLIS
                    + (shortText ? "h" : " hour" + (diff / HOUR_MILLIS == 1 ? "" : "s") + " " + chronology);
        else if (diff < 48 * HOUR_MILLIS)
            return shortText ? "~1d" : future ? "about a day" : "yesterday";
        else if (diff < 14 * DAY_MILLIS || !shortText)
            return diff / DAY_MILLIS + (shortText ? "d" : " day" + (diff == 1 ? "" : "s") + " " + chronology);
        return ">2w";
	}

    public static String getTimeAgo(OffsetDateTime time) {
        TimesIncludes includes = new TimesIncludes();
        includes.addAll();
        return getTimeAgo(time, includes);
    }

    public static String getTimeAgo(OffsetDateTime time, TimesIncludes includes) {
        // getting info out of the time passed
        int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        int year = time.getYear();

        int hour = time.getHour();
        int min = time.getMinute();

        String monthS = fixHangingZero(month);
        String dayS = fixHangingZero(day);
        String hourS = fixHangingZero(hour);
        String minS = fixHangingZero(min);

        // getting current info
        OffsetDateTime now = OffsetDateTime.now();
        int monthNow = now.getMonthValue();
        int dayNow = now.getDayOfMonth();
        int yearNow = now.getYear();

        int hourNow = now.getHour();
        int minNow = now.getMinute();

        // finding difference between the numbers
        int yearDiff = yearNow - year;
        int monthDiff = monthNow - month;
        int dayDiff = dayNow - day;

        int hourDiff = hourNow - hour;
        int minDiff = minNow - min;

        // fixing negative numbers
        if (minDiff < 0) {
            hourDiff--;
            minDiff += 60;
        }

        if (hourDiff < 0) {
            dayDiff--;
            hourDiff += 24;
        }

        if (dayDiff < 0) {
            monthDiff--;
            int daysInMonth = getDaysInMonth(time);
            dayDiff += daysInMonth;
        }

        if (monthDiff < 0) {
            yearDiff--;
            monthDiff += 12;
        }

        // declaring booleans
        boolean yearB = includes.has(IncludeType.YEAR);
        boolean monthB = includes.has(IncludeType.MONTH);
        boolean dayB = includes.has(IncludeType.DAY);
        boolean hourB = includes.has(IncludeType.HOUR);
        boolean minB = includes.has(IncludeType.MIN);

        // setup for limiting
        int limit = getLimit(includes.getConstraints());
        int current = 0;
        if (limit >= 0)
        {
            //year
            if (yearB && current != limit)
                current ++;
            else 
                yearB = false;

            //month
            if (monthB && current != limit)
                current ++;
            else 
                monthB = false;
            
            // day
            if (dayB && current != limit)
                current ++;
            else 
                dayB = false;
            
            //hour
            if (hourB && current != limit)
                current ++;
            else 
                hourB = false;
            
            //min
            if (minB && current != limit)
                current ++;
            else 
                minB = false;
        }

        // rounding
        if (!minB && minDiff >= 30)
            hourDiff++;
        if (!hourB && hourDiff >= 12)
            dayDiff++;
        if (!dayB & dayDiff >= (getDaysInMonth(time) / 2))
            monthDiff++;
        if (!monthB && monthDiff >= 6)
            yearDiff++;

        // building final
        String date = monthS + "/" + dayS + "/" + year + "\t" + hourS + ":" + minS;
        String diff = "";

        if (yearDiff >= 1 && yearB)
            diff += yearDiff + " year" + (yearDiff == 1 ? " " : "s ");
        if (monthDiff >= 1 && monthB)
            diff += monthDiff + " month" + (monthDiff == 1 ? " " : "s ");
        if (dayDiff >= 1 && dayB)
            diff += dayDiff + " day" + (dayDiff == 1 ? " " : "s ");
        if (hourDiff >= 1 && hourB)
            diff += hourDiff + " hour" + (hourDiff == 1 ? " " : "s ");
        if (minDiff >= 1 && minB)
            diff += minDiff + " minute" + (minDiff == 1 ? " " : "s ");

        String out = date + "\t(" + diff + "ago)";
        return out;
    }

    private static int getLimit(List<NumConstraint> constraints) 
    {
        int max = -1;
        for (NumConstraint c : constraints) 
        {
            if (c.getNum() > max)    
                max = c.getNum();
        }
        return max;
    }

    private static int getDaysInMonth(OffsetDateTime date) 
    {
        int month = date.getMonthValue();
        int year = date.getYear();
        return daysInMonth(month, year);
    }

    private static int daysInMonth(int month, int year) 
    {
        if (month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12 )
		{
			return 31;
		}
		else if ( month == 4 || month == 6 || month == 9 || month == 11 )
		{
			return 30;
		}  
		else if ( month == 2 )
		{
            if (isLeapYear(year))
                return 29;
            return 28;
        } 
        return -1;
    }

    private static boolean isLeapYear(int year) 
    {
        return (((year % 4 == 0) && (year % 100!= 0)) || (year%400 == 0));
        
    }

    private static String fixHangingZero(int num)
    {
        return fixHangingZero(num, 2);
    }

    /**
     * 
     * @param num the number you want to want to add the zeros to
     * @param zeros the number of digits there should be
     * @return a string with the appropriate number of 
     */
    private static String fixHangingZero(int num, int zeros) 
    {
        String out = "" + num;
        while(out.length() < zeros)
            out = "0" + out;
        return out;
    }

    public static String now() 
    {
        return date() + "\t" + time();
	}

    public static String date() 
    {
		OffsetDateTime time = OffsetDateTime.now();
		int month = time.getMonthValue();
        int day = time.getDayOfMonth();
        int year = time.getYear();

        String monthS = fixHangingZero(month);
        String dayS = fixHangingZero(day);

        String date = monthS + "/" + dayS + "/" + year;

        return date;
	}

    public static String time() 
    {
        OffsetDateTime time = OffsetDateTime.now();

        int hour = time.getHour();
        int min = time.getMinute();
        int sec = time.getSecond();

        String hourS = fixHangingZero(hour);
        String minS = fixHangingZero(min);
        String secS = fixHangingZero(sec);

        String date = hourS + ":" + minS + ":" + secS;
        return date;
	}

    public static String getMidTime(long time) 
    {
        Date d = new Date(time);
        
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min =  c.get(Calendar.MINUTE);
        int sec =  c.get(Calendar.SECOND);
        int mili=  c.get(Calendar.MILLISECOND);

        String hourS = fixHangingZero(hour);
        String minS = fixHangingZero(min);
        String secS = fixHangingZero(sec);
        String miliS = fixHangingZero(mili , 3);

        String date = hourS + ":" + minS + ":" + secS + "." + miliS;
        return date;
    }

    /**
     * 
     * @param time the length, in millis, that you are converting to normal time
     */
    public static String getTimeShortFromRelative (long time)
    {
        String timeS = String.valueOf(time);
        boolean roundup = false;

        if (timeS.length() > 3)
        {
            String sub = timeS.substring(timeS.length() - 3);
            if ( Long.parseLong(sub) >- 500L )
                roundup = true;
        }

        int seconds = (int) (time / 1000) + (roundup ? 1 : 0);
        int mins = (int)(seconds/60);
        int hours = (int)(mins/60);

        seconds %= 60;
        mins %= 60;
        
        String out = "";

        String minsS = "";
        String secondS = fixHangingZero(seconds);
        if (hours != 0)
        {
            out += hours + ":";
            minsS = fixHangingZero( mins );
        }
        else
            minsS = String.valueOf(mins);
        out += minsS + ":" + secondS;
        return out;
    }

    public static String getTimeTill(long time, TimesIncludes includes) 
    {
        Date d = new Date(time);
        
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        // getting info out of the time passed
        int month = c.get(Calendar.MONTH) + 1;
        int day =   c.get(Calendar.DAY_OF_MONTH);
        int year =  c.get(Calendar.YEAR);

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int min =  c.get(Calendar.MINUTE);
        int sec =  c.get(Calendar.SECOND);

        String monthS = fixHangingZero(month);
        String dayS = fixHangingZero(day);

        String hourS = fixHangingZero(hour);
        String minS = fixHangingZero(min);
        String secS = fixHangingZero(sec);

        // getting current info
        OffsetDateTime now = OffsetDateTime.now();
        int monthNow = now.getMonthValue();
        int dayNow = now.getDayOfMonth();
        int yearNow = now.getYear();

        int hourNow = now.getHour();
        int minNow = now.getMinute();
        int secNow = now.getSecond();

        // finding difference between the numbers
        int yearDiff =  year - yearNow;
        int monthDiff = month - monthNow;
        int dayDiff =   day - dayNow;

        int hourDiff =  hour - hourNow;
        int minDiff =   min - minNow;
        int secDiff =   sec - secNow;
        
        // fixing negative numbers
        if (secDiff < 0){
            minDiff --;
            secDiff += 60;
        }

        if (minDiff < 0) {
            hourDiff--;
            minDiff += 60;
        }

        if (hourDiff < 0) {
            dayDiff--;
            hourDiff += 24;
        }

        if (dayDiff < 0) {
            monthDiff--;
            int daysInMonth = getDaysInMonth(time);
            dayDiff += daysInMonth;
        }

        if (monthDiff < 0) {
            yearDiff--;
            monthDiff += 12;
        }

        // declaring booleans
        boolean yearB = includes.has(IncludeType.YEAR);
        boolean monthB = includes.has(IncludeType.MONTH);
        boolean dayB = includes.has(IncludeType.DAY);
        boolean hourB = includes.has(IncludeType.HOUR);
        boolean minB = includes.has(IncludeType.MIN);
        boolean secB = includes.has(IncludeType.SEC);
        
        // setup for limiting
        int limit = getLimit(includes.getConstraints());
        int current = 0;
        if (limit >= 0)
        {
            //year
            if (yearB && current != limit && yearDiff != 0)
                current ++;
            else 
                yearB = false;

            //month
            if (monthB && current != limit && monthDiff != 0)
                current ++;
            else 
                monthB = false;
            
            // day
            if (dayB && current != limit && dayDiff != 0)
                current ++;
            else 
                dayB = false;
            
            //hour
            if (hourB && current != limit && hourDiff != 0)
                current ++;
            else 
                hourB = false;
            
            //min
            if (minB && current != limit && minDiff != 0)
                current ++;
            else 
                minB = false;
                
            //sec
            if (secB && current != limit && secDiff != 0)
                current ++;
            else 
                secB = false;
        }

        // rounding
        if (!secB && secDiff >= 30)
            minDiff ++;
        if (!minB && minDiff >= 30)
            hourDiff++;
        if (!hourB && hourDiff >= 12)
            dayDiff++;
        if (!dayB & dayDiff >= (getDaysInMonth(time) / 2))
            monthDiff++;
        if (!monthB && monthDiff >= 6)
            yearDiff++;

        // building final
        String date = monthS + "/" + dayS + "/" + year + "\t" + hourS + ":" + minS + ":" + secS;
        String diff = "";

        if (yearDiff >= 1 && yearB)
            diff += yearDiff + " year" + (yearDiff == 1 ? " " : "s ");
        if (monthDiff >= 1 && monthB)
            diff += monthDiff + " month" + (monthDiff == 1 ? " " : "s ");
        if (dayDiff >= 1 && dayB)
            diff += dayDiff + " day" + (dayDiff == 1 ? " " : "s ");
        if (hourDiff >= 1 && hourB)
            diff += hourDiff + " hour" + (hourDiff == 1 ? " " : "s ");
        if (minDiff >= 1 && minB)
            diff += minDiff + " minute" + (minDiff == 1 ? " " : "s ");
        if (secDiff >= 1 && secB)
            diff += secDiff + " second" + (secDiff ==1 ? " " : "s ");

        String out = date + "\t(" + diff + "until)";
        return out;
    }

    private static int getDaysInMonth(long time) 
    {
        Date d = new Date(time);
        
        Calendar c = Calendar.getInstance();
        c.setTime(d);

        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        return daysInMonth(month, year);
    }


    
}
