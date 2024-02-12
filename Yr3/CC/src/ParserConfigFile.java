import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Class used for parsing the configuration file.
* @author Duarte Parente
* @since 07/11/2022
* Last change: 23/11/2022
*/
public class ParserConfigFile {

    /** 
     * Configurations File parser
     * @return validation code regarding the success (or not) of the operation
     */
    public static Integer readConfigFile(String filepath, Map<String, Map<String, List<String>>> configData) {
        File config = new File(filepath);
        try (BufferedReader br = new BufferedReader(new FileReader(config))) {
            String s;
            while ((s = br.readLine()) != null){
                if(!s.isEmpty() && s.charAt(0) != '#'){
                    String[] parts = s.split(" ");
                    if (!(configData.containsKey(parts[0]))) configData.put(parts[0],new HashMap<>());
                    if (!(configData.get(parts[0]).containsKey(parts[1]))) configData.get(parts[0]).put(parts[1],new ArrayList<>());
                    configData.get(parts[0]).get(parts[1]).add(parts[2]);
                }
            }
            return 0;
        }
        catch (IOException e){
            return 1;
        }
    }
}
