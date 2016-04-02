package com.yan2.currencyexchange.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import com.yan2.currencyexchange.model.Trade;
import com.yan2.currencyexchange.model.TradeSummary;

@RunWith(MockitoJUnitRunner.class)
public class TradeAnalyticsServiceTest {

    @Mock
    TradeService tradeService;

    @InjectMocks
    TradeAnalyticsService tradeAnalyticsService = new TradeAnalyticsService();

    Trade testTrade1 = new Trade();
    Trade testTrade2 = new Trade();
    List<Trade> testTrades = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        testTrade1.setCurrencyFrom("EUR");
        testTrade1.setCurrencyTo("USD");
        testTrade1.setAmountSell(new BigDecimal(500.50));
        testTrade1.setAmountBuy(new BigDecimal(570.57));
        testTrade1.setRate(new BigDecimal(1.14));
        testTrade1.setTimePlaced(new Date());
        testTrade1.setOriginatingCountry("FR");

        testTrades.add(testTrade1);

        testTrade2.setCurrencyFrom("USD");
        testTrade2.setCurrencyTo("SGD");
        testTrade2.setAmountSell(new BigDecimal(200.2));
        testTrade2.setAmountBuy(new BigDecimal(270.27));
        testTrade2.setRate(new BigDecimal(1.35));
        testTrade2.setTimePlaced(new Date());
        testTrade2.setOriginatingCountry("IE");

        testTrades.add(testTrade2);

    }

    @Test
    public void testGetTradeSummary24h() {

        // Setup the trades returned by the mocked TradeService.
        when(tradeService.getListTradesFromDate(any())).thenReturn(testTrades);

        List<TradeSummary> tradeSummaryList = tradeAnalyticsService.getTradeSummaryFromHours(24);

        assertEquals(4, tradeSummaryList.size());

        TradeSummary tradeSummaryExpected0 = new TradeSummary("EUR", "USD");
        tradeSummaryExpected0.setTotalAmountSell(new BigDecimal(500.50));
        tradeSummaryExpected0.setTotalTransactions(1);

        TradeSummary tradeSummaryExpected1 = new TradeSummary("SGD", "USD");

        TradeSummary tradeSummaryExpected2 = new TradeSummary("USD", "EUR");

        TradeSummary tradeSummaryExpected3 = new TradeSummary("USD", "SGD");
        tradeSummaryExpected3.setTotalAmountSell(new BigDecimal(200.2));
        tradeSummaryExpected3.setTotalTransactions(1);

        
        TradeSummary[] expected = new TradeSummary[] { tradeSummaryExpected0, tradeSummaryExpected1, tradeSummaryExpected2, tradeSummaryExpected3};

        for (int i = 0; i < expected.length; i++) {
            assertEquals(expected[i], tradeSummaryList.get(i));
        }
            
    }

}
