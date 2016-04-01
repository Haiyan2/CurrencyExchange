package com.yan2.currencyexchange.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

public interface CassandraClient {

    Session getInstance();
    ResultSet executeQuery(Statement query);

}