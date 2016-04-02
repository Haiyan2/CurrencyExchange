package com.yan2.currencyexchange.api;

import javax.inject.Inject;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yan2.currencyexchange.CurrencyExchangeApplication;
import com.yan2.currencyexchange.model.Trade;
import com.yan2.currencyexchange.service.TradeService;

/**
 * Trade API Endpoint.
 */
@RestController
public class TradeAPIController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeApplication.class);

    @Inject
    TradeService tradeService;

    @RequestMapping(method = {
            RequestMethod.POST }, path = "/api/trades", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(code=HttpStatus.CREATED)
    public void createTrade(@Valid @RequestBody Trade trade) {
        
        LOGGER.debug("Trade request: {}.", trade);
        tradeService.saveTrade(trade);
    }

}
