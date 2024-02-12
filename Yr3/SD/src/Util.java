import java.util.ArrayList;
import java.util.List;

public class Util {

    public static int distance(String pos, String userPos){
        String[] parts = pos.split(",");
        String[] userparts = userPos.split(",");
        int x0 = Integer.parseInt(parts[0]);
        int y0 = Integer.parseInt(parts[1]);
        int x1 = Integer.parseInt(userparts[0]);
        int y1 = Integer.parseInt(userparts[1]);
        return Math.abs(x1-x0) + Math.abs(y1-y0);
    }

    public static String generateRandomPos(int N){
        int x = (int)(Math.random() * N + 1);
        int y = (int)(Math.random() * N + 1);
        return Integer.toString(x) + "," + Integer.toString(y);
    }


    public static boolean isInDistance(int D, String pos, String userPos){
        return (distance(pos, userPos) <= D);
    }

    public static List<String> getNearPositions(int D, String pos){
        List<String> res = new ArrayList<>();
        String[] parts = pos.split(",");
        int x = Integer.parseInt(parts[0]);
        int y = Integer.parseInt(parts[1]);
        int N = DataHandler.getInstance().getGridDimension();
        int sup, inf;
        String s;
        // X muda e Y não
        for (int i = 1; i<=D; i++){
            sup = x + i;
            inf = x - i;
            if (sup <= N){
                s = Integer.toString(sup) + "," + Integer.toString(y);
                res.add(s);
            }
            if (inf > 0){
                s = Integer.toString(inf) + "," + Integer.toString(y);
                res.add(s);
            }
        }
        // Y muda e X não
        for (int i = 1; i<=D; i++){
            sup = y + i;
            inf = y - i;
            if (sup <= N){
                s = Integer.toString(x) + "," + Integer.toString(sup);
                res.add(s);
            }
            if (inf > 0){
                s = Integer.toString(x) + "," + Integer.toString(inf);
                res.add(s);
            }
        }
        // X e Y mudam
        for (int i = 1; i<D; i++){
            sup = x - i;
            inf = y - i;
            if (sup > 0 && inf > 0){
                s = Integer.toString(sup) + "," + Integer.toString(inf);
                res.add(s);
            }
            sup = x - i;
            inf = y + i;
            if (sup > 0 && inf <= N){
                s = Integer.toString(sup) + "," + Integer.toString(inf);
                res.add(s);
            }
            sup = x + i;
            inf = y - i;
            if (sup <= N && inf > 0){
                s = Integer.toString(sup) + "," + Integer.toString(inf);
                res.add(s);
            }
            sup = x + i;
            inf = y + i;
            if (sup <= N && inf <= N){
                s = Integer.toString(sup) + "," + Integer.toString(inf);
                res.add(s);
            }
        }
        return res;
    }
    
}
