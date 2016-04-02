package com.yan2.currencyexchange.dao;

import java.sql.Date;
import java.util.List;

import com.yan2.currencyexchange.model.Trade;

/**
 * The interface for the Trade data access object.
 */
public interface TradeDAO {
    
    /**
     * Save trade.
     * 
     * @param trade The Trade object.
     * @return The state of the saving.
     */
    boolean saveTrade(Trade trade);

    /**
     * Get the list of the trades that are created after the parameter <code>fromTimePlaced</code>.
     * 
     * @param fromTimePlaced The date after which the list of trades created will be retrieved.
     * @return The list of trades.
     */
    List<Trade> getListTradesFromDate(Date fromTimePlaced);

}
