package com.yan2.currencyexchange.web;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yan2.currencyexchange.service.TradeAnalyticsDataListener;
import com.yan2.currencyexchange.service.TradeAnalyticsDataProcessor;

/**
 * 
 */
@Controller
public class TradeController implements TradeAnalyticsDataListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    @Inject
    TradeAnalyticsDataProcessor dataProcessor;

    /**
     * Initialize.
     */
    @PostConstruct
    public void init() {

        dataProcessor.addListener(this);
    }

    String data = "from,to,count";

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized void dataUpdated(String data) {

        LOGGER.info("Data Updated.");
        this.data = data;
    }

    @RequestMapping("/")
    @ResponseBody
    String home() {
        return "Welcome to Trade World!";
    }

    @RequestMapping("/exchangeDyn.csv")
    @ResponseBody
    String exchange() {
        return data;
    }
}
