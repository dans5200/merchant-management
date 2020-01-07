package com.mpc.merchant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    String countryCode;
    String merchantName;
    String merchantCity;
    String postalCode;
    String globallyUniqueId;
    String mpan;
    String merchantId;
    String criteria;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCity() {
        return merchantCity;
    }

    public void setMerchantCity(String merchantCity) {
        this.merchantCity = merchantCity;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getGloballyUniqueId() {
        return globallyUniqueId;
    }

    public void setGloballyUniqueId(String globallyUniqueId) {
        this.globallyUniqueId = globallyUniqueId;
    }

    public String getMpan() {
        return mpan;
    }

    public void setMpan(String mpan) {
        this.mpan = mpan;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getCriteria() {
        return criteria;
    }

    public void setCriteria(String criteria) {
        this.criteria = criteria;
    }

    @Override
    public String toString() {
        return "Merchant{" +
                "id=" + id +
                ", countryCode='" + countryCode + '\'' +
                ", merchantName='" + merchantName + '\'' +
                ", merchantCity='" + merchantCity + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", globallyUniqueId='" + globallyUniqueId + '\'' +
                ", mpan='" + mpan + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", criteria='" + criteria + '\'' +
                '}';
    }
}