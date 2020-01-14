package com.mpc.merchant.listener;

import com.mpc.merchant.handler.PaymentHandler;
import com.mpc.merchant.handler.RefundHendler;
import com.mpc.merchant.handler.StatusCheckHandler;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.iso.ISORequestListener;
import org.jpos.iso.ISOSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

public class MerchantListener implements ISORequestListener {
    private Logger log = LogManager.getLogger(getClass());

    @Override
    public boolean process(ISOSource source, ISOMsg m) {
        try {
            String pcode = m.getString(3);
            log.info("MTI: "+m.getMTI());
            log.info("pcode: "+pcode);

            if (pcode != null){
                if (pcode.substring(0,2).equals("01") && pcode.substring(4).equals("91")){
                    log.info("========Payment Credit=========");
                    PaymentHandler paymentHandler = new PaymentHandler();
                    paymentHandler.processIsomsg(source, m);
                }else if (pcode.substring(0,2).equals("33") && pcode.substring(4).equals("91")){
                    log.info("========Check Status=========");
                    StatusCheckHandler statusCheckHandler = new StatusCheckHandler();
                    statusCheckHandler.processIsomsg(source, m);
                }else if (pcode.substring(0,2).equals("01") && pcode.substring(4).equals("92")){
                    log.info("========Refund=========");
                    RefundHendler refundHendler = new RefundHendler();
                    refundHendler.processIsomsg(source, m);
                }
            }else if(m.getMTI().equals("0800")){
                ISOMsg isoMsg = (ISOMsg) m.clone();
                isoMsg.setMTI("0810");
                source.send(isoMsg);
            }

        }catch (Exception e){
            e.printStackTrace();
        }

        return true;
    }
}
