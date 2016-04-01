package com.yan2.currencyexchange.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import com.yan2.currencyexchange.model.Trade;
import com.yan2.currencyexchange.model.TradeSummary;

@Service
public class TradeAnalyticsService {

    @Inject
    TradeService tradeService;

    public List<TradeSummary> getTradeSummary24h() {

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, -24);

        List<Trade> tradeList = tradeService.getListTrades(calendar.getTime());

        Map<StringTuple, TradeSummary> tradeSummaryByCurrencies = new HashMap<>();

        for (Trade trade : tradeList) {
            StringTuple currencies = new StringTuple(trade.getCurrencyFrom(), trade.getCurrencyTo());

            TradeSummary tradeSummary;
            if (tradeSummaryByCurrencies.containsKey(currencies)) {
                tradeSummary = tradeSummaryByCurrencies.get(currencies);
            } else {
                tradeSummary = new TradeSummary(trade.getCurrencyFrom(), trade.getCurrencyTo());
                tradeSummaryByCurrencies.put(currencies, tradeSummary);
                
                // Create empty TradeSummary with reversed origin and destination currencies.
                StringTuple currenciesReversed = new StringTuple(trade.getCurrencyTo(), trade.getCurrencyFrom());
                TradeSummary tradeSummaryReverse = new TradeSummary(trade.getCurrencyTo(), trade.getCurrencyFrom());
                tradeSummaryByCurrencies.put(currenciesReversed, tradeSummaryReverse);
            }

            tradeSummary.setTotalAmountSell(tradeSummary.getTotalAmountSell().add(trade.getAmountSell()));
            tradeSummary.setTotalTransactions(tradeSummary.getTotalTransactions() + 1);
        }

        List<TradeSummary> tradeSummaryList = new ArrayList<>(tradeSummaryByCurrencies.values());
        Collections.sort(tradeSummaryList, new Comparator<TradeSummary>() {

            @Override
            public int compare(TradeSummary o1, TradeSummary o2) {
                return (o1.getCurrencyFrom() + "-" + o1.getCurrencyTo())
                        .compareTo(o2.getCurrencyFrom() + "-" + o2.getCurrencyTo());
            }
        });
        
        return tradeSummaryList;
    }

    private class StringTuple {
        String str1;
        String str2;

        public StringTuple(String str1, String str2) {
            this.str1 = str1;
            this.str2 = str2;
        }

        @Override
        public String toString() {
            return str1 + "-" + str2;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + getOuterType().hashCode();
            result = prime * result + ((str1 == null) ? 0 : str1.hashCode());
            result = prime * result + ((str2 == null) ? 0 : str2.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            StringTuple other = (StringTuple) obj;
            if (!getOuterType().equals(other.getOuterType()))
                return false;
            if (str1 == null) {
                if (other.str1 != null)
                    return false;
            } else if (!str1.equals(other.str1))
                return false;
            if (str2 == null) {
                if (other.str2 != null)
                    return false;
            } else if (!str2.equals(other.str2))
                return false;
            return true;
        }

        private TradeAnalyticsService getOuterType() {
            return TradeAnalyticsService.this;
        }
    }

}
