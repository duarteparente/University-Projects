import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
* Class used to hold and format a TimeStamp
* @author Duarte Parente
* @since 07/11/2022
* Last change: 15/11/2022
*/
public class TimeStamp {

    private LocalDateTime timeStamp;

    public TimeStamp(){
        timeStamp = LocalDateTime.now();
    }
    
    @Override
    public String toString(){
        return DateTimeFormatter.ofPattern("dd:MM:yyyy.HH:mm:ss:SSS").format(this.timeStamp);
    }
    
}
