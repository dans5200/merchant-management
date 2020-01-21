package com.mpc.merchant.web;

import com.mpc.merchant.helper.ConnectionHelper;
import com.mpc.merchant.model.Merchant;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.service.MerchantService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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

    @PostMapping(value = "/get-merchant")
    TransactionResponse getMerchantById(@RequestBody Map<String, Object> value){
        return merchantService.getMerchantById(value);
    }

    @PostMapping(value = "/add-merchant")
    public TransactionResponse addMerchant(@RequestBody Merchant merchant){
        return merchantService.addMerchant(merchant);
    }

    @PostMapping(value = "/edit-merchant")
    public TransactionResponse editMerchant(@RequestBody Merchant merchant){
        return merchantService.addMerchant(merchant);
    }

    @PostMapping(value = "/remove-merchant")
    public TransactionResponse removeMerchant(@RequestBody Map<String, Object> value){
        return merchantService.removeMerchant(value);
    }
}
