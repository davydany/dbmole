package dbmole.connectors;

import dbmole.base.Base;
import dbmole.examiner.NetworkExaminer;
import picocli.CommandLine;

import java.io.File;
import java.sql.Connection;
import java.util.concurrent.Callable;

public abstract class BaseConnector extends Base implements Callable<Void> {

    // System Properties -----------------------------------------------------------------------------------------------
    protected final String KEYSTORE_KEY = "javax.net.ssl.keyStore";

    protected final String KEYSTORE_PASSWORD_KEY = "javax.net.ssl.keyStorePassword";

    protected final String KEYSTORE_TYPE_KEY = "javax.net.ssl.keyStoreType";

    protected final String TRUSTSTORE_KEY = "javax.net.ssl.trustStore";

    protected final String TRUSTSTORE_PASSWORD_KEY = "javax.net.ssl.trustStorePassword";

    protected final String TRUSTSTORE_TYPE_KEY = "javax.net.ssl.trustStoreType";

    // CLI Inputs ------------------------------------------------------------------------------------------------------
    @CommandLine.Parameters(paramLabel="HOSTNAME", index="0", description="Hostname of Database Server")
    protected String host = null;

    @CommandLine.Parameters(paramLabel="PORT", index="1", description="Port on which the database is listening on")
    protected int port = 0;

    @CommandLine.Option(names={"-u", "--username"}, description="Username to connect to the database.")
    protected String username = null;

    @CommandLine.Option(names={"-p", "--password"}, description="Password to connect to the database.")
    protected String password = null;

    @CommandLine.Option(names={"-d", "--database"}, description="The database (or service name) to connect to.")
    protected String database = null;

    @CommandLine.Option(names={"-q", "--query"}, description="The query to run if the connection was successful.")
    protected String query = null;

    @CommandLine.Option(names="--skip-auto-debug", description = "Skips the automatic debugging process.")
    protected boolean skip_auto_debug = false;

    @CommandLine.Option(names={ "--keyStorePath" }, description="Path to the Keystore")
    protected File keyStore;

    @CommandLine.Option(names={ "--keyStorePassword" }, description="Password for Keystore")
    protected String keyStorePassword;

    @CommandLine.Option(names={ "--keyStoreType" }, description="Keystore's Type")
    protected String keyStoreType;

    @CommandLine.Option(names={ "--trustStorePath" }, description="Path to the Truststore")
    protected File trustStore;

    @CommandLine.Option(names={ "--trustStorePassword" }, description="Password for the Truststore")
    protected String trustStorePassword;

    @CommandLine.Option(names={ "--trustStoreType" }, description="Truststore's Type")
    protected String trustStoreType;

    protected boolean isConnectionSuccessful = false;

    protected Exception connectionException = null;

    // Constructors ----------------------------------------------------------------------------------------------------
    public BaseConnector() {

    }

    // Getters ---------------------------------------------------------------------------------------------------------
    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDatabase() {
        return database;
    }

    public abstract boolean isSecure();

    public void setConnectionException(Exception ex) {
        this.connectionException = ex;
    }

    // Hooks to override -----------------------------------------------------------------------------------------------
    public void initialize() {

        this.setupCustomStores();
        this.displayCustomStoresDetails();
    }

    public void beforeConnect() {

    }

    public void afterConnectSuccess() {

    }

    public void afterConnectFailure() {

    }

    // Logic for Keystores / Truststores -------------------------------------------------------------------------------
    protected void setupCustomStores() {
        if(keyStore != null) {
            String keyStorePath = keyStore.getAbsolutePath();
            if(this.keyStore.exists()) {
                logger.info(String.format("Setting '%s' with '%s'", KEYSTORE_KEY, keyStorePath));
                System.setProperty(KEYSTORE_KEY, keyStorePath);
            } else {
                logger.error(String.format("Keystore Path '%s' does not exist. Not setting.", keyStorePath, KEYSTORE_KEY));
            }
        }

        if(keyStorePassword != null) {
            logger.info(String.format("Setting '%s' with '%s'", KEYSTORE_PASSWORD_KEY, keyStorePassword));
            System.setProperty(KEYSTORE_PASSWORD_KEY, keyStorePassword);
        }

        if(keyStoreType != null) {
            logger.info(String.format("Setting '%s' with '%s'", KEYSTORE_TYPE_KEY, keyStoreType));
            System.setProperty(KEYSTORE_TYPE_KEY, keyStoreType);
        }

        if(trustStore != null) {
            String trustStorePath = this.trustStore.getAbsolutePath();
            if(this.trustStore.exists()) {
                logger.info(String.format("Setting '%s' with '%s'", TRUSTSTORE_KEY, trustStorePath));
                System.setProperty(TRUSTSTORE_KEY, trustStorePath);
            } else {
                logger.error(String.format("Truststore Path '%s' does not exist. Not setting '%s'.", trustStorePath, TRUSTSTORE_KEY));
            }
        }

        if(trustStorePassword != null) {
            logger.info(String.format("Setting '%s' with '%s'", TRUSTSTORE_PASSWORD_KEY, trustStorePassword));
            System.setProperty(TRUSTSTORE_PASSWORD_KEY, trustStorePassword);
        }

        if(trustStoreType != null) {
            logger.info(String.format("Setting '%s' with '%s'", TRUSTSTORE_TYPE_KEY, trustStoreType));
            System.setProperty(TRUSTSTORE_TYPE_KEY, trustStoreType);
        }
    }

