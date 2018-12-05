package dbmole.connectors.sql.oracle;

import dbmole.connectors.sql.BaseJDBCConnector;
import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;
import picocli.CommandLine;

import java.sql.Connection;
import java.util.Properties;


@CommandLine.Command(name="oracle", description="Checks connectivity to an Oracle Database.")
public class OracleConnector extends BaseJDBCConnector {

    public OracleConnector() {
        super();
        this.loadDriver();
    }

    private void loadDriver() {

        // load the OracleDriver
        try {

            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            logger.error("Unable to initialize the OracleDriver to test connectivity. This is a problem with this JAR file.");
            System.exit(1);
        }

    }

    public String generateConnectionString() {

        // use the OracleStringBuilder
        OracleStringBuilder builder = new OracleStringBuilder(
            this.getHost(),
            this.getDatabase(),
            this.getPort(),
            this.isSecure()
        );
        try {
            return builder.stringify();
        } catch (Exception ex) {
            logger.error("Unable to generate Connection String.");
            ex.printStackTrace();
            return "";
        }
    }

    public Properties getConnectionProperties() {

        final String DB_USER = this.getUsername();
        final String DB_PASSWORD = this.getPassword();

        Properties info = new Properties();
        info.put(OracleConnection.CONNECTION_PROPERTY_USER_NAME, DB_USER);
        info.put(OracleConnection.CONNECTION_PROPERTY_PASSWORD, DB_PASSWORD);
        info.put(OracleConnection.CONNECTION_PROPERTY_DEFAULT_ROW_PREFETCH, "20");
        return info;
    }


    public Connection createConnection() throws Exception {

        String connectionString = this.generateConnectionString();
        Properties info = this.getConnectionProperties();

        logger.info("Creating connection with connection string: " + connectionString);
        logger.info("Properties for connection: " + info.toString());

        // create the connection
        OracleDataSource ods = new OracleDataSource();
        ods.setConnectionProperties(info);
        ods.setURL(connectionString);

        return ods.getConnection();
    }

    public boolean isSecure() {
        return false;
    }
}
