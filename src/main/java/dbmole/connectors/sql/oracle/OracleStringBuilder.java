package dbmole.connectors.sql.oracle;

import dbmole.base.Base;

public class OracleStringBuilder extends Base {

    private String protocol = null;

    private String port = "1521";

    private String host = null;

    private String serviceName = null;

    private String SSLServerCertDN = null;

    OracleStringBuilder(String host, String serviceName) {
        this.host = host;
        this.serviceName = serviceName;
    }

    OracleStringBuilder(String host, String serviceName, int port, boolean isSecure) {
        this.host = host;
        this.serviceName = serviceName;
        this.port = String.valueOf(port);
        this.setProtocol(isSecure);
    }

    public String getProtocol() {
        return String.format("(PROTOCOL=%s)", this.protocol);
    }

    public void setProtocol(boolean isSecure) {
        if (isSecure) {
            this.protocol = "TCPS";
        } else {
            this.protocol = "TCP";
        }
    }

    public String getPort() {
        return String.format("(PORT=%s)", this.port);
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getHost() {
        return String.format("(HOST=%s)", this.host);
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getServiceName() {
        return String.format("(SERVICE_NAME=%s)", this.serviceName);
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSSLServerCertDN() {
        return "(ssl_server_cert_dn=\"" + this.SSLServerCertDN + "\")";
    }

    public void setSSLServerCertDN(String ssl_server_cert_dn) {
        this.SSLServerCertDN = ssl_server_cert_dn;
    }

    public String stringify() throws Exception {

        // check if everything is in order
        if (this.host == null) {
            logger.error("Host was not provided.");
            throw new Exception("Host not provided.");
        }
        if (this.serviceName == null) {
            logger.error("Service Name / Database was not provided.");
            throw new Exception("Service Name / Database not provided.");
        }

        // collect details we need
        StringBuilder builder = new StringBuilder();
        builder.append("jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=");
        builder.append(this.getProtocol());
        builder.append(this.getPort());
        builder.append(this.getHost());
        builder.append(")(CONNECT_DATA=");
        builder.append(this.getServiceName());
        builder.append(")");

        // add optional fields
        if (this.SSLServerCertDN != null) {
            builder.append("(SECURITY=");
            builder.append(this.getSSLServerCertDN());
            builder.append(")");
        }
        builder.append(")");

        return builder.toString();
    }
}
