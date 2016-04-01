package com.yan2.currencyexchange.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yan2.currencyexchange.dao.TradeDAO;
import com.yan2.currencyexchange.model.Trade;

@Service
public class TradeServiceImpl implements TradeService {

    @Inject
    TradeDAO tradeDAO;

    @Override
    public boolean saveTrade(Trade trade) {

        UUID uuid = UUID.randomUUID();
        trade.setId(uuid);

        tradeDAO.saveTrade(trade);

        return true;
    }

    @Override
    public List<Trade> getListTrades(Date fromTimePlaced) {
        return tradeDAO.getListTrades(new java.sql.Date(fromTimePlaced.getTime()));
    }

}
