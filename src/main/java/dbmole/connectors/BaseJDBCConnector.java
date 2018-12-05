package dbmole.connectors;

import java.sql.*;
import java.util.Properties;

public abstract class BaseJDBCConnector extends BaseConnector {

    public BaseJDBCConnector() {

    }

    public Object createJDBCConnection(String connectionString, Properties props) throws Exception {
        return DriverManager.getConnection(connectionString, props);
    }

    public abstract String generateConnectionString();

    @Override
    public void afterConnectSuccess() {

        if (this.query != null) {
            try {
                Connection conn = this.createConnection();
                Statement statement = conn.createStatement();
                ResultSet rs = statement.executeQuery(this.query);
                ResultSetMetaData metadata = rs.getMetaData();
                int columnCount = metadata.getColumnCount();

                // print the headers
                for (int i = 1; i <= columnCount; i++) {
                    System.out.println(metadata.getColumnName(i) + " | ");
                }

                // print each column for each row
                int rowCount = 0;
                while (rs.next()) {
                    rowCount += 1;
                    String row = "";
                    for (int i = 1; i <= columnCount; i++) {
                        row += rs.getString(i) + " | ";
                    }
                    System.out.println(row);
                }

                logger.info(String.format("Query Resulted in '%s' Rows", rowCount));
            } catch (Exception ex) {
                logger.error("Unable to run query: " + query);
                ex.printStackTrace();
            }
        }
    }

    //    @Override
//    public void displayDebugInformation() {
//        super.displayDebugInformation();
//        logger.info(String.format("    * Connection String: %s", this.generateConnectionString()));
//    }
}
