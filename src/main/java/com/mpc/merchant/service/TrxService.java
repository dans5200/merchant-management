package com.mpc.merchant.service;

import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.model.TrxLog;
import com.mpc.merchant.repository.TrxRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrxService {
    @Autowired
    TrxRepository trxRepository;

    private Logger log = LogManager.getLogger(getClass());

    public TransactionResponse getAllTrx(){
        TransactionResponse transactionResponse = null;
        try{
            List<TrxLog> trxLogs = trxRepository.findAll();
            transactionResponse = new TransactionResponse(200,"","success",trxLogs);
        }catch (Exception e){
            e.printStackTrace();
            transactionResponse = new TransactionResponse(400,e.getMessage(),"Failed",new Object());
        }

        return transactionResponse;
    }
}
