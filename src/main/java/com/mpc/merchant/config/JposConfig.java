package com.mpc.merchant.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jpos.q2.Q2;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class JposConfig {
    Logger log = LogManager.getLogger(getClass());

    @EventListener(ApplicationReadyEvent.class)
    public void startJpos(){
        log.info("JPOS Start");
        Q2 q2 = new Q2();
        q2.start();
    }
}
