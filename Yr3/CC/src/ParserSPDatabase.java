import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
* Class used for SP database parsing.
* @author Duarte Parente, Ana Rita
* @since 07/11/2022
* Last change: 23/11/2022
*/
public class ParserSPDatabase {

    /** 
     * SP Database File parser
     * @return validation code regarding the success (or not) of the operation
     */
    public static Integer readSPDatabaseFile(String filepath, Map<String, List<String>> data) {
        File db = new File(filepath);
        if (!db.exists()) return 1;
        try (BufferedReader br = new BufferedReader(new FileReader(db))){
            String s;
            while ((s = br.readLine()) != null){
                if(!s.isEmpty() && s.charAt(0) != '#'){
                    
                    String parts[] = s.split(" ");
                    if (!(data.containsKey(parts[1]))) data.put(parts[1], new ArrayList<>());
                    data.get(parts[1]).add(s);
                    
                }
            }
        }
        catch (FileNotFoundException e){
            return 3;
        }
        catch (IOException e){
            return 4;
        }
        return 0;
    }

    // Parsing of the string containing the database lines used in zone transfer 
    public static Map<String, List<String>> readSPString (String s, String domain){
        Map<String, List<String>> data = new HashMap<>();
        String[] parts = s.split(";");
        for(String i : parts){
            String[] parts1 = i.split(" ");
            StringBuilder ne = new StringBuilder();
            for(int j=1; j<parts1.length-1; j++){
                ne.append(parts1[j]).append(" ");
            }
            ne.append(parts1[parts1.length-1]);
            String n = ne.toString();
            
            String[] parts2 = n.split(" ");
            if (!(data.containsKey(parts2[1]))) data.put(parts2[1], new ArrayList<>());
            data.get(parts2[1]).add(n);
       
        }
        return data;

    }

    // Collect all lines from DB file and merge them into 1 string to be sent in zone transfer
    public static String getDBFileZT() throws FileNotFoundException, IOException{
        int counter = 1;
        StringBuilder sb = new StringBuilder();
        File db = new File(ConfigHandler.getInstance().getSPDatabaseFilePath());
        try (BufferedReader br = new BufferedReader(new FileReader(db))){
            String s;
            while ((s = br.readLine()) != null){
                if(!s.isEmpty() && s.charAt(0) != '#'){
                    sb.append(counter + ": " + s + ";");
                    counter++;
                }
            }
        }
        return sb.toString();
    }
}
