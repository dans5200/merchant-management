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

public class RefundHendler{
    private Logger log = LogManager.getLogger(getClass());
    private ConnectionHelper connectionHelper = new ConnectionHelper();

    public void processIsomsg(ISOSource source, ISOMsg m){
        ISOMsg isoMsg = null;
        IsoHelper isoHelper = new IsoHelper();
        TrxLog dataTrxLog = null;
        isoMsg = (ISOMsg) m.clone();
        ResultSet resultSet = null;
        String status = "success";

        try{
            String data123 = isoMsg.getString(11)+ isoMsg.getString(13);
            String mti = isoMsg.getMTI();

            log.debug("String ISO: "+new String(m.pack()));
            log.info("Response MTI: "+isoHelper.setMTI(mti));

            isoMsg.setMTI(isoHelper.setMTI(mti));
            isoMsg.set(39,"00");
            isoMsg.set(123,data123+data123);

            dataTrxLog = new TrxLog(
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
                    status,
                    isoMsg.getString(123),
                    new DateFormaterHelper().getNowTimestamp()
            );

            source.send(isoMsg);
        }catch (Exception e){
            e.printStackTrace();
            isoMsg.set(39, "99");
            status = "failed";
        }

        try {
            log.info(dataTrxLog);
            connectionHelper.save(dataTrxLog);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
