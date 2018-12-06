package dbmole;

import dbmole.cli.CLILogger;
import dbmole.connectors.nosql.mongo.MongoDBConnector;
import dbmole.connectors.sql.mariadb.MariaDBConnector;
import dbmole.connectors.sql.mysql.MySQLDBConnector;
import dbmole.connectors.sql.oracle.OracleConnector;
import dbmole.connectors.sql.oracle.SecureOracleConnector;
//import org.apache.commons.cli.*;
//import org.apache.commons.cli.ParseException;
import dbmole.connectors.sql.postgresql.PostgreSQLConnector;
import dbmole.licence.LicenseManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;

import java.io.File;
import java.util.concurrent.*;

/**
 * DBMoleApp is a Database Connection testing utility.
 */
@CommandLine.Command(subcommands = {
    MariaDBConnector.class,
    MongoDBConnector.class,
    MySQLDBConnector.class,
    OracleConnector.class,
    SecureOracleConnector.class,
    PostgreSQLConnector.class
})
public class DBMoleApp implements Callable<Void>
{
    public static final String NAME = "DBConnect";

    private static final CLILogger logger = CLILogger.getInstance();

    private static String[] args = null;

    public static void main( String[] args ) {

        DBMoleApp.args = args;
        CommandLine.call(new DBMoleApp(), args);
    }

    @Override
    public Void call() throws Exception {

        File jarFile = new java.io.File(DBMoleApp.class.getProtectionDomain()
                .getCodeSource()
                .getLocation()
                .getPath());
        String jarFileName = jarFile.getName();

        String msg = String.format("Usage: java -jar ./%s --help", jarFileName);
        logger.info(msg);
        return null;
    }
}
