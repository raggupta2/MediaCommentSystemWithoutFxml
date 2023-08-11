package sample.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DateTimeUtil {
    /***
     *
     * @param dtString : "Wed Aug 09 09:40:19 PDT 2023"
     * @param inFormatString : "ccc MMM dd HH:mm:ss z yyyy"
     * @param outFormatString : "yyyy:MM:dd HH:mm:ss"
     * @return formatted datetime string : 2023:08:09 09:40:19
     */
    public String getDateTimeConvertedFormat(String dtString, String inFormatString, String outFormatString) {
        try {

            LocalDateTime ld = LocalDateTime.parse(dtString, DateTimeFormatter.ofPattern(inFormatString, Locale.US));
      //      System.out.println(ld);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(outFormatString);
//        LocalDateTime parsedDate = LocalDateTime.parse(ld.toString(), formatter);
   //         System.out.println(ld.format(formatter));
            return ld.format(formatter);
        } catch (Exception e) {

        }
        return dtString;
    }
}
