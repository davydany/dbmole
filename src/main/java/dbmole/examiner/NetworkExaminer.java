package dbmole.examiner;

import dbmole.base.Base;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class NetworkExaminer extends Base {


    protected String _host = null;

    protected String _resolvedHost = null;

    protected int _port = 0;

    public NetworkExaminer(String host, int port) {
        this._host = host;
        this._port = port;
        this._resolvedHost = this.resolveHostAsIPv4Address();
    }

    public String resolveHostAsIPv4Address() {
        InetAddress address = null;
        try {
            address = InetAddress.getByName(this._host);
            return address.getHostAddress();
        } catch (UnknownHostException e) {
            return null;
        }
    }

    public boolean sendPingRequest(String host) {
        try {
            InetAddress address = InetAddress.getByName(host);
            boolean isReachable = address.isReachable(5000);
            if (isReachable) {
                logger.info(String.format("Ping request to '%s' was successful.", host));
            } else {
                logger.info(String.format("Ping request to '%s' was unsuccessful.", host));
            }
            return isReachable;
        } catch (IOException e) {
            logger.error(String.format("Unable to ping '%s' because of '%s'", host, e.getMessage()));
            return false;
        }
    }

    public boolean isPortAvailableForTCP(String host) {
        try {
            Socket socket = new Socket(host, this._port);
            socket.close();
            logger.info(String.format("The Host '%s' is available on Port '%s'", host, this._port));
            return true;
        } catch (Exception e) {
            logger.error(String.format("The Host '%s' is NOT available on Port '%s'", host, this._port));
            return false;
        }
    }

    public void traceRoute(String host) {
        // TODO: write up the code for traceroute
        // More information here: https://github.com/mgodave/Jpcap/blob/master/sample/Traceroute.java
    }

    // Getters ---------------------------------------------------------------------------------------------------------
    public String getHost() {
        return _host;
    }

    public String getResolvedHost() {
        return _resolvedHost;
    }

    public int getPort() {
        return _port;
    }
}
