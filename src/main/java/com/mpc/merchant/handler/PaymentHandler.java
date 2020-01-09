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

@Component
public class PaymentHandler {
    private Logger log = LogManager.getLogger(getClass());

    public void processIsomsg(ISOSource source, ISOMsg m){
        IsoHelper isoHelper = new IsoHelper();
        ISOMsg isoMsg = (ISOMsg) m.clone();
        ISOMsg isoMsgVlink = (ISOMsg) m.clone();

        try{
            Integer countMerchant = new ConnectionHelper()
                                  .select("merchant")
                                  .where("mpan",isoMsg.getString(103))
                                  .where("merchant_id", isoMsg.getString(41).trim())
                                  .count();

            if (countMerchant > 0){
                try {
                    //send to vlink
                    isoMsgVlink.setMTI("0200");
                    isoMsgVlink.set(2,"6274520000000000");
                    isoMsgVlink.set(28, isoMsg.getString(28).replace("C","").replace("D",""));
                    isoMsgVlink.unset(38);
                    isoMsgVlink.set(41, new StringHelper().padRight( isoMsg.getString(41).trim(), 8 ));
                    isoMsgVlink.unset(48);
                    isoMsgVlink.unset(57);
                    isoMsgVlink.set(62, new DateFormaterHelper().nowDateGetYear()+"       600100    0");

                    try{
                        new IsoClientHelper().sendRequest(isoMsgVlink);
                        isoMsg.set(39, "00");
                        if (this.sendResponseToIST(source, isoMsg)){
                            log.info("Save to DB");
                            this.sendToDB(isoMsg);
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                        isoMsg.set(39,"99");
                        this.sendResponseToIST(source, isoMsg);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    isoMsg.set(39,"99");
                    this.sendResponseToIST(source, isoMsg);
                }
            }else{
                isoMsg.set(39,"14");
                this.sendResponseToIST(source, isoMsg);
            }
        }catch (Exception e){
            e.printStackTrace();
            isoMsg.set(39, "99");
            this.sendResponseToIST(source, isoMsg);
        }
    }

    private Boolean sendResponseToIST(ISOSource source, ISOMsg isoMsg){
        Boolean status = false;
        try{
            String data123 = isoMsg.getString(11)+ ISODate.getDateTime(new Date());
            IsoHelper isoHelper = new IsoHelper();
            String mti = isoMsg.getMTI();

            log.debug("String ISO: "+new String(isoMsg.pack()));
            log.info("Response MTI: "+isoHelper.setMTI(mti));

            isoMsg.setMTI(isoHelper.setMTI(mti));
            isoMsg.set(123,data123);

            source.send(isoMsg);
            status = true;
        }catch (Exception e){
            e.printStackTrace();
        }

        return status;
    }

    private void sendToDB(ISOMsg isoMsg){
        try {
            String data123 = isoMsg.getString(11)+ ISODate.getDateTime(new Date());isoMsg.getString(13);

           TrxLog dataTrxLog = new TrxLog(
                    isoMsg.getString(2),
                    isoMsg.getString(3),
                    new BigInteger(isoMsg.getString(4)),
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
                    "success",
                    data123,
                    new DateFormaterHelper().getNowTimestamp()
            );

           new ConnectionHelper().save(dataTrxLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
