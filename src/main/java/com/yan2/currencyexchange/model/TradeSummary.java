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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((currencyFrom == null) ? 0 : currencyFrom.hashCode());
        result = prime * result + ((currencyTo == null) ? 0 : currencyTo.hashCode());
        result = prime * result + ((totalAmountSell == null) ? 0 : totalAmountSell.hashCode());
        result = prime * result + (int) (totalTransactions ^ (totalTransactions >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TradeSummary other = (TradeSummary) obj;
        if (currencyFrom == null) {
            if (other.currencyFrom != null)
                return false;
        } else if (!currencyFrom.equals(other.currencyFrom))
            return false;
        if (currencyTo == null) {
            if (other.currencyTo != null)
                return false;
        } else if (!currencyTo.equals(other.currencyTo))
            return false;
        if (totalAmountSell == null) {
            if (other.totalAmountSell != null)
                return false;
        } else if (!totalAmountSell.equals(other.totalAmountSell))
            return false;
        if (totalTransactions != other.totalTransactions)
            return false;
        return true;
    }
    
}
