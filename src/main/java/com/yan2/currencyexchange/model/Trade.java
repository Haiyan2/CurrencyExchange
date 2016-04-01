package com.yan2.currencyexchange.model;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Trade model class.
 *
 */
public class Trade {

    @JsonIgnore
    private UUID id;
    
    @NotNull
    @NotEmpty
    private String userId;
    
    @NotNull
    @NotEmpty
    private String currencyFrom;
    
    @NotNull
    @NotEmpty
    private String currencyTo;
    
    @NotNull
    private BigDecimal amountSell;
    
    @NotNull
    private BigDecimal amountBuy;
    
    @NotNull
    private BigDecimal rate;

    @NotNull
    @JsonFormat(pattern = "dd-MMM-yy HH:mm:ss")
    private Date timePlaced;
    
    @NotNull
    @NotEmpty
    private String originatingCountry;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public BigDecimal getAmountSell() {
        return amountSell;
    }

    public void setAmountSell(BigDecimal amountSell) {
        this.amountSell = amountSell;
    }

    public BigDecimal getAmountBuy() {
        return amountBuy;
    }

    public void setAmountBuy(BigDecimal amountBuy) {
        this.amountBuy = amountBuy;
    }

    public BigDecimal getRate() {
        return rate;
    }

    public void setRate(BigDecimal rate) {
        this.rate = rate;
    }

    public Date getTimePlaced() {
        return timePlaced;
    }

    public void setTimePlaced(Date timePlaced) {
        this.timePlaced = timePlaced;
    }

    public String getOriginatingCountry() {
        return originatingCountry;
    }

    public void setOriginatingCountry(String originatingCountry) {
        this.originatingCountry = originatingCountry;
    }

    @Override
    public String toString() {
        return "CurrencyExchange [id=" + id + ", userId=" + userId + ", currencyFrom=" + currencyFrom + ", currencyTo="
                + currencyTo + ", amountSell=" + amountSell + ", amountBuy=" + amountBuy + ", rate=" + rate
                + ", timePlaced=" + timePlaced + ", originatingCountry=" + originatingCountry + "]";
    }

}
