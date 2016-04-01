package com.yan2.currencyexchange.dao;

import static com.datastax.driver.core.querybuilder.QueryBuilder.gt;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.querybuilder.QueryBuilder;
import com.yan2.currencyexchange.model.Trade;

@Service
public class TradeDAOCassandra implements TradeDAO {
    /**
     * Logger.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TradeDAOCassandra.class);

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
    CassandraClient cassandraClient;

    @PostConstruct
    public void init() {

    }

    @Override
    public boolean saveTrade(Trade trade) {

        Statement query = QueryBuilder.insertInto(DATABASE, TABLE_TRADE).value(ID, trade.getId())
                .value(USERID, trade.getUserId()).value(CURRENCY_FROM, trade.getCurrencyFrom())
                .value(CURRENCY_TO, trade.getCurrencyTo()).value(AMOUNT_SELL, trade.getAmountSell())
                .value(AMOUNT_BUY, trade.getAmountBuy()).value(RATE, trade.getRate())
                .value(TIME_PLACED, trade.getTimePlaced()).value(ORINGINATING_COUNTRY, trade.getOriginatingCountry());

        cassandraClient.executeQuery(query);

        LOGGER.info("Save trade {}.", trade);

        return true;
    }

    @Override
    public List<Trade> getListTrades(Date date) {

        Statement query = QueryBuilder.select().all().from(DATABASE, TABLE_TRADE).allowFiltering()
                .where(gt(TIME_PLACED, date));

        ResultSet resultSet = cassandraClient.executeQuery(query);

        List<Trade> trades = new ArrayList<>();
        for (Row row : resultSet) {
            Trade trade = row2Trade(row);
            trades.add(trade);
        }

        LOGGER.info("Get list of trade with total number: {} .", trades.size());

        return trades;
    }

    private Trade row2Trade(Row row) {
        Trade trade = new Trade();
        trade.setId(row.getUUID(ID));
        trade.setUserId(row.getString(USERID));
        trade.setCurrencyFrom(row.getString(CURRENCY_FROM));
        trade.setCurrencyTo(row.getString(CURRENCY_TO));
        trade.setAmountBuy(row.getDecimal(AMOUNT_SELL));
        trade.setAmountSell(row.getDecimal(AMOUNT_BUY));
        trade.setRate(row.getDecimal(RATE));
        trade.setTimePlaced(row.getTimestamp(TIME_PLACED));
        trade.setOriginatingCountry(row.getString(ORINGINATING_COUNTRY));

        return trade;
    }
}
