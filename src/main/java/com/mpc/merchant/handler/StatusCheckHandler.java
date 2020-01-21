package com.mpc.merchant.handler;

import com.mpc.merchant.helper.ConnectionHelper;
import com.mpc.merchant.helper.DateFormaterHelper;
import com.mpc.merchant.helper.IsoHelper;
import com.mpc.merchant.model.TrxLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.math.BigInteger;
import java.sql.ResultSet;

public class StatusCheckHandler {
    private Logger log = LogManager.getLogger(getClass());

    public void processIsomsg(ISOSource source, ISOMsg m){
        ISOMsg isoMsg = null;
        IsoHelper isoHelper = new IsoHelper();
        isoMsg = (ISOMsg) m.clone();
        ResultSet resultSet = null;
        String status = "failed";
        isoMsg.set(39,"99");

        try {
            String mti = isoMsg.getMTI();

            log.debug("String ISO: "+new String(m.pack()));
            log.info("Response MTI: "+isoHelper.setMTI(mti));

            isoMsg.setMTI(isoHelper.setMTI(mti));
            isoMsg.set(39,"99");

            try{
                resultSet = new ConnectionHelper()
                            .table("trx_log")
                            .where("pcode","like","%01__91%")
                            .where("mpan",isoMsg.getString(103))
                            .where("invoice", isoMsg.getString(123))
                            .where("retrieval_reference_number", isoMsg.getString(37))
                            .first();

                if (resultSet.next() != false){
                    isoMsg.set(123, resultSet.getString("invoice"));
                    isoMsg.set(39, resultSet.getString("response_code"));
                    log.info("Set data invoice: "+resultSet.getString("invoice"));
                }else{
                    //data transaksi tidak ditemukan
                    isoMsg.set(39, "12");
                    status = "failed";
                }
            }catch (Exception e){
                log.error(e);
                e.printStackTrace();
                isoMsg.set(39, "99");
                status = "failed";
            }

            source.send(isoMsg);
            this.sendToDB(isoMsg, status);
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }
    }

    private void sendToDB(ISOMsg isoMsg, String status){
        try{
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
                    isoMsg.getString(123),
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
