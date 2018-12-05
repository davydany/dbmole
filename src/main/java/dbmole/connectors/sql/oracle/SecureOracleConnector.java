package dbmole.connectors.sql.oracle;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name="oracle_tls", description = "Checks connectivity to an Oracle Database over TLS")
public class SecureOracleConnector extends OracleConnector {

    public String generateConnectionString() {

        OracleStringBuilder connectionStringGenerator = new OracleStringBuilder(
                this.getHost(),
                this.getDatabase(),
                this.getPort(),
                true
        );
        try {
            return connectionStringGenerator.stringify();
        } catch (Exception ex) {
            logger.error("Unable to generate Connection String.");
            ex.printStackTrace();
            return "";
        }
    }

    public boolean isSecure() {
        return true;
    }
}
