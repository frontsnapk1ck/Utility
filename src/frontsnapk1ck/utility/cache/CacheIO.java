package frontsnapk1ck.utility.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

class CacheIO {

    @SuppressWarnings("unchecked")
    public static  <T> T readObjectFromFile(File file, Class<T> type) 
    {
        try (
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream oos = new ObjectInputStream(fis);
        ){
            Object object = oos.readObject();
            if (object == null) return null;
            if ( object.getClass().isAssignableFrom(type))
                return (T)object;

        } catch (IOException | ClassNotFoundException e) 
        {
        } 
        return null;
    }

    public static <T> boolean saveValueToFile(File file, T save)
    {
        try (
            FileOutputStream fis = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fis)
        ) {
            oos.writeObject( save );

            oos.flush();
            oos.close();
            return true;
        }
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean saveNewFile(File file) 
    {
        try {
            if (file.createNewFile())
                return true;
            else
                return false;
        } catch (IOException e) {
           System.err.println("an error has occurred");
           e.printStackTrace();
        }
        return false;
    }

    public static boolean saveNewFolder(File file) 
    {
        try {
            if (file.mkdir())
                return true;
            else
                return false;
        } catch (SecurityException e) {
           System.err.println("an error has occurred\nsomething about perms\n\n");
           e.printStackTrace();
        }
        return false;
    }

    public static boolean deleteFile(File file) 
    {
        return file.delete();
    }

    public static String[] readFolderContents (String path)
    {
        try 
        {
            return readFolderContents(new File(path));
        }
        catch (Exception e) 
        {
            return null;
        }
    }

    public static String[] readFolderContents(File file) 
    {
        if (!file.exists())
            return null;
        if (!file.isDirectory())
            return null;

        String[] arr = new String[file.listFiles().length];
        int i = 0;
        for (File sf : file.listFiles())
        {
            String name = sf.getName();
            name.replace(file.getAbsolutePath(), "");
            arr[i] = name;
            i++;
        }
        return arr;
	}
}
