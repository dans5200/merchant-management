package com.mpc.merchant.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Timestamp;

@Entity
public class TrxLog{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String pan;
    private String pcode;
    private BigInteger trxAmount;
    private String trxDateTime;
    private String trace;
    private String localTrxTime;
    private String localTrxDate;
    private String captureDate;
    private String merchantType;
    private String posem;
    private String amountFee;
    private String bit32;
    private String qrisId;
    private String retrievalReferenceNumber;
    private String authIdResponse;
    private String terminalId;
    private String cardAcceptorId;
    private String terminalLocation;
    private String bit48;
    private String curencyCode;
    private String bit57;
    private String fromAccountNo;
    private String mpan;
    private String status;
    private String invoice;
    private Timestamp createdAt;

    public TrxLog() {
    }

    public TrxLog(String pan, String pcode, BigInteger trxAmount, String trxDateTime, String trace, String localTrxTime, String localTrxDate, String captureDate, String merchantType, String posem, String amountFee, String bit32, String qrisId, String retrievalReferenceNumber, String authIdResponse, String terminalId, String cardAcceptorId, String terminalLocation, String bit48, String curencyCode, String bit57, String fromAccountNo, String mpan, String status, String invoice, Timestamp createdAt) {
        this.pan = pan;
        this.pcode = pcode;
        this.trxAmount = trxAmount;
        this.trxDateTime = trxDateTime;
        this.trace = trace;
        this.localTrxTime = localTrxTime;
        this.localTrxDate = localTrxDate;
        this.captureDate = captureDate;
        this.merchantType = merchantType;
        this.posem = posem;
        this.amountFee = amountFee;
        this.bit32 = bit32;
        this.qrisId = qrisId;
        this.retrievalReferenceNumber = retrievalReferenceNumber;
        this.authIdResponse = authIdResponse;
        this.terminalId = terminalId;
        this.cardAcceptorId = cardAcceptorId;
        this.terminalLocation = terminalLocation;
        this.bit48 = bit48;
        this.curencyCode = curencyCode;
        this.bit57 = bit57;
        this.fromAccountNo = fromAccountNo;
        this.mpan = mpan;
        this.status = status;
        this.invoice = invoice;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPan() {
        return pan;
    }

    public void setPan(String pan) {
        this.pan = pan;
    }

    public String getPcode() {
        return pcode;
    }

    public void setPcode(String pcode) {
        this.pcode = pcode;
    }

    public BigInteger getTrxAmount() {
        return trxAmount;
    }

    public void setTrxAmount(BigInteger trxAmount) {
        this.trxAmount = trxAmount;
    }

    public String getTrxDateTime() {
        return trxDateTime;
    }

    public void setTrxDateTime(String trxDateTime) {
        this.trxDateTime = trxDateTime;
    }

    public String getTrace() {
        return trace;
    }

    public void setTrace(String trace) {
        this.trace = trace;
    }

    public String getLocalTrxTime() {
        return localTrxTime;
    }

    public void setLocalTrxTime(String localTrxTime) {
        this.localTrxTime = localTrxTime;
    }

    public String getLocalTrxDate() {
        return localTrxDate;
    }

    public void setLocalTrxDate(String localTrxDate) {
        this.localTrxDate = localTrxDate;
    }

    public String getCaptureDate() {
        return captureDate;
    }

    public void setCaptureDate(String captureDate) {
        this.captureDate = captureDate;
    }

    public String getMerchantType() {
        return merchantType;
    }

    public void setMerchantType(String merchantType) {
        this.merchantType = merchantType;
    }

    public String getPosem() {
        return posem;
    }

    public void setPosem(String posem) {
        this.posem = posem;
    }

    public String getAmountFee() {
        return amountFee;
    }

    public void setAmountFee(String amountFee) {
        this.amountFee = amountFee;
    }

    public String getBit32() {
        return bit32;
    }

    public void setBit32(String bit32) {
        this.bit32 = bit32;
    }

    public String getQrisId() {
        return qrisId;
    }

    public void setQrisId(String qrisId) {
        this.qrisId = qrisId;
    }

    public String getRetrievalReferenceNumber() {
        return retrievalReferenceNumber;
    }

    public void setRetrievalReferenceNumber(String retrievalReferenceNumber) {
        this.retrievalReferenceNumber = retrievalReferenceNumber;
    }

    public String getAuthIdResponse() {
        return authIdResponse;
    }

    public void setAuthIdResponse(String authIdResponse) {
        this.authIdResponse = authIdResponse;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCardAcceptorId() {
        return cardAcceptorId;
    }

    public void setCardAcceptorId(String cardAcceptorId) {
        this.cardAcceptorId = cardAcceptorId;
    }

    public String getTerminalLocation() {
        return terminalLocation;
    }

    public void setTerminalLocation(String terminalLocation) {
        this.terminalLocation = terminalLocation;
    }

    public String getBit48() {
        return bit48;
    }

    public void setBit48(String bit48) {
        this.bit48 = bit48;
    }

    public String getCurencyCode() {
        return curencyCode;
    }

    public void setCurencyCode(String curencyCode) {
        this.curencyCode = curencyCode;
    }

    public String getBit57() {
        return bit57;
    }

    public void setBit57(String bit57) {
        this.bit57 = bit57;
    }

    public String getFromAccountNo() {
        return fromAccountNo;
    }

    public void setFromAccountNo(String fromAccountNo) {
        this.fromAccountNo = fromAccountNo;
    }

    public String getMpan() {
        return mpan;
    }

    public void setMpan(String mpan) {
        this.mpan = mpan;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "TrxLog{" +
                "id=" + id +
                ", pan='" + pan + '\'' +
                ", pcode='" + pcode + '\'' +
                ", trxAmount=" + trxAmount +
                ", trxDateTime='" + trxDateTime + '\'' +
                ", trace='" + trace + '\'' +
                ", localTrxTime='" + localTrxTime + '\'' +
                ", localTrxDate='" + localTrxDate + '\'' +
                ", captureDate='" + captureDate + '\'' +
                ", merchantType='" + merchantType + '\'' +
                ", posem='" + posem + '\'' +
                ", amountFee='" + amountFee + '\'' +
                ", bit32='" + bit32 + '\'' +
                ", qrisId='" + qrisId + '\'' +
                ", retrievalReferenceNumber='" + retrievalReferenceNumber + '\'' +
                ", authIdResponse='" + authIdResponse + '\'' +
                ", terminalId='" + terminalId + '\'' +
                ", cardAcceptorId='" + cardAcceptorId + '\'' +
                ", terminalLocation='" + terminalLocation + '\'' +
                ", bit48='" + bit48 + '\'' +
                ", curencyCode='" + curencyCode + '\'' +
                ", bit57='" + bit57 + '\'' +
                ", fromAccountNo='" + fromAccountNo + '\'' +
                ", mpan='" + mpan + '\'' +
                ", status='" + status + '\'' +
                ", invoice='" + invoice + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}

