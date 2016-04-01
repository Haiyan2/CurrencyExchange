package com.yan2.currencyexchange.dao;

import java.sql.Date;
import java.util.List;

import com.yan2.currencyexchange.model.Trade;

public interface TradeDAO {
    boolean saveTrade(Trade trade);

    List<Trade> getListTrades(Date fromTimePlaced);

}
