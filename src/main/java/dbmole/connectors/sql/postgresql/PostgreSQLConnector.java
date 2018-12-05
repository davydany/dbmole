package dbmole.connectors.sql.postgresql;

import dbmole.connectors.sql.BaseJDBCConnector;
import picocli.CommandLine;

import java.sql.Connection;
import java.util.Properties;

@CommandLine.Command(name="postgresql", description = "Checks connectivity to an PostgreSQL Database")
public class PostgreSQLConnector extends BaseJDBCConnector {

    PostgreSQLConnector() {
        this.loadDriver();
    }

    @Override
    public boolean isSecure() {
        return false;
    }

    private void loadDriver() {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            logger.error("Unable to initialize the PostgreSQL Driver to test connectivity. This is a problem with this JAR file.");
            System.exit(1);
        }
    }

//    public abstract String generateConnectionString();
//
//    public abstract Properties getConnectionProperties();

    public String generateConnectionString() {

        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:postgresql://");

        // add the host and port
        builder.append(host);
        builder.append(":");
        builder.append(port);

        // add the database
        if (database != null) {
            builder.append("/");
            builder.append(database);
        }

        return builder.toString();
    }

    @Override
    public Properties getConnectionProperties() {

        Properties props = new Properties();
        props.setProperty("user", this.username);
        props.setProperty("password", this.password);
        return props;
    }

}
