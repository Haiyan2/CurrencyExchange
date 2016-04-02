package com.yan2.currencyexchange.service;

import java.util.Date;
import java.util.List;

import com.yan2.currencyexchange.model.Trade;

/**
 * Interface of trade service.
 */
public interface TradeService {
    
    /**
     * Save the trade.
     * 
     * @param trade The trade object.
     * @return The state of the saving.
     */
    public boolean saveTrade(Trade trade);
    
    /**
     * Get the list of the trades that are created after the parameter <code>fromTimePlaced</code>.
     * 
     * @param fromTimePlaced The date after which the list of trades created will be retrieved.
     * @return The list of trades.
     */
    public List<Trade> getListTradesFromDate(Date fromTimePlaced);


}
