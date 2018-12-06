package dbmole.connectors.sql.mariadb;

import dbmole.connectors.sql.BaseJDBCConnector;
import picocli.CommandLine;

import java.util.Properties;

@CommandLine.Command(name="mariadb", description = "Checks connectivity to a MariaDB Database")
public class MariaDBConnector extends BaseJDBCConnector {

    @Override
    public boolean isSecure() {
        return false;
    }

    @Override
    public String generateConnectionString() {

        StringBuilder builder = new StringBuilder();

        // add the host and port
        builder.append("jdbc:mariadb://");
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

    @Override
    public void loadDriver() {
        try {
            Class.forName("org.mariadb.jdbc.Driver");
        } catch (ClassNotFoundException ex) {
            logger.error("Unable to initialize the MariaDB Driver to test connectivity. This is a problem with this JAR file.");
            System.exit(1);
        }
    }
}
