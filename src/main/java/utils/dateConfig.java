package utils;

import java.util.Date;

public class dateConfig {

    public static String getDateToday(){
        Date date = new Date();
        String month = String.valueOf(date.getMonth()+1);
        String day = String.valueOf(date.getDate());
        String year = String.valueOf(date.getYear()+1900);
        String secound = String.valueOf(date.getSeconds());
        String minutes = String.valueOf(date.getMinutes());
        String hours = String.valueOf(date.getHours());
        return (hours + "." + minutes + "." + secound + "-" + day + "." + month + "." + year);
    }
}
