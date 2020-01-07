package com.mpc.merchant.helper;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class DateFormaterHelper {
    public Timestamp getNowTimestamp(){
        Calendar calendar = Calendar.getInstance();
        java.util.Date date = calendar.getTime();
        Timestamp timestamp = new Timestamp(date.getTime());

        return timestamp;
    }

    public  String timestampToDB(Timestamp timestamp){
        Date date = new Date();
        date.setTime(timestamp.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return formattedDate;
    }

    public String timestampToDB(){
        Date date = new Date();
        date.setTime(this.getNowTimestamp().getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return formattedDate;
    }
}