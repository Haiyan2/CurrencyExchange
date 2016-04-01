package com.yan2.currencyexchange;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Application class that initializes Spring Boot Application.
 */
@SpringBootApplication
public class CurrencyExchangeApplication {

    private static final Logger LOGGER = LoggerFactory.getLogger(CurrencyExchangeApplication.class);

    public static void main(String[] args) throws Exception {

        LOGGER.info("Starting application CurrencyExchange.");

        SpringApplication.run(CurrencyExchangeApplication.class, args);
    }
}
