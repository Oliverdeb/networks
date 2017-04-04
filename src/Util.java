import java.sql.Time;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**
 * Created by dbroli001 on 2017/03/29.
 */
public class Util {

    public static String time_now(){

        ZoneId zoneId = ZoneId.of("Africa/Harare");
        ZonedDateTime time = ZonedDateTime.now(zoneId);

        return "[" + time.getHour()+ ":" + String.format("%02d", time.getMinute()) + "]";
    }

    public static String parse_message(String input, String user){
        return input.substring(input.indexOf(user)+user.length()+1);
    }

    public static String parse_location(String input){
        return input.substring(input.indexOf("file ") + 5);
    }


}
