package com.mpc.merchant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.math.BigInteger;
import java.sql.Timestamp;

@Entity
public class Refund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private Integer idTrxLog;
    private BigInteger trx_refund;
    private Timestamp createdAt;

    public Refund() {
    }

    public Refund(Integer idTrxLog, BigInteger trx_refund, Timestamp createdAt) {
        this.idTrxLog = idTrxLog;
        this.trx_refund = trx_refund;
        this.createdAt = createdAt;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdTrxLog() {
        return idTrxLog;
    }

    public void setIdTrxLog(Integer idTrxLog) {
        this.idTrxLog = idTrxLog;
    }

    public BigInteger getTrx_refund() {
        return trx_refund;
    }

    public void setTrx_refund(BigInteger trx_refund) {
        this.trx_refund = trx_refund;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Refund{" +
                "id=" + id +
                ", idTrxLog=" + idTrxLog +
                ", trx_refund=" + trx_refund +
                ", createdAt=" + createdAt +
                '}';
    }
}
