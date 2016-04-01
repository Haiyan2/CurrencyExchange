package com.yan2.currencyexchange.service;

import java.util.Date;
import java.util.List;

import com.yan2.currencyexchange.model.Trade;


public interface TradeService {
    public boolean saveTrade(Trade ce);

    public List<Trade> getListTrades(Date fromTimePlaced);


}
