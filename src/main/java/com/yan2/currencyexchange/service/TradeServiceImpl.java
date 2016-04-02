package com.yan2.currencyexchange.service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yan2.currencyexchange.dao.TradeDAO;
import com.yan2.currencyexchange.model.Trade;

/**
 * The implemetation of the TradeService class.
 */
@Service
public class TradeServiceImpl implements TradeService {

    @Inject
    TradeDAO tradeDAO;

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean saveTrade(Trade trade) {

        UUID uuid = UUID.randomUUID();
        trade.setId(uuid);

        tradeDAO.saveTrade(trade);

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Trade> getListTradesFromDate(Date fromTimePlaced) {
        return tradeDAO.getListTradesFromDate(new java.sql.Date(fromTimePlaced.getTime()));
    }

}
