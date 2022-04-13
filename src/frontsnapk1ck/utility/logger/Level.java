package frontsnapk1ck.utility.logger;

    public class Level
    {
        public static final Level INFO = new Level("INFO");
        public static final Level WARN = new Level("WARN");
        public static final Level DEBUG = new Level("DEBUG");
        public static final Level ERROR = new Level("ERROR");
        
        private String level;
        
        public Level(String string) 
        {
            this.level = string;
        }

        @Override
        public boolean equals(Object obj) 
        {
            if (obj instanceof Level)
                return ((Level)obj).getLevel().equalsIgnoreCase(this.getLevel());
            return false;
        }

        @Override
        public String toString() 
        {
            return this.level;
        }

        public String getLevel() 
        {
            return level;
        }
    }
