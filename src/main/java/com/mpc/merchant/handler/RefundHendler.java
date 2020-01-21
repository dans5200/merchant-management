package com.mpc.merchant.handler;

import com.mpc.merchant.helper.*;
import com.mpc.merchant.model.Refund;
import com.mpc.merchant.model.TrxLog;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISOSource;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Map;

public class RefundHendler{
    private Logger log = LogManager.getLogger(getClass());

    public void processIsomsg(ISOSource source, ISOMsg m){
        ISOMsg isoMsgVlink = (ISOMsg) m.clone();
        ISOMsg isoMsg = (ISOMsg) m.clone();
        isoMsg.set(39, "99");
        String status = "failed";

        try{
            ResultSet dataTransaksi = new ConnectionHelper()
                                        .table("trx_log")
                                        .where("pcode","like", "%01__91%")
                                        .where("invoice","like", isoMsg.getString(123).substring(0,10)+"%")
                                        .where("retrieval_reference_number", "like", "%"+isoMsg.getString(123).substring(10,20))
                                        .where("response_code","00")
                                        .first();

            log.info("Invoice Number: "+ isoMsg.getString(123));

            if (dataTransaksi.next()) {
                BigInteger dataSumRefund = BigInteger.valueOf(0);
                BigInteger dataAmountDB = dataTransaksi.getBigDecimal("trx_amount").toBigInteger();
                BigInteger dataISO = new BigInteger(isoMsg.getString(4).substring(0,10));

                ResultSet dataRefund = new ConnectionHelper()
                        .table("refund")
                        .where("id_trx_log", dataTransaksi.getInt("id"))
                        .get();

                while (dataRefund.next()){
                    dataSumRefund = dataSumRefund.add( dataRefund.getBigDecimal("trx_refund").toBigInteger() );
                }

                BigInteger maxRefund = dataAmountDB.subtract(dataSumRefund);

                log.info("SumRefund: ["+dataSumRefund+"] DataAountDB: ["+dataAmountDB+"] DataISO: ["+dataISO+"] MaximalRefund: ["+maxRefund+"]");

                if (dataISO.compareTo(maxRefund) <= 0){
                    isoMsgVlink.setMTI("0200");
                    isoMsgVlink.set(2,"6274520000000000");
                    isoMsgVlink.set(18,"6019");
                    if (isoMsg.getString(28) != null) {
                        isoMsgVlink.set(28, dataTransaksi.getString("amount_fee").replace("C", "").replace("D", ""));
                    }
                    isoMsgVlink.set(41, new StringHelper().padRight( isoMsg.getString(41).trim(), 8 ));
                    isoMsgVlink.unset(57);
                    isoMsgVlink.set(62, new DateFormaterHelper().nowDateGetYear()+ new StringHelper().padLeft( isoMsg.getString(33),13 ) + "    0");

                    try {
                        log.info("Send message to Vlink");
                        ISOMsg responseVlink = new IsoClientHelper().sendRequest(isoMsgVlink);
                        isoMsg.set(39, responseVlink.getString(39));
                        this.sendRefundToDB(dataTransaksi.getInt("id"), dataISO);
                        if (this.sendResponseToIST(source, isoMsg)){
                            status = "success";
                        }
                    }catch (Exception e){
                        isoMsg.set(39, "99");
                        this.sendResponseToIST(source, isoMsg);
                        log.error(e);
                        e.printStackTrace();
                    }
                }else{
                    log.info("Amount refund melebihi amount transaksi");
                    isoMsg.set(39, "13");
                    this.sendResponseToIST(source, isoMsg);
                }
            }else{
                log.info("Data transaksi tidak ditemukan");
                isoMsg.set(39, "03");
                this.sendResponseToIST(source, isoMsg);
            }

            this.sendLogToDB(isoMsg, status);
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
            isoMsg.set(39, "99");
            this.sendResponseToIST(source, isoMsg);
        }
    }

    private Boolean sendResponseToIST(ISOSource source, ISOMsg isoMsg){
        Boolean status = false;
        try{
            IsoHelper isoHelper = new IsoHelper();
            String mti = isoMsg.getMTI();

            log.debug("String ISO: "+new String(isoMsg.pack()));
            log.info("Response MTI: "+isoHelper.setMTI(mti));

            isoMsg.setMTI(isoHelper.setMTI(mti));

            source.send(isoMsg);
            status = true;
        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }

        return status;
    }

    private void sendRefundToDB(Integer idTrxLog, BigInteger nominalRefund){
        try {
            Refund refund = new Refund(
                    idTrxLog,
                    nominalRefund,
                    new DateFormaterHelper().getNowTimestamp()
            );
            new ConnectionHelper().save(refund);

        }catch (Exception e){
            log.error(e);
            e.printStackTrace();
        }
    }

    private void sendLogToDB(ISOMsg isoMsg, String status){
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
