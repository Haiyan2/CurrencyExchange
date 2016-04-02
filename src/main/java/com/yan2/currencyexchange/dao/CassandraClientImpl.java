package com.yan2.currencyexchange.dao;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.ResultSet;
import com.datastax.driver.core.Row;
import com.datastax.driver.core.Session;
import com.datastax.driver.core.Statement;
import com.datastax.driver.core.exceptions.InvalidQueryException;
import com.datastax.driver.core.exceptions.NoHostAvailableException;

/**
 * The implementation for CassandraClient.
 */
@Service
public class CassandraClientImpl implements CassandraClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraClientImpl.class);

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

    private Cluster cluster;
    private Session session;

    private boolean connected = false;

    @Value("${cassandra.node}")
    String node;

    /**
     * Initialize the connection to Cassandra database.
     */
    @PostConstruct
    public synchronized void init() {

        LOGGER.info("Connecting Cassandra to node {}.", node);
        startConnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public synchronized Session getInstance() {

        if (connected) {
            return this.session;
        } else {
            throw new IllegalStateException("Not connected to Cassandra.");
        }
    }

    private synchronized void startConnect() {

        if (connected) {
            LOGGER.info("Already connected to Cassandra.");

        } else {
            try {
                LOGGER.info("Start to connect to Cassandra.");
                cluster = Cluster.builder().addContactPoint(node).build();
                Metadata metadata = cluster.getMetadata();
                LOGGER.info("Connected to cluster: " + metadata.getClusterName());

                try {
                    session = cluster.connect(DATABASE);
                } catch (InvalidQueryException invalidQueryException) {
                    session = cluster.connect();
                    createKeyspace();
                    createTableTrade();
                }
                LOGGER.info("Connect Cassandra cluster: {}.", node);
                connected = true;

            } catch (NoHostAvailableException noHostAvailableException) {
                LOGGER.error("Fail to connect to Cassandra on: " + node, noHostAvailableException);
                throw noHostAvailableException;
            }
        }
    }

    /**
     * CQL data types to Java types:
     * http://docs.datastax.com/en//developer/java-driver/2.0/java-driver/
     * reference/javaClass2Cql3Datatypes_r.html
     */
    void createKeyspace() {
        LOGGER.info("Create Cassandra Keyspace {} if not exists.", DATABASE);
        session.execute("CREATE KEYSPACE IF NOT EXISTS " + DATABASE + " WITH replication "
                + "= {'class':'SimpleStrategy', 'replication_factor':1};");

        session.execute("USE " + DATABASE + ";");

    }

    void createTableTrade() {
        LOGGER.info("Create Cassandra Table {} if not exists.", TABLE_TRADE);
        session.execute("CREATE TABLE IF NOT EXISTS " + TABLE_TRADE + " (" + ID + " uuid, " + USERID + " text, "
                + CURRENCY_FROM + " text, " + CURRENCY_TO + " text, " + AMOUNT_SELL + " decimal, " + AMOUNT_BUY
                + " decimal," + RATE + " decimal, " + TIME_PLACED + " timestamp, " + ORINGINATING_COUNTRY + " text, "
                + "PRIMARY KEY (" + ID + "," + TIME_PLACED + ")" + ");");

    }

    /**
     * {@inheritDoc} 
     */
    @Override
    public ResultSet executeQuery(Statement query) {
        return session.execute(query);
    }

    void querySchema() {
        ResultSet rs = session.execute("select release_version from system.local");
        Row row = rs.one();

        LOGGER.info("Cassandra version: {}.", row.getString("release_version"));
    }

    @PreDestroy
    public synchronized void close() {

        if (connected) {
            session.close();
            cluster.close();
        }
    }

}
