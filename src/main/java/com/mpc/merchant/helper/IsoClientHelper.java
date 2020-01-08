package com.mpc.merchant.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;

public class IsoClientHelper {
    private Logger log = LogManager.getLogger(getClass());
    private QMUX qmux = null;
    private PropertiesHelper applicationProperties = new PropertiesHelper("application.properties");

    public IsoClientHelper() {
        try{
            qmux = (QMUX) NameRegistrar.get("mux.jposMux");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public ISOMsg sendRequest(ISOMsg isoMsg){
        ISOMsg isoMsgResponse = null;
        try{
            log.info("Vlink Request: "+ new String(isoMsg.pack()));
            isoMsgResponse = qmux.request(isoMsg, new Long(applicationProperties.getPropertis("vlink.timeout")) );
            log.info("Vlink Response: "+ new String(isoMsgResponse.pack()));
        }catch (Exception e){
            e.printStackTrace();
        }

        return isoMsgResponse;
    }
}
