package com.mpc.merchant.web;


import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.service.TrxService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TrxController {
    private Logger log = LogManager.getLogger(getClass());
    @Autowired
    TrxService trxService;

    @PostMapping(value = "/list-trx")
    public TransactionResponse getAllListTrx(){

        return trxService.getAllTrx();
    }

    @PostMapping(value = "/list-trx/page/{page}")
    public TransactionResponse getAllListTrx(@PathVariable String page){

        return trxService.getAllTrx(page);
    }
}
