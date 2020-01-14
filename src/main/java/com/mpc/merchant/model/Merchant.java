package com.mpc.merchant.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String countryCode;
    private String merchantName;
    private String merchantCity;
    private String postalCode;
    private String globallyUniqueId;
    private String mpan;
    private String rekening;
    private String merchantId;
    private String criteria;
    private String mobileId;

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

    public String getRekening() {
        return rekening;
    }

    public void setRekening(String rekening) {
        this.rekening = rekening;
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

    public String getMobileId() {
        return mobileId;
    }

    public void setMobileId(String mobileId) {
        this.mobileId = mobileId;
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
                ", rekening='" + rekening + '\'' +
                ", merchantId='" + merchantId + '\'' +
                ", criteria='" + criteria + '\'' +
                ", mobileId='" + mobileId + '\'' +
                '}';
    }
}
