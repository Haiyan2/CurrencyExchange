package com.yan2.currencyexchange.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collector;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.yan2.currencyexchange.model.TradeSummary;
import com.yan2.currencyexchange.web.TradeController;

/**
 * The trade analytics data processor.
 */
@Service
public class TradeAnalyticsDataProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    private List<TradeAnalyticsDataListener> listeners = new ArrayList<>();

    private ScheduledExecutorService scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

    @Inject
    TradeAnalyticsService tradeAnalyticsService;

    @Value("${trade.summary.hours}")
    private int nbHours;

    @Value("${trade.summary.initialdelay}")
    private int initialDelay;

    @Value("${trade.summary.refreshinterval}")
    private int refreshInterval;

    /**
     * Initialize the bean.
     */
    @PostConstruct
    public void init() {

        // Use scheduled service to constantly update the data. Wait with a
        // refresh interval (seconds)
        // after completion before running again.
        scheduledExecutorService.scheduleWithFixedDelay(new ProcessCommand(), initialDelay, refreshInterval,
                TimeUnit.SECONDS);
    }

    /**
     * Destroy the bean.
     */
    @PreDestroy
    public void stop() {
        scheduledExecutorService.shutdown();
    }

    /**
     * Add a listener to the listeners list.
     * 
     * @param listener
     *            A listener to add.
     */
    public void addListener(TradeAnalyticsDataListener listener) {
        listeners.add(listener);
    }

    void notifyListeners(String data) {
        for (TradeAnalyticsDataListener listener : listeners) {
            listener.dataUpdated(data);
        }
    }

    /**
     * Process Command to run.
     */
    class ProcessCommand implements Runnable {

        /**
         * Generate CSV for chart and notify the listeners: <code>
         * from,to,count
         * "AED","AUD",0
         * "EUR","SGD",1000
         * "USD","EUR",500
         * </code>
         * 
         * @return
         */
        @Override
        public void run() {

            LOGGER.info("Starting to get new data.");

            List<TradeSummary> tradeSummaryList = tradeAnalyticsService.getTradeSummaryFromHours(nbHours);

            String data = tradeSummaryList.stream()
                    .collect(Collector.of(() -> new StringBuilder("from,to,count\n"), (sb, tradeSummary) -> {

                        sb.append('"');
                        sb.append(tradeSummary.getCurrencyFrom());
                        sb.append('"');
                        sb.append(',');
                        sb.append('"');
                        sb.append(tradeSummary.getCurrencyTo());
                        sb.append('"');
                        sb.append(',');
                        sb.append(tradeSummary.getTotalTransactions());
                        sb.append('\n');
                    } , StringBuilder::append, StringBuilder::toString));

            notifyListeners(data);
        }
    }
}