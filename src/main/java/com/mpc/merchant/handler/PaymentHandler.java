package com.mpc.merchant.handler;

import com.mpc.merchant.helper.*;
import com.mpc.merchant.model.TrxLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class PaymentHandler {
    private Logger log = LogManager.getLogger(getClass());
    private String data123 = null;

    public void processIsomsg(ISOSource source, ISOMsg m){
        IsoHelper isoHelper = new IsoHelper();
        ISOMsg isoMsg = (ISOMsg) m.clone();
        ISOMsg isoMsgVlink = (ISOMsg) m.clone();
        isoMsg.set(39, "99");
        String status = "failed";
        String rekeningMerchant = null;
        String branch = null;
        Random random = new Random();

        try{
            Integer countMerchant = 0;
            ResultSet resultSetMerchant = new ConnectionHelper()
                                          .table("merchant")
                                          .where("mpan",isoMsg.getString(103))
                                          .where("merchant_id", isoMsg.getString(42).trim())
                                          .first();

            while (resultSetMerchant.next()){
                rekeningMerchant = resultSetMerchant.getString("rekening");
                branch = resultSetMerchant.getString("branch");
                countMerchant++;
            }

            if (countMerchant > 0){
                try {
                    //send to vlink
                    isoMsgVlink.setMTI("0200");
                    isoMsgVlink.set(2,"6274520000000000");
                    isoMsgVlink.set(18,"6019");

                    if (isoMsg.getString(28) != null) {
//                        isoMsgVlink.set(28, isoMsg.getString(28).replace("C", "").replace("D", ""));
                        isoMsgVlink.unset(28);
                    }

                    isoMsgVlink.unset(38);
                    isoMsgVlink.set(41, branch);
                    isoMsgVlink.unset(48);
                    isoMsgVlink.unset(57);
                    isoMsgVlink.set(62, new DateFormaterHelper().nowDateGetYear()+ new StringHelper().padLeft( isoMsg.getString(33),13 ) + "    0");
                    isoMsgVlink.set(103, rekeningMerchant);

                    try{
                        log.info("Send message to Vlink");
                        ISOMsg responseVlink = new IsoClientHelper().sendRequest(isoMsgVlink);
                        isoMsg.set(39, responseVlink.getString(39));
                        data123 = isoMsg.getString(11)+ String.format("%04d", random.nextInt(10000));
                        if (this.sendResponseToIST(source, isoMsg)){
                            status = "success";
                        }
                    }catch (Exception e){
                        log.error(e);
                        e.printStackTrace();
                        isoMsg.set(39,"99");
                        this.sendResponseToIST(source, isoMsg);
                    }
                }catch (Exception e){
                    log.error(e);
                    e.printStackTrace();
                    isoMsg.set(39,"99");
                    this.sendResponseToIST(source, isoMsg);
                }
            }else{
                log.info("Merchant not found");
                isoMsg.set(39,"14");
                this.sendResponseToIST(source, isoMsg);
            }

            log.info("Save to DB");
            this.sendToDB(isoMsg, status);
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
            this.sendResponseToIST(source, isoMsg);
        }
    }

    private Boolean sendResponseToIST(ISOSource source, ISOMsg isoMsg){
        Boolean status = false;
        try{
            Random random = new Random();

            IsoHelper isoHelper = new IsoHelper();
            String mti = isoMsg.getMTI();

            log.debug("String ISO: "+new String(isoMsg.pack()));
            log.info("Response MTI: "+isoHelper.setMTI(mti));

            isoMsg.setMTI(isoHelper.setMTI(mti));

            if (isoMsg.getString(39).equals("00")){
                isoMsg.set(123,data123+data123);
            }

            source.send(isoMsg);
            status = true;
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }

        return status;
    }

    private void sendToDB(ISOMsg isoMsg, String status){
        try {
            isoMsg.getString(13);

           TrxLog dataTrxLog = new TrxLog(
                    isoMsg.getString(2),
                    isoMsg.getString(3),
                    new BigInteger(isoMsg.getString(4).substring(0,10)),
                    isoMsg.getString(7),
                    isoMsg.getString(11),
                    isoMsg.getString(12),
                    isoMsg.getString(13),
                    isoMsg.getString(17),
                    isoMsg.getString(18),
                    isoMsg.getString(22),
                    isoMsg.getString(28),
                    isoMsg.getString(32),
                    isoMsg.getString(33),
                    isoMsg.getString(37),
                    isoMsg.getString(38),
                    isoMsg.getString(41),
                    isoMsg.getString(42),
                    isoMsg.getString(43),
                    isoMsg.getString(48),
                    isoMsg.getString(49),
                    isoMsg.getString(57),
                    isoMsg.getString(102),
                    isoMsg.getString(103),
                    status,
                   data123+data123,
                    isoMsg.getString(39),
                    new DateFormaterHelper().getNowTimestamp()
            );

           new ConnectionHelper().save(dataTrxLog);
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }
    }
}
