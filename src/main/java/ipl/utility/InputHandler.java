package ipl.utility;

public class InputHandler {
    public static Integer integerInput(String s)
    {
        if (s == null) return null;
        try
        {
            return Integer.parseInt(s);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }

    public static Double doubleInput(String s)
    {
        if (s == null) return null;
        try
        {
            return Double.parseDouble(s);
        }
        catch(NumberFormatException e)
        {
            return null;
        }
    }

    public static String capitalizeWord(String str){
        if(str == null || str.isEmpty()) return "";
        String[] words=str.split("\\s");
        String finalWord="";
        for(String w:words){
            String first=w.substring(0,1);
            String after=w.substring(1);
            finalWord+=first.toUpperCase()+after.toLowerCase()+" ";
        }
        return finalWord.trim();
    }
}
