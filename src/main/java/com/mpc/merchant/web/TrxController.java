package com.mpc.merchant.web;


import com.mpc.merchant.model.TransactionResponse;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

public class TrxController {
    private Logger log = LogManager.getLogger(getClass());

    @PostMapping(value = "list-trx")
    public TransactionResponse getAllListTrx(){
        TransactionResponse transactionResponse = new TransactionResponse();

        return transactionResponse;
    }
}
