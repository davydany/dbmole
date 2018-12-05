package dbmole;

import dbmole.connectors.oracle.OracleConnector;
import dbmole.connectors.oracle.SecureOracleConnector;
//import org.apache.commons.cli.*;
//import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.*;

/**
 * DBMoleApp is a Database Connection testing utility.
 */
@CommandLine.Command(subcommands = {
    OracleConnector.class,
    SecureOracleConnector.class
})
public class DBMoleApp implements Callable<Void>
{
    public static final String NAME = "DBConnect";

    private static final Logger logger = LogManager.getLogger(DBMoleApp.class);

    private static String[] args = null;

    public static void main( String[] args ) {

        DBMoleApp.args = args;
        CommandLine.call(new DBMoleApp(), args);

//        DBMoleApp DBMoleApp = new DBMoleApp();
//        CommandLine cmd = DBMoleApp.parseArguments(args);

//        // determine what type of connection we will be making
//        String host = cmd.getOptionValue("host");
//        int port = Integer.parseInt(cmd.getOptionValue("port"));
//        String database = cmd.getOptionValue("database");
//        boolean secure = cmd.hasOption("secure");
//        String username = cmd.getOptionValue("username");
//        String password = cmd.getOptionValue("password");
//        String dbType = cmd.getOptionValue("dbType");
//        String timeout = cmd.getOptionValue("timeout", "5");
//        boolean debug = cmd.hasOption("debug");
//
//        // compile additional details to send to the connector
//        Map<String, Object> connectionDetails = new HashMap<>();
//        connectionDetails.put(BaseConnector.USERNAME, username);
//        connectionDetails.put(BaseConnector.PASSWORD, password);
//        connectionDetails.put(BaseConnector.DATABASE, database);
//        connectionDetails.put(BaseConnector.DEBUG, debug);
//        connectionDetails.put(BaseConnector.DB_CONNECTION_TIMEOUT, timeout);
//        connectionDetails.put(BaseConnector.SECURE, secure);
//
//        BaseConnector connector = null;
//        if (dbType.equals("oracle")) {
//            if (secure) {
//                connector = new SecureOracleConnector(host, port, connectionDetails);
//            } else {
//                connector = new OracleConnector(host, port, connectionDetails);
//            }
//        } else {
//            logger.error("Invalid database type provided. " + dbType + " is not a valid database type.");
//            System.exit(1);
//        }
//
//        // show debug information
//        if (debug) {
//            logger.info(String.format("%s is in DEBUG MODE", DBMoleApp.NAME));
//            logger.info("    * Connector Type: " + connector.getClass());
//            // connector.displayDebugInformation();
//        }
//
//        // attempt connection until specified timeout expires
//        ExecutorService executorService = Executors.newSingleThreadExecutor();
//        Future future = executorService.submit(connector);
//        int timeoutSecs = Integer.parseInt(timeout);
//
//        try {
//            if (connector.isSecure()) {
//                logger.info("Establishing a Secure Connection to Database...");
//            } else {
//                logger.info("Establishing Connection to Database...");
//            }
//            future.get(timeoutSecs, TimeUnit.SECONDS);
//            logger.info(String.format("Connection was established in '%s seconds'", timeoutSecs));
//
//        } catch (InterruptedException e) {
//            connector.setConnectionException(e);
//        } catch (ExecutionException e) {
//            connector.setConnectionException(e);
//        } catch (TimeoutException e) {
//            connector.setConnectionException(e);
//            logger.error(String.format("Unable to establish a connection in '%s seconds'", timeoutSecs));
//        } finally {
//            future.cancel(true);
//            executorService.shutdownNow();
//        }
//
//        // determine if the connection was successful
//        if (connector.isConnectionSuccessful()) {
//            logger.info("Connection was Successful");
//            return;
//        } else {
//            logger.info("Unable to connect to server '" + host + "'");
//        }
//
//        // if the connection was not successful, let's try to figure out why
//        logger.info("Beginning Debug Process...");
//        DBMoleApp.verifyConnectivity(connector);
    }

//    private void verifyConnectivity(BaseConnector connector) {
//
//        NetworkExaminer networkExaminer = connector.getNetworkExaminer();
//        String host = networkExaminer.getHost();
//        String resolvedHost = networkExaminer.getResolvedHost();
//        boolean isResolvedHostDifferent = !resolvedHost.equals(networkExaminer.getHost());
//
//        // determine what the host resolves to
//        if (isResolvedHostDifferent) {
//            logger.info(
//                    String.format(
//                            "Hostname '%s' has been resolved to '%s'",
//                            networkExaminer.getHost(),
//                            networkExaminer.getResolvedHost()));
//        }
//
//        // can we ping the host and see if the host exists?
//        if (isResolvedHostDifferent) {
//            networkExaminer.sendPingRequest(host);
//            networkExaminer.sendPingRequest(resolvedHost);
//        } else {
//            networkExaminer.sendPingRequest(host);
//        }
//
//        // can we see if the port on the server is listening
//        if (isResolvedHostDifferent) {
//            networkExaminer.isPortAvailableForTCP(host);
//            networkExaminer.isPortAvailableForTCP(resolvedHost);
//        } else {
//            networkExaminer.isPortAvailableForTCP(host);
//        }
//    }


//    private CommandLine parseArguments(String[] args) {
//        Options opts = new Options();
//
//        // option for debug mode
//        Option _debugMode = new Option("debug", false, String.format("Sets %s into debug mode.", DBMoleApp.NAME));
//        _debugMode.setRequired(false);
//        opts.addOption(_debugMode);
//
//        // option for databasetype
//        Option _dbtype = new Option("d", "dbType", true, "Type of Database: oracle");
//        _dbtype.setRequired(true);
//        opts.addOption(_dbtype);
//
//        // option for host
//        Option _host = new Option("h", "host", true, "hostname to connect to.");
//        _host.setRequired(true);
//        opts.addOption(_host);
//
//        // option for port
//        Option _hostPort = new Option("p", "port", true, "port to connect on the host.");
//        _hostPort.setRequired(true);
//        opts.addOption(_hostPort);
//
//        // option for secure
//        Option _secureConnection = new Option("s", "secure", false, "Is the connection secure?");
//        _secureConnection.setRequired(false);
//        opts.addOption(_secureConnection);
//
//        // option for service name
//        Option _db = new Option("b", "database", true, "Database to connect to.");
//        _db.setRequired(true);
//        opts.addOption(_db);
//
//        // option for username
//        Option _username = new Option("u", "username", true, "Username for Connection to Database");
//        _username.setRequired(true);
//        opts.addOption(_username);
//
//        // option for password
//        Option _password = new Option("w", "password", true, "Password for Connection to Database");
//        _password.setRequired(true);
//        opts.addOption(_password);
//
//        // option for session timeout
//        Option _timeoutSecs = new Option("timeout", true, "How many seconds to wait for database connection before debugging begins. Default: 5 seconds");
//        _timeoutSecs.setRequired(false);
//        opts.addOption(_timeoutSecs);
//
//        // setup parser, parse and return with outputs
//        HelpFormatter formatter = new HelpFormatter();
//        CommandLineParser parser = new DefaultParser();
//        CommandLine cmd = null;
//        try {
//            cmd = parser.parse(opts, args);
//            return cmd;
//        } catch (ParseException ex) {
//            System.out.println(ex.getMessage());
//            formatter.printHelp("jtnsping", opts);
//            System.exit(1);
//        }
//        return null;
//    }

    @Override
    public Void call() throws Exception {

        File jarFile = new java.io.File(DBMoleApp.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());
        String jarFileName = jarFile.getName();
//        StringBuilder sb = new StringBuilder();
//        for (String s : DBMoleApp.args) {
//            sb.append(s);
//            sb.append(" ");
//        }
        String msg = String.format("Usage: java -jar ./%s --help", jarFileName);
        System.out.println(msg);
//        logger.info(String.format("Please call with '--help' to see what '%s' can really do!", DBMoleApp.NAME));
        return null;
    }
}
