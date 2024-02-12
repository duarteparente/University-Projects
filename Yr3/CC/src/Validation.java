import java.io.File;
import java.io.IOException;

public class Validation {

    
    /** 
     * @return True if the string given is a
     */
    public static boolean isNumeric(String s){
        try {
            Integer.parseInt(s);
        }
        catch (NumberFormatException e){
            return false;
        }
        return true;
    }

    
    /** 
     * First level validation of Server arguments
     * @return Valid if the correct number of arguments was given
     */
    public static boolean checkClientArguments(String[] args){
        return (args.length == 3);
    }

    
    /** 
     * First level validation of Server arguments
     * @return Valid if Configurations file exists, server port and timeout are numeric numbers, server mode is one of the two possibilities allowed
     */
    public static boolean checkServerArguments(String[] args){
        if (args.length != 4) return false;
        File configFile = new File(args[0]);
        return (configFile.exists() && isNumeric(args[1]) && isNumeric(args[2]) && (args[3].equals("debug") || args[3].equals("shy")));
    }


    /** 
     * Abort component execution after error detection
     */
    public static void abortExecution(Integer validationCode, String mode, TimeStamp timeStamp) throws IOException{
        String description = "";
        switch(validationCode){
            case 1: description = "error1"; break;
            case 2: description = "error2"; break;
            default: description = "error"; break;
        }
        LogHandler.getInstance().internalFailure(timeStamp, new TimeStamp(),description, mode);
        System.exit(1);
    }
    
}