    protected void displayCustomStoresDetails() {
        logger.info("Custom Stores' Values: ");
        logger.info(String.format("    * %s --> %s", KEYSTORE_KEY, System.getProperty(KEYSTORE_KEY)));
        logger.info(String.format("    * %s --> %s", KEYSTORE_PASSWORD_KEY, System.getProperty(KEYSTORE_PASSWORD_KEY)));
        logger.info(String.format("    * %s --> %s", KEYSTORE_TYPE_KEY, System.getProperty(KEYSTORE_TYPE_KEY)));
        logger.info(String.format("    * %s --> %s", TRUSTSTORE_KEY, System.getProperty(TRUSTSTORE_KEY)));
        logger.info(String.format("    * %s --> %s", TRUSTSTORE_PASSWORD_KEY, System.getProperty(TRUSTSTORE_PASSWORD_KEY)));
        logger.info(String.format("    * %s --> %s", TRUSTSTORE_TYPE_KEY, System.getProperty(TRUSTSTORE_TYPE_KEY)));
    }


    @Override
    public Void call() throws Exception {
        logger.info(String.format("Initializing Connector: %s", this.getClass().getName()));
        this.initialize();
        this.beforeConnect();
        try {
            this.createConnection();
            this.isConnectionSuccessful = true;
            this.makeQuery();
            this.afterConnectSuccess();
        } catch (Exception ex) {
            this.isConnectionSuccessful = false;
            this.connectionException = ex;
            this.afterConnectFailure();
        }

        if (isConnectionSuccessful) {
            logger.info("The connection to the database was successful!");
        } else {
            logger.info("The connection to the database was NOT successful.");
            if (skip_auto_debug) {
                logger.info("Skipping automatic debugging.");
            } else {
                logger.info("Initializing Automatic Debugging.");
                this.verifyConnectivity();
            }

            // show the exception that was encountered while connecting
            logger.error(String.format("Here is the exception that was encountered.", this.connectionException.getMessage()));
            this.connectionException.printStackTrace();

        }
        return null;
    }

    /**
     * Verifies network connectivity.
     */
    private void verifyConnectivity() {

        NetworkExaminer networkExaminer = this.getNetworkExaminer();
        String host = networkExaminer.getHost();
        String resolvedHost = networkExaminer.getResolvedHost();
        boolean isResolvedHostDifferent = !resolvedHost.equals(networkExaminer.getHost());

        // determine what the host resolves to
        if (isResolvedHostDifferent) {
            logger.info(
                    String.format(
                            "Hostname '%s' has been resolved to '%s'",
                            networkExaminer.getHost(),
                            networkExaminer.getResolvedHost()));
        }

        // can we ping the host and see if the host exists?
        if (isResolvedHostDifferent) {
            networkExaminer.sendPingRequest(host);
            networkExaminer.sendPingRequest(resolvedHost);
        } else {
            networkExaminer.sendPingRequest(host);
        }

        // can we see if the port on the server is listening
        if (isResolvedHostDifferent) {
            networkExaminer.isPortAvailableForTCP(host);
            networkExaminer.isPortAvailableForTCP(resolvedHost);
        } else {
            networkExaminer.isPortAvailableForTCP(host);
        }
    }

    // Examiner Methods --------------------------------------------------------------------------------------------------
    public NetworkExaminer getNetworkExaminer() {
        return new NetworkExaminer(this.host, this.port);
    }

    // ABSTRACT METHODS ------------------------------------------------------------------------------------------------
    public abstract String generateConnectionString();

    public abstract Object createConnection() throws Exception;

    public abstract void makeQuery();
}
