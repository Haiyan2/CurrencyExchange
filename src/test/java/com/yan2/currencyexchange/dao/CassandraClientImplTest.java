package com.yan2.currencyexchange.dao;

import static com.datastax.driver.core.querybuilder.QueryBuilder.eq;
import static org.junit.Assert.assertEquals;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.yan2.currencyexchange.CurrencyExchangeApplication;
import com.yan2.currencyexchange.model.Trade;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = CurrencyExchangeApplication.class)
@TestPropertySource(locations = "classpath:test.properties")
public class CassandraClientImplTest {

    private static final String DATABASE = "currencyexchange";
    private static final String TABLE_TRADE = "trades";

    private static final String ID = "id";
    private static final String USERID = "userid";
    private static final String CURRENCY_FROM = "currencyfrom";
    private static final String CURRENCY_TO = "currencyto";
    private static final String AMOUNT_SELL = "amountsell";
    private static final String AMOUNT_BUY = "amountbuy";
    private static final String RATE = "rate";
    private static final String TIME_PLACED = "timeplaced";
    private static final String ORINGINATING_COUNTRY = "originatingcountry";

    @Inject
    private CassandraClientImpl cassandraClientImpl;

    @Test
    public void testExecuteQuery() {

        Trade testTrade1 = new Trade();
        Trade testTrade2 = new Trade();

        testTrade1.setUserId("1");
        testTrade1.setCurrencyFrom("EUR");
        testTrade1.setCurrencyTo("USD");
        testTrade1.setAmountSell(new BigDecimal(500.50));
        testTrade1.setAmountBuy(new BigDecimal(570.57));
        testTrade1.setRate(new BigDecimal(1.14));
        testTrade1.setOriginatingCountry("FR");

        testTrade2.setUserId("2");
        testTrade2.setCurrencyFrom("USD");
        testTrade2.setCurrencyTo("SGD");
        testTrade2.setAmountSell(new BigDecimal(200.2));
        testTrade2.setAmountBuy(new BigDecimal(270.27));
        testTrade2.setRate(new BigDecimal(1.35));
        testTrade2.setOriginatingCountry("IE");

        Trade[] testTrades = new Trade[] { testTrade1, testTrade2 };

        for (int i = 0; i < testTrades.length; i++) {
            testTrades[i].setId(UUID.randomUUID());
            testTrades[i].setTimePlaced(new Date());
        }

        // 1. Insert test Trade data
        for (int i = 0; i < testTrades.length; i++) {
            System.out.println("***********************" + testTrades[i]);
            Statement query = QueryBuilder.insertInto(DATABASE, TABLE_TRADE).value(ID, testTrades[i].getId())
                    .value(USERID, testTrades[i].getUserId()).value(CURRENCY_FROM, testTrades[i].getCurrencyFrom())
                    .value(CURRENCY_TO, testTrades[i].getCurrencyTo()).value(AMOUNT_SELL, testTrades[i].getAmountSell())
                    .value(AMOUNT_BUY, testTrades[i].getAmountBuy()).value(RATE, testTrades[i].getRate())
                    .value(TIME_PLACED, testTrades[i].getTimePlaced())
                    .value(ORINGINATING_COUNTRY, testTrades[i].getOriginatingCountry());

            cassandraClientImpl.getInstance().execute(query);
        }

        // 2. Retrieve test Trade data and verify the results
        List<Row> resultList = new ArrayList<>();
        for (int i = 0; i < testTrades.length; i++) {
            Statement query = QueryBuilder.select().all().from(DATABASE, TABLE_TRADE)
                    .where(eq(ID, testTrades[i].getId())).limit(1);
            resultList.add(cassandraClientImpl.getInstance().execute(query).one());
        }

        assertEquals(2, resultList.size());
        for (int i = 0; i < resultList.size(); i++) {
            assertEquals(resultList.get(i).getString(USERID), testTrades[i].getUserId());
            assertEquals(resultList.get(i).getString(CURRENCY_FROM), testTrades[i].getCurrencyFrom());
            assertEquals(resultList.get(i).getString(CURRENCY_TO), testTrades[i].getCurrencyTo());
            assertEquals(resultList.get(i).getDecimal(AMOUNT_SELL), testTrades[i].getAmountSell());
            assertEquals(resultList.get(i).getDecimal(AMOUNT_BUY), testTrades[i].getAmountBuy());
            assertEquals(resultList.get(i).getDecimal(RATE), testTrades[i].getRate());
            assertEquals(resultList.get(i).getTimestamp(TIME_PLACED), testTrades[i].getTimePlaced());
            assertEquals(resultList.get(i).getString(ORINGINATING_COUNTRY), testTrades[i].getOriginatingCountry());
        }

        // 3. Clean up the test Trade data and verify the results
        for (int i = 0; i < testTrades.length; i++) {
            Statement query = QueryBuilder.delete().all().from(DATABASE, TABLE_TRADE)
                    .where(eq(ID, testTrades[i].getId()));
            cassandraClientImpl.getInstance().execute(query);
        }
        for (int i = 0; i < testTrades.length; i++) {
            Statement query = QueryBuilder.select().all().from(DATABASE, TABLE_TRADE)
                    .where(eq(ID, testTrades[i].getId())).limit(1);
            assertEquals(0, cassandraClientImpl.getInstance().execute(query).all().size());
        }

    }

}
