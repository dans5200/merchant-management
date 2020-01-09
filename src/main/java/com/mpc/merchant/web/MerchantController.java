package com.mpc.merchant.web;

import com.mpc.merchant.helper.ConnectionHelper;
import com.mpc.merchant.helper.DateFormaterHelper;
import com.mpc.merchant.helper.IsoClientHelper;
import com.mpc.merchant.helper.StringHelper;
import com.mpc.merchant.model.Merchant;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.model.TrxLog;
import com.mpc.merchant.service.MerchantService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.jpos.iso.ISODate;
import org.jpos.iso.ISOMsg;
import org.jpos.q2.iso.QMUX;
import org.jpos.util.NameRegistrar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Properties;

@RestController
public class MerchantController {
    @Autowired
    private MerchantService merchantService;

    Logger log = LogManager.getLogger(getClass());

    @Autowired
    ConnectionHelper connectionHelper;

    @PostMapping(value = "/list-merchant")
    public TransactionResponse listMerchant(){
        return merchantService.listMerchant();
    }

    @PostMapping(value = "/get-merchant/{id}")
    TransactionResponse getMerchantById(@PathVariable("id") Integer idMerchant){
        return merchantService.getMerchantById(idMerchant);
    }

    @PostMapping(value = "/add-merchant")
    public TransactionResponse addMerchant(@RequestBody Merchant merchant){
        return merchantService.addMerchant(merchant);
    }

    @PostMapping(value = "/edit-merchant")
    public TransactionResponse editMerchant(@RequestBody Merchant merchant){
        return merchantService.addMerchant(merchant);
    }

    @PostMapping(value = "/remove-merchant/{id}")
    public TransactionResponse removeMerchant(@PathVariable("id") Integer idMerchant){
        System.out.println(idMerchant);
        return merchantService.removeMerchant(idMerchant);
    }
}
