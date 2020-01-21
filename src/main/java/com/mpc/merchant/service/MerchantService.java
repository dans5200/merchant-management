package com.mpc.merchant.service;

import com.mpc.merchant.helper.DateFormaterHelper;
import com.mpc.merchant.helper.IsoClientHelper;
import com.mpc.merchant.helper.IsoHelper;
import com.mpc.merchant.helper.StringHelper;
import com.mpc.merchant.model.Merchant;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.repository.MerchanRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MerchantService {
    @Autowired
    private MerchanRepository merchanRepository;
    private Logger log = LogManager.getLogger(getClass());

    public TransactionResponse listMerchant(){
        List<Merchant> merchantList = merchanRepository.findAll();
        return new TransactionResponse(200,"","Success",merchantList);
    }

    public TransactionResponse getMerchantById(Map<String, Object> value){
        Optional<Merchant> merchantResponse = null;
        try{
            if(value.get("id") != null){
                merchantResponse = merchanRepository.findById((Integer) value.get("id"));
            }else{
                return new TransactionResponse(404,"No ID found.","Not Found", new ArrayList<>());
            }
        }catch (Exception e){
            return new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
        }
        return new TransactionResponse(200,"","Success",merchantResponse);
    }

    public TransactionResponse addMerchant(Merchant merchant){
        TransactionResponse transactionResponse = null;
        Merchant responseMerchant = null;

        try {
            String bit43 = new StringHelper().padRight(merchant.getMerchantName().toUpperCase(),25)+
                           new StringHelper().padRight(merchant.getMerchantCity().toUpperCase(), 13) +
                           new StringHelper().padRight(merchant.getCountryCode().toUpperCase(), 2);

            try{
                ISOMsg isoMsg = new ISOMsg();
                isoMsg.setMTI("0200");
                isoMsg.set(2,"6274520000000000");
                isoMsg.set(3, "320091");
                isoMsg.set(7, ISODate.getDateTime(new Date()));
                isoMsg.set(11, String.valueOf(System.currentTimeMillis() % 1000000));
                isoMsg.set(12, ISODate.getTime(new Date()));
                isoMsg.set(13, ISODate.getDate(new Date()));
                isoMsg.set(17, ISODate.getDate(new Date()));
                isoMsg.set(18, merchant.getMerchantType());
                isoMsg.set(32, "93600111");
                isoMsg.set(33,"600100");
                isoMsg.set(37,"000000000013");
                isoMsg.set(41, new StringHelper().padRight(merchant.getMerchantId().substring(0, 8), 8));
                isoMsg.set(42, "000088080000020");
                isoMsg.set(43, bit43 );
                isoMsg.set(49, "360");
                isoMsg.set(62, new DateFormaterHelper().nowDateGetYear()+ new StringHelper().padLeft(isoMsg.getString(33), 13) +"    0");
                isoMsg.set(102, merchant.getRekening());
                isoMsg.set(103, merchant.getMpan());

                ISOMsg responseISOMsg = new IsoClientHelper().sendRequest(isoMsg);

                if (responseISOMsg.getString(39).equals("00")){
                    responseMerchant = merchanRepository.save(merchant);
                }else{
                    return new TransactionResponse(401, "RC "+responseISOMsg.getString(39)+" from Vlink","Unauthorized", new ArrayList<>());
                }
            }catch (Exception e){
                e.printStackTrace();
                return new TransactionResponse(401, e.getMessage(),"Unauthorized", new ArrayList<>());
            }
            transactionResponse = new TransactionResponse(200,"","Success",responseMerchant);
        }catch (Exception e){
            e.printStackTrace();
            return new TransactionResponse(401, e.getMessage(),"Unauthorized", new ArrayList<>());
        }
        return transactionResponse;
    }

    public  TransactionResponse removeMerchant(Map<String, Object> value){
        try {
            if(value.get("id") != null){
                merchanRepository.deleteById((Integer) value.get("id"));
            }else{
                return new TransactionResponse(404,"No ID found.","Not Found", new ArrayList<>());
            }
        }catch (Exception e){
            return new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
        }
        return new TransactionResponse(200, "","Success",new ArrayList<>());
    }
}
