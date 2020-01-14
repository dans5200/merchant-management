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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {
    @Autowired
    private MerchanRepository merchanRepository;
    private Logger log = LogManager.getLogger(getClass());

    public TransactionResponse listMerchant(){
        List<Merchant> merchantList = merchanRepository.findAll();
        return new TransactionResponse(200,"","Success",merchantList);
    }

    public TransactionResponse getMerchantById(Integer idMerchant){
        Optional<Merchant> merchantResponse = null;
        try{
            merchantResponse = merchanRepository.findById(idMerchant);
        }catch (Exception e){
            return new TransactionResponse(400,e.getMessage(),"Bad Request", new ArrayList<>());
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
                isoMsg.set(18, "5812");
                isoMsg.set(32, "93600111");
                isoMsg.set(33,"600100");
                isoMsg.set(37,"000000000013");
                isoMsg.set(41, new StringHelper().padRight(merchant.getMerchantId(), 8));
                isoMsg.set(42, "000088080000020");
                isoMsg.set(43, bit43 );
                isoMsg.set(49, "360");
                isoMsg.set(62, new DateFormaterHelper().nowDateGetYear()+"       600100    0");
                isoMsg.set(102, new StringHelper().padLeft(merchant.getRekening(),19));
                isoMsg.set(103, new StringHelper().padLeft(merchant.getMpan(), 19));

                ISOMsg responseISOMsg = new IsoClientHelper().sendRequest(isoMsg);

                if (responseISOMsg == null){
                    return new TransactionResponse(400, "RTO","Could not get any response", new ArrayList<>());
                }else{
                    responseMerchant = merchanRepository.save(merchant);
                }
            }catch (Exception e){
                e.printStackTrace();
                return new TransactionResponse(400, e.getMessage(),"Bad Request", new ArrayList<>());
            }
            transactionResponse = new TransactionResponse(200,"","Success",responseMerchant);
        }catch (Exception e){
            e.printStackTrace();
            return new TransactionResponse(400, e.getMessage(),"Bad Request", new ArrayList<>());
        }
        return transactionResponse;
    }

    public  TransactionResponse removeMerchant(Integer idMerchant){
        try {
            merchanRepository.deleteById(idMerchant);
        }catch (Exception e){
            return new TransactionResponse(400,e.getMessage(),"Bad Request", new ArrayList<>());
        }
        return new TransactionResponse(200, "","Success",new ArrayList<>());
    }
}
