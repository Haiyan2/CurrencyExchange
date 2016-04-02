package com.yan2.currencyexchange.simulator.api;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * The simulator for trade rest API.
 */
@Service
public class TradeAPISimulator {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeAPISimulator.class);

    private static String[] CURRENCIES = { "AED", "AUD", "CAD", "CHF", "CZK", "DKK", "EUR", "GBP", "HKD", "HUF", "NOK",
            "NZD", "PLN", "SEK", "SGD", "USD", "ZAR" };

    @Value("${trade.simulator.enabled}")
    boolean simulatorEnabled;

    /**
     * Initialize.
     */
    @PostConstruct
    public void init() {

        if (simulatorEnabled) {
            LOGGER.warn("Simulator is enabled!");
            ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(10);

            scheduledExecutorService.scheduleWithFixedDelay(new PostTradeCommand(), 30000, 10, TimeUnit.MILLISECONDS);
        } else {
            LOGGER.debug("Simulator is disabled.");
        }
    }

    class PostTradeCommand implements Runnable {

        @Override
        public void run() {

            LOGGER.debug("POSTing from Simulator.");

            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");

            URL url;
            HttpURLConnection connection = null;

            try {
                String from = CURRENCIES[(int) (Math.random() * CURRENCIES.length)];

                String to = CURRENCIES[(int) (Math.random() * CURRENCIES.length)];
                while (from.equals(to)) {
                    to = CURRENCIES[(int) (Math.random() * CURRENCIES.length)];
                }

                url = new URL("http://localhost:8080/api/trades");

                connection = (HttpURLConnection) url.openConnection();
                connection.setDoOutput(true);
                connection.setInstanceFollowRedirects(false);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                // connection.setRequestProperty("charset", "utf-8");

                OutputStream os = connection.getOutputStream();

                String msg = "{\"userId\": \"134256\", \"currencyFrom\": \"" + from + "\", \"currencyTo\": \"" + to
                        + "\", \"amountSell\": 3000, \"amountBuy\": 707.10, \"rate\": 0.7471, \"timePlaced\" : \""
                        + dateFormat.format(new Date()) + "\", \"originatingCountry\" : \"FR\"}";

                LOGGER.info("POSTing simulated message: {}", msg);

                os.write(msg.getBytes("UTF-8"));
                os.close();

                // Get Response
                int responseCode = connection.getResponseCode();

                LOGGER.info("Response code is {}.", responseCode);

                connection.connect();
            } catch (Exception e) {
                LOGGER.error("Error.", e);
            } finally {

                if (connection != null) {

                    try {
                        connection.disconnect();
                    } catch (Exception e) {
                        LOGGER.error("Error.", e);
                    }
                }
            }
        }

    }
}
