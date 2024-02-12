import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
* Class used for parsing the ST list file.
* @author Inês Castro
* @since 07/11/2022
* Last change: 16/11/2022
*/
public class ParserSTFile {

    /** 
     * ST List File parser
     * @return validation code regarding the success (or not) of the operation
     */
    public static Integer readDataFile(String filepath, List<String> rootservers) {
        File data = new File(filepath);
        try (BufferedReader br = new BufferedReader(new FileReader(data))) {
            String s;
            while ((s = br.readLine()) != null){
                if(!s.isEmpty() && s.charAt(0) != '#') rootservers.add(s);
            }
        }
        catch(FileNotFoundException e){
            return 5;
        }
        catch(IOException e){
            return 6;
        }
        return 0;
    }

}
