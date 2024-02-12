import java.io.IOException;


public class Component {
    /*
     * Server initialization -> log registration is initiated and system files are read 
     */
    public static void serverStartUp(String args[]) throws IOException{

        TimeStamp serverInitialization = new TimeStamp();

        /* Read Configuration File */
        ConfigHandler config = ConfigHandler.getInstance();
        TimeStamp configParsing = config.readConfigFile(args[0]);
        
        /* Check Configuration reading */
        if (config.getvalidationCode() != 0) Validation.abortExecution(config.getvalidationCode(), args[3], configParsing);

        LogHandler.getInstance().serverInitialization(serverInitialization, configParsing, args);

        DataHandler data = DataHandler.getInstance();
        data.setPort(Integer.parseInt(args[1]));
        data.setTimeout(Integer.parseInt(args[2]));
        data.setMode(args[3]);
        data.initializeCache();

        /* Check Server Type */
        if (config.checkServerType() == 0){ /* Server is SP */

            /* Read DB File */
            TimeStamp dbParsing = data.readSPDataFile(config.getSPDatabaseFilePath());

            /* Check DB reading */
            if (data.getDBValidationCode() != 0) Validation.abortExecution(data.getDBValidationCode(), args[3], dbParsing);
            
            LogHandler.getInstance().internalActivity(dbParsing, "db-file-read", config.getSPDatabaseFilePath(), args[3]);

        }

        /* Read ST File */
        if (config.hasST()){
            TimeStamp stParsing = data.readSTConfigFile(config.getSTListFilePath());
            /* Check ST reading */
            if (data.getSTValidationCode() != 0) Validation.abortExecution(data.getSTValidationCode(), args[3], stParsing);
            LogHandler.getInstance().internalActivity(stParsing, "st-file-read", config.getSTListFilePath(), args[3]);
        }

    }


    public static void main(String[] args) throws IOException{

        if (Validation.checkServerArguments(args)){

            /* Server Initialization */
            serverStartUp(args);

            if (ConfigHandler.getInstance().checkServerType() != 2){
                TCPCommunication tcp = new TCPCommunication();
                new Thread(tcp).start();
            }

            //udp communication:
            new UDPCommunication();
            

        }
        else System.out.println("\n » Server not initialized - Wrong Arguments\n");
    }
    
}
