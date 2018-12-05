package dbmole.connectors.oracle;

import picocli.CommandLine;

import java.io.File;

@CommandLine.Command(name="oracle_tls", description = "Checks connectivity to an Oracle Database over TLS")
public class SecureOracleConnector extends OracleConnector {

    protected final String KEYSTORE_KEY = "javax.net.ssl.keyStore";

    protected final String KEYSTORE_PASSWORD_KEY = "javax.net.ssl.keyStorePassword";

    protected final String KEYSTORE_TYPE_KEY = "javax.net.ssl.keyStoreType";

    protected final String TRUSTSTORE_KEY = "javax.net.ssl.trustStore";

    protected final String TRUSTSTORE_PASSWORD_KEY = "javax.net.ssl.trustStorePassword";

    protected final String TRUSTSTORE_TYPE_KEY = "javax.net.ssl.trustStoreType";

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

    public SecureOracleConnector() {

        super();
    }

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

    public void initialize() {
        this.setupCustomStores();
        this.displayCustomStoresDetails();
    }

    public boolean isSecure() {
        return true;
    }
}
