package com.yan2.currencyexchange.dao;

import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;

/**
 * Interface for Cassandra client.
 */
public interface CassandraClient {

    /**
     * Get the connected Cassandra instance.
     * 
     * @return The instance.
     */
    Session getInstance();
    
    /**
     * Execute the query.
     * 
     * @param query The detail statement.
     * @return The result set of the query.
     */
    ResultSet executeQuery(Statement query);

}