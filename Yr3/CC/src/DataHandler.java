import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataHandler {

    private static DataHandler dataInstance;

    private String mode;
    private Integer port;
    private Integer timeout;
    private List<String> stData; 
    private Map<String, List<String>> dbData;
    private Cache cache;
    private Integer dbVersion;
    private Integer dbValidationCode;
    private Integer stValidationCode;

    /** 
     * DataHandler class constructor
     */
    private DataHandler(){
        this.stData = new ArrayList<>();
        this.dbData = new HashMap<>();
        
        this.dbVersion = 0;
    }


    /** 
     * @return An instance of this class (creating it if there isn't one)
     */
    public static DataHandler getInstance(){
        if (dataInstance == null){
            dataInstance = new DataHandler();
        }
        return dataInstance; 
    }


    /** 
     * 'dbValidationCode' getter
     */
    public Integer getDBValidationCode(){
        return this.dbValidationCode;
    }


    /** 
     * 'stValidationCode' getter
     */
    public Integer getSTValidationCode(){
        return this.stValidationCode;
    }


    /** 
     * 'mode' getter
     */
    public String getMode(){
        return this.mode;
    }


    /** 
     * 'mode' setter
     */
    public void setMode(String mode){
        this.mode = mode;
    }

    /** 
     * 'port' getter
     */
    public Integer getPort(){
        return this.port;
    }


    /** 
     * 'port' setter
     */
    public void setPort(Integer port){
        this.port = port;
    }


    /** 
     * 'timeout' getter
     */
    public Integer getTimeout(){
        return this.timeout;
    }


    /** 
     * 'timeout' setter
     */
    public void setTimeout(Integer timeout){
        this.timeout = timeout;
    }


    /** 
     * 'timeout' getter
     */
    public Integer getDBVersion(){
        return this.dbVersion;
    }


    /** 
     * Get the DB version from the 'SOASERIAL' parameter in DB file
     */
    public void setDBVersion(Integer dbVersion){
        this.dbVersion = dbVersion;
    }


    /** 
     * Get ST IPs
     */
    public List<String> getSTIPs(){
        List<String> res = new ArrayList<>();
        for (String i : this.stData){
            res.add(i);
        }
        return res;
    }

    public void initializeCache() throws IOException{
        this.cache = new Cache();
        LogHandler.getInstance().internalActivity(new TimeStamp(), "cache-create", "", this.mode);
    }


    /** 
     * Method in charge of handling SP database reading, parsing and storage
     * @param filepath Configuration Filepath
     * @return Validation code regarding the success of the operation
     */
    public TimeStamp readSPDataFile(String filepath){
        this.dbValidationCode = ParserSPDatabase.readSPDatabaseFile(filepath, dbData);
        if (this.dbValidationCode == 0 && this.dbData.containsKey("SOASERIAL")){
            String[] parts  = this.dbData.get("SOASERIAL").get(0).split(" ");
            this.dbVersion = Integer.parseInt(parts[2]);
        }
        return new TimeStamp();
    }


    /** 
     * Method in charge of handling root servers list file reading, parsing and storage
     * @param filepath Root Servers (ST List) Filepath
     * @return Validation code regarding the success of the operation
     */
    public TimeStamp readSTConfigFile(String filepath){
        this.stValidationCode = ParserSTFile.readDataFile(filepath, stData);
        return new TimeStamp();
    }


    public void fillResponseValues(QueryHandler query){
        String name = query.getQueryInfoName();
        String typeOfValue = query.getQueryInfoType();

        for(String i : this.dbData.get(typeOfValue)){
            String parts[] = i.split(" ");
            if (parts[0].equals(name)){
                query.addResponseValue(i);
                for(String j : this.dbData.get("A")){
                    String parts2[] = j.split(" ");
                    if (parts[2].equals(parts2[0]) && !(parts2[0].equals(query.getQueryInfoName()))){
                        query.addExtraValue(j);
                    } 
                }
            }
        }
    }

    public void fillAuthoritiesValues(QueryHandler query){
        String name = query.getQueryInfoName();
        String match = "";
        for(String i : this.dbData.get("NS")){
            String parts[] = i.split(" ");
            if (name.contains(parts[0]) && (parts[0].length() > match.length())){
                match = parts[0];
            }
        }
        for(String i : this.dbData.get("NS")){
            String parts[] = i.split(" ");
            if (parts[0].equals(match)){
                query.addAuthoritiesValue(i);
                for(String j : this.dbData.get("A")){
                    String parts2[] = j.split(" ");
                    if (parts[2].equals(parts2[0]) && !(parts2[0].equals(query.getQueryInfoName()))){
                        query.addExtraValue(j);
                    } 
                }
            }
        }

    }


    /*
     * Inspect query received and build the answer based on field type of value
     */
    public void prepare_answer(QueryHandler query){
        String domain = ConfigHandler.getInstance().getDomain();
        String queryName = query.getQueryInfoName();
        if (query.getQueryInfoType().equals("A")){
            int l = queryName.indexOf('.');
            for (int i=0; i<l+1; i++){
                queryName = queryName.substring(1);
            }
        }
        System.out.println(queryName);

        if (domain.equals(queryName)){
            query.setResponseCode(0);
            fillResponseValues(query);
            fillAuthoritiesValues(query);
        } 
        else{
            if (queryName.contains(domain)) query.setResponseCode(1);
            else query.setResponseCode(2);
            fillAuthoritiesValues(query);
        }

        if (query.getResponseCode() == 0 && query.getNrValues() == 0){
            query.setResponseCode(1);
        }
        query.setFlagA();
    }


    // Get the number of lines of DB file from the data already stored
    public int getNumberOfDBEntries(){
        int entries=0;
        for(List<String> valueList : this.dbData.values()) {
            entries+=valueList.size();
        }
        return entries;
    }


    public void storeDB(String bdCompacted){
        this.dbData = ParserSPDatabase.readSPString(bdCompacted, ConfigHandler.getInstance().getDomain());
    }

    
}
