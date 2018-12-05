package dbmole.connectors;

import dbmole.base.Base;
import dbmole.examiner.NetworkExaminer;
import picocli.CommandLine;

import java.sql.Connection;
import java.util.concurrent.Callable;

public abstract class BaseConnector extends Base implements Callable<Void> {

    // Public Static Variables -----------------------------------------------------------------------------------------
    public final static String USERNAME = "username";

    public final static String PASSWORD = "password";

    public final static String DATABASE = "database";

    public final static String DEBUG = "debug";

    public final static String DB_CONNECTION_TIMEOUT = "db_conn_timeout";

    public final static String SECURE = "secure";

    // Protected Member Variables --------------------------------------------------------------------------------------
    @CommandLine.Parameters(paramLabel="HOSTNAME", index="0", description="Hostname of Database Server")
    protected String host = null;

    @CommandLine.Parameters(paramLabel="PORT", index="1", description="Port on which the database is listening on")
    protected int port = 0;

    @CommandLine.Option(names={"-u", "--username"}, description="Username to connect to the database.")
    protected String username = "";

    @CommandLine.Option(names={"-p", "--password"}, description="Password to connect to the database.")
    protected String password = "";

    @CommandLine.Option(names={"-d", "--database"}, description="The database (or service name) to connect to.")
    protected String database = "";

    @CommandLine.Option(names={"-q", "--query"}, description="The query to run if the connection was successful.")
    protected String query = null;

    @CommandLine.Option(names="--skip-auto-debug", description = "Skips the automatic debugging process.")
    protected boolean skip_auto_debug = false;

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

    public void initialize() {

    }

    public void beforeConnect() {

    }

    public void afterConnectSuccess() {

    }

    public void afterConnectFailure() {

    }

    @Override
    public Void call() throws Exception {
        logger.info(String.format("Initializing Connector: %s", this.getClass().getName()));
        this.initialize();
        this.beforeConnect();
        try {
            this.createConnection();
            this.isConnectionSuccessful = true;
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


    // Utility Methods -------------------------------------------------------------------------------------------------
    public boolean isConnectionSuccessful() {
        return this.isConnectionSuccessful;
    }

    // Examiner Methods --------------------------------------------------------------------------------------------------
    public NetworkExaminer getNetworkExaminer() {
        return new NetworkExaminer(this.host, this.port);
    }

    // ABSTRACT METHODS ------------------------------------------------------------------------------------------------
    public abstract String generateConnectionString();

    public abstract Connection createConnection() throws Exception;
}
