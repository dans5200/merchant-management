package com.mpc.merchant.listener;

import com.mpc.merchant.handler.PaymentHandler;
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
            log.info("pcode: "+pcode);

            if (pcode.substring(0,2).equals("01") && pcode.substring(4).equals("91")){
                PaymentHandler paymentHandler = new PaymentHandler();
                paymentHandler.processIsomsg(source, m);
            }else if (pcode.substring(0,2).equals("33") && pcode.substring(4).equals("91")){
                StatusCheckHandler statusCheckHandler = new StatusCheckHandler();
                statusCheckHandler.processIsomsg(source, m);
            }else if (pcode.substring(0,2).equals("01") && pcode.substring(4).equals("92")){

            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return true;
    }
}
