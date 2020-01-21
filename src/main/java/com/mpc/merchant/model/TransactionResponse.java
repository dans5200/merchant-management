package com.mpc.merchant.model;

import com.mpc.merchant.helper.DateFormaterHelper;
import com.mpc.merchant.helper.StringHelper;
import lombok.Data;

import javax.persistence.Entity;

public class TransactionResponse {
    private String timestamp;
    private Integer status;
    private String error;
    private String message;
    private Object dataDetail;

    public TransactionResponse() {
    }

    public TransactionResponse(Integer status, String error, String message, Object dataDetail) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.dataDetail = dataDetail;
        this.timestamp = new DateFormaterHelper().timeStampToResponse();
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

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "TransactionResponse{" +
                "timestamp='" + timestamp + '\'' +
                ", status=" + status +
                ", error='" + error + '\'' +
                ", message='" + message + '\'' +
                ", dataDetail=" + dataDetail +
                '}';
    }
}
