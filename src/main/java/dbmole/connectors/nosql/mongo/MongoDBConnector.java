package dbmole.connectors.nosql.mongo;

import com.mongodb.*;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import dbmole.connectors.BaseConnector;
import org.bson.Document;
import picocli.CommandLine;

import java.util.Arrays;

public class MongoDBConnector extends BaseConnector {


    @CommandLine.Option(names="--secure", description = "Specifies if the connection should be secure.")
    protected boolean secure = false;

    @CommandLine.Option(names="--invalid-hostname-allowed", description="Skips invalid hostname validation.")
    protected boolean invalidHostnameAllowed = false;

    @CommandLine.Option(names={"-c", "--collection"}, description="The collection to query")
    protected String collection = null;

    @Override
    public boolean isSecure() {
        return this.secure;
    }

    @Override
    public String generateConnectionString() {
        return null;
    }

    @Override
    public Object createConnection() throws Exception {

        MongoClientOptions options = MongoClientOptions.builder()
            .sslEnabled(this.secure)
            .sslInvalidHostNameAllowed(this.invalidHostnameAllowed)
            .build();

        // setup authentication
        MongoClient client = null;
        if (this.username != null) {
            MongoCredential credentials = MongoCredential
                    .createCredential(this.username, this.database, this.password.toCharArray());
            client = new MongoClient(
                    new ServerAddress(this.host, this.port),
                    Arrays.asList(credentials));

        } else {
            client = new MongoClient(
                    new ServerAddress(this.host, this.port));
        }
        return client;
    }

    @Override
    public void makeQuery() {

        if ((this.collection != null) && (this.query != null)) {
            try {
                MongoClient client = (MongoClient) this.createConnection();
                MongoDatabase database = client.getDatabase(this.database);
                MongoCollection collection = database.getCollection(this.collection);
                BasicDBObject queryObject = BasicDBObject.parse(this.query);
                FindIterable<Document> results = collection.find(queryObject);

                int rowCount = 0;
                for (Document doc : results) {
                    System.out.println(doc.toJson());
                    rowCount += 1;
                }

                logger.info(String.format("Query Resulted in '%s' Results", rowCount));
            } catch (Exception ex) {
                logger.error("Unable to run query: " + query);
                ex.printStackTrace();
            }
        } else {
            logger.error("Both Collection (--collection) and Query (--query) must be provided to perform a query.");
        }
    }
}
