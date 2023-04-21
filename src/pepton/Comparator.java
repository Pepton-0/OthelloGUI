package pepton;

public class Comparator {
    public static int toInt(boolean b){
        return Boolean.compare(b, false);
    }

    public static boolean toBoolean(int i){
        return !(i==0);
    }
}
