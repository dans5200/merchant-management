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

    public String timestampToDB(Timestamp timestamp){
        Date date = new Date();
        date.setTime(timestamp.getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return formattedDate;
    }

    public String stringDBToFormat(String date, String format){
        String newString = null;
        try{
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(date));

            simpleDateFormat = new SimpleDateFormat(format);
            newString = simpleDateFormat.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        return  newString;
    }

    public String timestampToDB(){
        Date date = new Date();
        date.setTime(this.getNowTimestamp().getTime());
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);

        return formattedDate;
    }

    public String timeStampToResponse(){
        Date date = new Date();
        date.setTime(this.getNowTimestamp().getTime());
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(date);

        return formattedDate;
    }

    public Timestamp stringToTimestamp(String data){
        Timestamp timestamp = null;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.SSS");
            Date parsedDate = dateFormat.parse(data);
            timestamp = new java.sql.Timestamp(parsedDate.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }

        return  timestamp;
    }

    public String nowDateGetYear(){
        Date date = new Date();
        date.setTime(this.getNowTimestamp().getTime());
        String formattedDate = new SimpleDateFormat("yyyy").format(date);
        return formattedDate;
    }

    public String increaseDate(String nowDate, Integer increaseValue){
        String newDate = null;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(simpleDateFormat.parse(nowDate));
            calendar.add(Calendar.DATE, increaseValue);
            newDate = simpleDateFormat.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return newDate;
    }

    public String increaseDate(Integer increaseValue){
        String newDate = null;
        try {
            Date date = new Date();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.DATE, increaseValue);
            newDate = simpleDateFormat.format(calendar.getTime());
        }catch (Exception e){
            e.printStackTrace();
        }
        return newDate;
    }
}
