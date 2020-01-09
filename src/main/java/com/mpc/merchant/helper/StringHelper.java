package com.mpc.merchant.helper;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.lang.*;

public class StringHelper {
    public String strConvertUC(String str){
        Integer index = 0;
        do {
            index = str.indexOf("_");
            String tmp = str.charAt(index+1)+"";
            str = str.replace("_"+str.charAt(index+1), tmp.toUpperCase());
        } while (index >= 0);

        return str;
    }

    public String strConvertCU(String str){
        Integer countString = str.length();
        String _str = new String();

        for(Integer i = 0; i < countString; i++){
            if (Character.isUpperCase(str.charAt(i)) && i != 0) {
                String tmp = str.charAt(i) + "";
                _str += "_" + tmp.toLowerCase();
            }else if (Character.isUpperCase(str.charAt(i)) && i == 0){
                String tmp = str.charAt(i) + "";
                _str += tmp.toLowerCase();
            }else {
                _str += str.charAt(i);
            }
        }

        return _str;
    }

    public String padLeft(String value, Integer length){
        return String.format("%1$"+length+"s", value);
    }

    public String padRight(String value, Integer length){
        return  String.format("%1$-"+length+"s", value);
    }
}
