package com.yan2.currencyexchange.model;

import java.math.BigDecimal;

public class TradeSummary {

    private String currencyFrom;
    private String currencyTo;
    private BigDecimal totalAmountSell = new BigDecimal(0);
    private long totalTransactions = 0;
    
    
    public TradeSummary(String currencyFrom, String currencyTo) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
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
    public BigDecimal getTotalAmountSell() {
        return totalAmountSell;
    }
    public void setTotalAmountSell(BigDecimal totalAmountSell) {
        this.totalAmountSell = totalAmountSell;
    }
    public long getTotalTransactions() {
        return totalTransactions;
    }
    public void setTotalTransactions(long totalTransactions) {
        this.totalTransactions = totalTransactions;
    }
    
    
}
