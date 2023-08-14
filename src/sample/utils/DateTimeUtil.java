package sample.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DateTimeUtil {
    /***
     * A method to convert any datetime string to desired format.
     * @param dt : "Wed Aug 09 09:40:19 PDT 2023"
     * @param outFormat : "yyyy:MM:dd HH:mm:ss"
     * @return  formatted datetime string : 2023:08:09 09:40:19
     */
    public String convertDateTimeFormat(String dt, String outFormat){
        if (dt.isEmpty()){
            return "";
        }
        SimpleDateFormat outPattern = new SimpleDateFormat(outFormat);
        List<SimpleDateFormat> inputPatterns = new ArrayList<SimpleDateFormat>();
        inputPatterns.add(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSSSS"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ssZ"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm.ss"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ"));
        inputPatterns.add(new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy"));
        inputPatterns.add(new SimpleDateFormat("d MMM yyyy HH:mm:ss"));
        inputPatterns.add(new SimpleDateFormat("yyyyMMdd"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-ddZ"));
        inputPatterns.add(new SimpleDateFormat("HH:mm:ss"));
        inputPatterns.add(new SimpleDateFormat("HH:mm:ssZ"));
        inputPatterns.add(new SimpleDateFormat("d MMM yyyy HH:mm"));
        inputPatterns.add(new SimpleDateFormat("yyyy.MM.dd G HH:mm:ss z"));
        inputPatterns.add(new SimpleDateFormat("EEE, MMM d, ''yy"));
        inputPatterns.add(new SimpleDateFormat("h:mm a"));
        inputPatterns.add(new SimpleDateFormat("hh 'o''clock' a, zzzz"));
        inputPatterns.add(new SimpleDateFormat("K:mm a, z"));
        inputPatterns.add(new SimpleDateFormat("yyyyy.MMMMM.dd GGG hh:mm aaa"));
        inputPatterns.add(new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z"));
        inputPatterns.add(new SimpleDateFormat("yyMMddHHmmssZ"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ"));
        inputPatterns.add(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX"));
        inputPatterns.add(new SimpleDateFormat("YYYY-'W'ww-u"));
        for (SimpleDateFormat pattern : inputPatterns) {
//            pattern.setLenient(false);
            try {
                // Take a try
                Date ret = new Date(pattern.parse(dt).getTime());
           //     System.out.println("matched pattern:" + pattern.toPattern());
                return outPattern.format(ret);


            } catch (ParseException pe) {
                // Loop on
//                System.out.println("pattern:" + pattern.toPattern() + ", error: " + pe.getMessage());
            }
        }
        System.err.println("No known Date format found: " + dt);
        return "";
    }
}
