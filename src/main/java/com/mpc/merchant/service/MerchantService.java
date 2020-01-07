package com.mpc.merchant.service;

import com.mpc.merchant.model.Merchant;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.repository.MerchanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MerchantService {
    @Autowired
    MerchanRepository merchanRepository;

    public TransactionResponse listMerchant(){
        List<Merchant> merchantList = merchanRepository.findAll();
        return new TransactionResponse(200,"","Success",merchantList);
    }

    public TransactionResponse getMerchantById(Integer idMerchant){
        Optional<Merchant> merchantResponse = null;
        try{
            merchantResponse = merchanRepository.findById(idMerchant);
        }catch (Exception e){
            return new TransactionResponse(400,e.getMessage(),"Bad Request", new Object());
        }
        return new TransactionResponse(200,"","Success",merchantResponse);
    }

    public TransactionResponse addMerchant(Merchant merchant){
        Merchant responseMerchant = merchanRepository.save(merchant);
        return new TransactionResponse(200,"","Success",responseMerchant);
    }

    public  TransactionResponse removeMerchant(Integer idMerchant){
        try {
            merchanRepository.deleteById(idMerchant);
        }catch (Exception e){
            return new TransactionResponse(400,e.getMessage(),"Bad Request", new Object());
        }
        return new TransactionResponse(200, "","Success",new ArrayList<>());
    }
}
