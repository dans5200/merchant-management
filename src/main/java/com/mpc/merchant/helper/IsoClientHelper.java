package com.mpc.merchant.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;

import java.util.Date;

public class IsoClientHelper {
    private Logger log = LogManager.getLogger(getClass());
    private QMUX qmux = null;
    private PropertiesHelper applicationProperties = new PropertiesHelper("application.properties");

    public IsoClientHelper() {
        try{
            qmux = (QMUX) NameRegistrar.get("mux.jposMux");
        }catch (Exception e){
            log.error(e);
        }
    }

    public ISOMsg sendRequest(ISOMsg isoMsg){
        ISOMsg isoMsgResponse = null;
        try{

            isoMsg.set(7, ISODate.getDateTime(new Date()));
            isoMsg.set(12, ISODate.getTime(new Date()));
            isoMsg.set(13, ISODate.getDate(new Date()));
            isoMsg.set(17, ISODate.getDate(new Date()));

            isoMsgResponse = qmux.request(isoMsg, new Long(applicationProperties.getPropertis("vlink.timeout")) );
            try {
                log.info("Response from Vlink: "+new String(isoMsgResponse.pack()));
            }catch (NullPointerException e){
                isoMsgResponse = isoMsg;
                log.debug("Response from VLINK: Time Out");
                isoMsg.set(39,"08");
            }
        }catch (Exception e){
            log.error(e);
        }

        return isoMsgResponse;
    }
}
