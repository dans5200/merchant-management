package com.mpc.merchant.web;

import com.mpc.merchant.helper.IsoClientHelper;
import com.mpc.merchant.helper.QRReaderHelper;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.service.TrxService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISOMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class TrxController {
    private Logger log = LogManager.getLogger(getClass());
    @Autowired
    TrxService trxService;

    @PostMapping(value = "/list-trx")
    public TransactionResponse getListTrx(){
        log.info("========Get List Trx=========");
        return trxService.getAllTrx();
    }

    @PostMapping(value = "/list-trx/page/{page}")
    public TransactionResponse getListTrx(@PathVariable String page){
        log.info("========Get List Trx page "+page+"=========");
        return trxService.getAllTrx(page);
    }

    @PostMapping(value = "/list-trx/find")
    public TransactionResponse findListTrx(@RequestBody Map<String, Object> findValue){
        log.info("========Find List Trx by value =========");
        log.info(findValue);
        TransactionResponse transactionResponse = trxService.findListTrx(findValue);
        log.info(transactionResponse);
        return transactionResponse;
    }

    @PostMapping(value = "/echo-vlink")
    public void echoVlink(){
        log.info("========Echo Vlink =========");
        try{
            log.info("Echo vlink start.");
            ISOMsg isoMsg = new ISOMsg();
            isoMsg.setMTI("0800");
            isoMsg.set(70,"301");
            ISOMsg response = new IsoClientHelper().sendRequest(isoMsg);
            log.info("Echo ended.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @PostMapping(value = "/read-token")
    public Map<String, String> readQR(@RequestBody Map<String, Object> param){
        log.info(param);
        return new QRReaderHelper(param.get("qrData").toString()).getResult();
    }
}
