package com.mpc.merchant.helper;

public class IsoHelper {
    public String setMTI(String mti){
        String _mti = "210";
        if (mti.equals("200")){
            _mti = "210";
        }else if (mti.equals("210")){
            _mti = "220";
        }else if (mti.equals("400")){
            _mti = "410";
        }else if (mti.equals("800")){
            _mti = "810";
        }
        return _mti;
    }
}
