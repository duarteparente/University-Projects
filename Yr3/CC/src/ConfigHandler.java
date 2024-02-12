import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* Class used to hold the module related to server configuration.
* @author Duarte Parente
* @since 02/12/2022
* Last change: 
*/
public class ConfigHandler {

    private static ConfigHandler configInstance;
    
    private Map<String, Map<String, List<String>>> configData;
    private Integer validationCode;


    /** 
     * ConfigHanler class constructor
     */
    private ConfigHandler(){
        configData = new HashMap<>();
    }

    
    /** 
     * @return An instance of this class (creating it if there isn't one)
     */
    public static ConfigHandler getInstance(){
        if (configInstance == null){
            configInstance = new ConfigHandler();
        }
        return configInstance; 
    }


    /** 
     * 'validationCode' getter
     */
    public Integer getvalidationCode(){
        return this.validationCode;
    }

    
    /** 
     * @return filepath for 'all' log file
     */
    public String getAllLogFile(){
        return configData.get("all").get("LG").get(0);
    }

    
    /** 
     * @return filepath for the domain's log file
     */
    public String getLogFile(){
        return configData.get(getDomain()).get("LG").get(0);
    }


    /**
     * @return filepath for the SP database
     */
    public String getSPDatabaseFilePath(){
        return configData.get(getDomain()).get("DB").get(0);
    }


    /**
     * @return filepath for the ST List
     */
    public String getSTListFilePath(){
        return configData.get("root").get("ST").get(0);
    }


    /**
     * @return filepath for the ST List
     */
    public String getPrimaryServerIP(){
        String ip = configData.get(getDomain()).get("SP").get(0);
        if (ip.contains(":")){
            String[] parts = ip.split(":");
            ip = parts[0];
        }
        return ip;
    }


    /** 
     * @return the domain specified in config file 
     */
    public String getDomain(){
        for(String i : configData.keySet()){
            if (i.equals("all") || i.equals("root"));
            else return i;
        }
        return "";
    }
    
    public boolean hasST(){
        return configData.containsKey("root");
    }

    public boolean hasDD(){
        return configData.get(getDomain()).containsKey("DD");
    }


    public List<String> getDDIPs(){
        return configData.get(getDomain()).get("DD");
    }


    /** 
     * Get server type based on its configuration file
     * @return 0 -> SP, 1 -> SS, 2 -> SR
     */
    public Integer checkServerType(){
        if (configData.get(getDomain()).containsKey("DB")) return 0;
        if (configData.get(getDomain()).containsKey("SP")) return 1;
        return 2;
    }

    
    /** 
     * Method in charge of handling configuration data reading, parsing and storage
     * @param filepath Configuration Filepath
     * @return Validation code regarding the success of the operation
     */
    public TimeStamp readConfigFile(String filepath) throws IOException{
        this.validationCode = ParserConfigFile.readConfigFile(filepath, configData);
        return new TimeStamp();
    }
    
}
