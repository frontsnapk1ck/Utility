package frontsnapk1ck.utility;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

    private static List<List<String>> formatData(String[][] data) 
    {
        List<List<String>> table = new ArrayList<List<String>>();
        for (String[] row : data) 
            table.add(Util.arrayToList(row));
        
        return table;
    }

    public static String formatAsTable(List<List<String>> rows)
    {
        int[] maxLengths = new int[rows.get(0).size()];
        for (List<String> row : rows)
        {
            for (int i = 0; i < row.size(); i++)
            {
                maxLengths[i] = Math.max(maxLengths[i], row.get(i).length());
            }
        }
    
        StringBuilder formatBuilder = new StringBuilder();
        for (int maxLength : maxLengths)
        {
            formatBuilder.append("%-").append(maxLength + 2).append("s");
        }
        String format = formatBuilder.toString();
    
        StringBuilder result = new StringBuilder();
        for (List<String> row : rows)
        {
            result.append(String.format(format, row.toArray(new Object[0]))).append("\n");
        }
        return result.toString();
    }

    public static String joinStrings(String[] args) 
    {
		return joinStrings(args , 0);
	}

    public static String joinStrings(String[] strings, int startIndex) 
    {
        return joinStrings(strings, startIndex, strings.length);
    }

    public static String joinStrings(String[] strings, int startIndex, int endIndex)
    {
        if (startIndex < strings.length) 
        {
            StringBuilder ret = new StringBuilder(strings[startIndex]);
            endIndex = Math.min(endIndex, strings.length);
            for (int i = startIndex + 1; i < endIndex; i++) 
                ret.append(" ").append(strings[i]);
            return ret.toString();
        }
        return "";
    }

    public static String makeTable( String[][] data , String[] headers )
    {
        String[][] newArr = new String[data.length + 1][];
        newArr[0] = new String[headers.length];
        for (int i = 0; i < headers.length; i++) 
            newArr[0][i] = headers[i];   
        
        for (int i = 0; i < data.length; i++) 
            newArr[i+1] = data[i];
        
        return makeTable(newArr);
    }

    public static String makeTable(String[][] data)
    {
        return formatAsTable(formatData(data));
    }

    public static int getNumCharsInString(String s , CharSequence key)
    {
        int num = 0;
        String copy = s.toString();
        int len = key.length();
        for (int i = 0; i < s.length() - len ; i++) 
        {
            String sub = copy.substring(i, i + len);
            if (sub.equalsIgnoreCase((String) key))
                num ++;

        }

        return num;
    }

    public static String list(String[] list) 
    {
        String out = "[ ";

        if (list.length == 0)
            return out + " none ]";

        int i = 0;
        for (String string : list) 
        {
            if (i != list.length - 1)
                out += string + ", ";
            else
                out += string + " ]";
            i++;    
        }
        return out;
	}
    
}
