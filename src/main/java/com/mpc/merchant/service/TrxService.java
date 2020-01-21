package com.mpc.merchant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mpc.merchant.helper.ConnectionHelper;
import com.mpc.merchant.helper.DateFormaterHelper;
import com.mpc.merchant.helper.PagingHelper;
import com.mpc.merchant.helper.PropertiesHelper;
import com.mpc.merchant.model.TransactionResponse;
import com.mpc.merchant.model.TrxLog;
import com.mpc.merchant.repository.TrxRepository;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TrxService {
    @Autowired
    private TrxRepository trxRepository;
    private PropertiesHelper applicationProperties = new PropertiesHelper("application.properties");
    private Logger log = LogManager.getLogger(getClass());

    public TransactionResponse getAllTrx(){
        TransactionResponse transactionResponse = null;
        String maxPageNumber = applicationProperties.getPropertis("number.of.show.data");
        try{
            Map<String, Object> response = new HashMap<>();
            List<TrxLog> trxLogs = new ArrayList<>();
            Integer count = new ConnectionHelper().table("trx_log").count();
            ResultSet paginate = new ConnectionHelper()
                                  .table("trx_log")
                                  .paginate(maxPageNumber);

            while (paginate.next()){
                trxLogs.add(
                        new TrxLog(
                                //new Integer(paginate.getString("id")),
                                paginate.getInt("id"),
                                paginate.getString("pan"),
                                paginate.getString("pcode"),
                                //new BigInteger(paginate.getString("trx_amount")),
                                paginate.getBigDecimal("trx_amount").toBigInteger(),
                                paginate.getString("trx_date_time"),
                                paginate.getString("trace"),
                                paginate.getString("local_trx_time"),
                                paginate.getString("local_trx_date"),
                                paginate.getString("capture_date"),
                                paginate.getString("merchant_type"),
                                paginate.getString("posem"),
                                paginate.getString("amount_fee"),
                                paginate.getString("bit32"),
                                paginate.getString("qris_id"),
                                paginate.getString("retrieval_reference_number"),
                                paginate.getString("auth_id_response"),
                                paginate.getString("terminal_id"),
                                paginate.getString("card_acceptor_id"),
                                paginate.getString("terminal_location"),
                                paginate.getString("bit48"),
                                paginate.getString("curency_code"),
                                paginate.getString("bit57"),
                                paginate.getString("from_account_no"),
                                paginate.getString("mpan"),
                                paginate.getString("status"),
                                paginate.getString("invoice"),
                                paginate.getString("response_code"),
                                new DateFormaterHelper().stringToTimestamp(paginate.getString("created_at"))
                        )
                );
            }

            response.put("countData", count);
            response.put("currentPage",1);
            response.put("maximalPage", new PagingHelper().countMaxPage( count, new Integer(maxPageNumber) ));
            response.put("dataList",trxLogs);

            transactionResponse = new TransactionResponse(200,"","Success",response);
        }catch (Exception e){
            e.printStackTrace();
            transactionResponse = new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
        }
        return transactionResponse;
    }

    public TransactionResponse getAllTrx(String page){
        TransactionResponse transactionResponse = null;
        String maxPageNumber = applicationProperties.getPropertis("number.of.show.data");
        try{
            Map<String, Object> response = new HashMap<>();
            List<TrxLog> trxLogs = new ArrayList<>();
            Integer count = new ConnectionHelper().table("trx_log").count();
            ResultSet paginate = new ConnectionHelper()
                    .table("trx_log")
                    .paginate(maxPageNumber, page);

            while (paginate.next()){
                trxLogs.add(
                        new TrxLog(
                                paginate.getInt("id"),
                                paginate.getString("pan"),
                                paginate.getString("pcode"),
                                paginate.getBigDecimal("trx_amount").toBigInteger(),
                                paginate.getString("trx_date_time"),
                                paginate.getString("trace"),
                                paginate.getString("local_trx_time"),
                                paginate.getString("local_trx_date"),
                                paginate.getString("capture_date"),
                                paginate.getString("merchant_type"),
                                paginate.getString("posem"),
                                paginate.getString("amount_fee"),
                                paginate.getString("bit32"),
                                paginate.getString("qris_id"),
                                paginate.getString("retrieval_reference_number"),
                                paginate.getString("auth_id_response"),
                                paginate.getString("terminal_id"),
                                paginate.getString("card_acceptor_id"),
                                paginate.getString("terminal_location"),
                                paginate.getString("bit48"),
                                paginate.getString("curency_code"),
                                paginate.getString("bit57"),
                                paginate.getString("from_account_no"),
                                paginate.getString("mpan"),
                                paginate.getString("status"),
                                paginate.getString("invoice"),
                                paginate.getString("response_code"),
                                new DateFormaterHelper().stringToTimestamp(paginate.getString("created_at"))
                        )
                );
            }

            response.put("countData", count);
            response.put("currentPage", new Integer(page));
            response.put("maximalPage", new PagingHelper().countMaxPage( count, new Integer(maxPageNumber) ));
            response.put("dataList",trxLogs);

            transactionResponse = new TransactionResponse(200,"","Success",response);
        }catch (Exception e){
            e.printStackTrace();
            transactionResponse = new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
        }
        return transactionResponse;
    }

    public TransactionResponse findListTrx(Map<String, Object> searchValue){
        TransactionResponse transactionResponse = null;
        Integer count = 0;
        String invoice = "";
        String refnum = "";
        try {
            TrxLog trxLog = null;
            try {
                count = new ConnectionHelper()
                        .table("merchant")
                        .where("rekening",searchValue.get("mobileId").toString())
                        .count();
            }catch (NullPointerException e){
                return new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
            }

            if (count == 0){
                return new TransactionResponse(401,"Mobile ID not found.","Unauthorized", new ArrayList<>());
            }

            try{
                searchValue.remove("mobile_id");
                searchValue.remove("mobileId");
                invoice = searchValue.get("invoice").toString().substring(0,10);
                refnum = searchValue.get("invoice").toString().substring(10,20);
                searchValue.put("invoice", invoice+invoice);
                searchValue.put("retrievalReferenceNumber", refnum);
            }catch (Exception e){

            }

            ResultSet paginate = new ConnectionHelper()
                    .table("trx_log")
                    .findBy(searchValue)
                    .first();

            if (paginate.next()){
                trxLog = new TrxLog(
                    paginate.getInt("id"),
                    paginate.getString("pan"),
                    paginate.getString("pcode"),
                    paginate.getBigDecimal("trx_amount").toBigInteger(),
                    paginate.getString("trx_date_time"),
                    paginate.getString("trace"),
                    paginate.getString("local_trx_time"),
                    paginate.getString("local_trx_date"),
                    paginate.getString("capture_date"),
                    paginate.getString("merchant_type"),
                    paginate.getString("posem"),
                    paginate.getString("amount_fee"),
                    paginate.getString("bit32"),
                    paginate.getString("qris_id"),
                    paginate.getString("retrieval_reference_number"),
                    paginate.getString("auth_id_response"),
                    paginate.getString("terminal_id"),
                    paginate.getString("card_acceptor_id"),
                    paginate.getString("terminal_location"),
                    paginate.getString("bit48"),
                    paginate.getString("curency_code"),
                    paginate.getString("bit57"),
                    paginate.getString("from_account_no"),
                    paginate.getString("mpan"),
                    paginate.getString("status"),
                    paginate.getString("invoice"),
                    paginate.getString("response_code"),
                    new DateFormaterHelper().stringToTimestamp(paginate.getString("created_at"))
                );
            }

            transactionResponse = new TransactionResponse(200,"","Success",trxLog);
        }catch (Exception e){
            e.printStackTrace();
            transactionResponse = new TransactionResponse(401,e.getMessage(),"Unauthorized", new ArrayList<>());
        }

        return transactionResponse;
    }
}
