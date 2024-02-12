import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


/**
* Class used to hold the module that represents any log related feature.
* @author Duarte Parente
* @since 07/11/2022
* Last change: 02/12/2022
*/
public class LogHandler {

    private static LogHandler logInstance;

    private File log;
    private TimeStamp logCreation;
    private File allLog;


    /** 
     * LogHandler class constructor
     */
    private LogHandler() throws IOException{
        this.log = new File(ConfigHandler.getInstance().getLogFile());
        if (!this.log.exists()) {
            FileWriter fileWriter = new FileWriter(this.log, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            this.logCreation = new TimeStamp();
            bufferedWriter.write("# Log file\n");
            bufferedWriter.close();
        }
        String allLogFilepath = ConfigHandler.getInstance().getAllLogFile();
        this.allLog = new File(allLogFilepath);
        if (!this.allLog.exists()){
            FileWriter fileWriter = new FileWriter(this.allLog, true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("# Log file\n" + new TimeStamp() + " EV 127.0.0.1 log-file-created" + allLogFilepath + "\n");
            bufferedWriter.close();
        }
    }


    /** 
     * @return An instance of this class (creating it if there isn't one)
     */
    public static LogHandler getInstance() throws IOException{
        if (logInstance == null){
            logInstance = new LogHandler();
        }
        return logInstance; 
    }


    /** 
     * Register logs related to server activation, including configuration
     */
    public synchronized void serverInitialization(TimeStamp serverTimeStamp, TimeStamp configParsing, String[] args) throws IOException{
        this.componentInitialization(serverTimeStamp, args);
        this.internalActivity(configParsing, "conf-file-read", args[0], args[3]);
        if (this.logCreation != null) {
            this.internalActivity(this.logCreation, "log-file-created", ConfigHandler.getInstance().getLogFile(), args[3]);
        }
    }


    /** 
     * Register logs related to server activation, including configuration
     */
    public synchronized void componentInitialization(TimeStamp timeStamp, String[] args) throws IOException{
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(timeStamp + " ST 127.0.0.1 " + args[1] + " " + args[2] + " " + args[3] + "\n");
        if(args[3].equals("debug")){
            System.out.println(timeStamp + " ST 127.0.0.1 " + args[1] + " " + args[2] + " " + args[3]);
        }
        bufferedWriter.close();
    }


    /** 
     * Register logs related to server internal activity, entry type is 'EV'
     */
    public synchronized void internalActivity(TimeStamp timeStamp, String description, String filepath, String mode) throws IOException{
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(timeStamp + " EV 127.0.0.1 " + description + " " + filepath + "\n");
        if (mode.equals("debug")){
            System.out.println(timeStamp + " EV 127.0.0.1 " + description + " " + filepath);
        }
        bufferedWriter.close();
    }

    
    /** 
     * Register logs related to an internal failure in server execution leading to component shut down
     */
    public synchronized void internalFailure(TimeStamp failure, TimeStamp abortion, String description, String mode) throws IOException{
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(failure + " FL 127.0.0.1 " + description + "\n");
        bufferedWriter.write(abortion + " SP 127.0.0.1 " + description + "\n");
        if(mode.equals("debug")){
            System.out.println(failure + " FL 127.0.0.1 " + description);
            System.out.println(abortion + " SP 127.0.0.1 " + description);
        } 
        bufferedWriter.close();
    }


    /** 
     * Register logs related to receiving and sending queries, handle types 'QR/QE' and 'RP/RR'
     */
    public synchronized void queryActivity(String flag, TimeStamp timeStamp, String ip, QueryHandler query, String mode) throws IOException{
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(timeStamp + " " + flag + " " + ip + " " + query.toString() + "\n");
        if(mode.equals("debug")){
            System.out.println(timeStamp + " " + flag + " " + ip + " " + query.toString());
        }
        bufferedWriter.close();
    }


    /** 
     * Register logs related to successful completion of a Zone Transfer
     */
    public synchronized void zoneTransfer(TimeStamp timeStamp, String ip, long time, Integer byteCounter, Integer serverType, String mode) throws IOException{
        String s = "SP";
        if (serverType == 1) s = "SS";
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(timeStamp + " ZT " + ip + " " + s + " " + time + "ms " + byteCounter + "B\n");
        bufferedWriter.close();
        if(mode.equals("debug")) System.out.println(timeStamp + " ZT " + ip + " " + s + " " + time + "ms " + byteCounter + "B");
    }


    /** 
     * Register logs related to error in a Zone Transfer
     * @throws IOException
     */
    public synchronized void errorZoneTransfer(TimeStamp timeStamp, String ip, Integer serverType, String mode) throws IOException{
        String s = "SP";
        if (serverType == 1) s = "SS";
        FileWriter fileWriter = new FileWriter(this.log, true);
        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        bufferedWriter.write(timeStamp + " EZ " + ip + " " + s + "\n");
        bufferedWriter.close();
        if(mode.equals("debug")) System.out.println(timeStamp + " EZ " + ip + " " + s);
    }
}
