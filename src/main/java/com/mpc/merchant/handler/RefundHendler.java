package com.mpc.merchant.handler;

import com.mpc.merchant.helper.*;
import com.mpc.merchant.model.Refund;
import com.mpc.merchant.model.TrxLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.math.BigInteger;
import java.sql.ResultSet;

public class RefundHendler{
    private Logger log = LogManager.getLogger(getClass());

    public void processIsomsg(ISOSource source, ISOMsg m){
        ISOMsg isoMsgVlink = (ISOMsg) m.clone();
        ISOMsg isoMsg = (ISOMsg) m.clone();
        try{
            ResultSet dataTransaksi = new ConnectionHelper()
                                        .select("trx_log")
                                        .where("pcode"," like ", "01__91")
                                        .where("terminal_id", isoMsgVlink.getString(41).trim())
                                        .where("invoice",isoMsgVlink.getString(123))
                                        .first();

            if (dataTransaksi.next()){
                BigInteger maxRefund = BigInteger.ZERO;
                BigInteger dataSumRefund = BigInteger.ZERO;
                BigInteger dataAmountDB = dataTransaksi.getBigDecimal("trx_amount").toBigInteger();
                BigInteger dataISO = new BigInteger(isoMsgVlink.getString(4));

                ResultSet dataRefund = new ConnectionHelper()
                        .select("refund")
                        .where("id_trx_log", dataTransaksi.getInt("id"))
                        .get();

                while (dataRefund.next()){
                    dataSumRefund.add( dataRefund.getBigDecimal("trx_refund").toBigInteger() );
                }

                maxRefund = dataAmountDB.subtract(dataSumRefund);

                if (dataISO.compareTo(maxRefund) <= 0){
                    isoMsgVlink.setMTI("0200");
                    isoMsgVlink.set(2,"6274520000000000");
                    isoMsgVlink.set(28, isoMsg.getString(28).replace("C","").replace("D",""));
                    isoMsgVlink.set(41, new StringHelper().padRight( isoMsg.getString(41).trim(), 8 ));
                    isoMsgVlink.unset(57);
                    isoMsgVlink.set(62, new DateFormaterHelper().nowDateGetYear()+"       600100    0");

                    try {
                        new IsoClientHelper().sendRequest(isoMsgVlink);
                        isoMsg.set(39, "00");
                        if (this.sendResponseToIST(source, isoMsg)){
                            this.sendToDB(dataTransaksi.getInt("id"), dataISO);
                            this.sendResponseToIST(source, isoMsg);
                        }
                    }catch (Exception e){
                        isoMsg.set(39, "99");
                        this.sendResponseToIST(source, isoMsg);
                    }
                }else{
                    isoMsg.set(39, "99");
                    this.sendResponseToIST(source, isoMsg);
                }
            }else{
                //data transaksi tidak ditemukan
                isoMsg.set(39, "03");
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
            String data123 = isoMsg.getString(11)+ isoMsg.getString(13);
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

    private void sendToDB(Integer idTrxLog, BigInteger nominalRefund){
        try {
            Refund refund = new Refund(
                    idTrxLog,
                    nominalRefund,
                    new DateFormaterHelper().getNowTimestamp()
            );

            new ConnectionHelper().save(refund);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
