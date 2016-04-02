package com.yan2.currencyexchange.service;

/**
 * The trade data listener class for analytics.
 */
public interface TradeAnalyticsDataListener {

    /**
     * Perform the event on data updated.
     * 
     * @param data The updated data.
     */
    void dataUpdated(String data);
}
