package com.mpc.merchant.model;

import lombok.Data;

import javax.persistence.Entity;

public class TransactionResponse {
    Integer status;
    String error;
    String message;
    Object dataDetail;

    public TransactionResponse() {
    }

    public TransactionResponse(Integer status, String error, String message, Object dataDetail) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.dataDetail = dataDetail;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getDataDetail() {
        return dataDetail;
    }

    public void setDataDetail(Object dataDetail) {
        this.dataDetail = dataDetail;
    }
}
