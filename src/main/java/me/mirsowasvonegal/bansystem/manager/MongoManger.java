package me.mirsowasvonegal.bansystem.manager;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;

import java.util.Collections;

@Getter
public class MongoManger {

    @Getter
    private MongoClient mongoClient;
    @Getter
    private MongoDatabase mongoDatabase;
    @Getter
    private MongoCollection<Document> banCollection;
    @Getter
    private MongoCollection<Document> reasonCollection;

    String username;
    String database;
    String password;
    String host;

    public MongoManger(String username, String database, String password, String host) {
        this.username = username;
        this.database = database;
        this.password = password;
        this.host = host;
    }

    public void connect() {
        MongoCredential mongoCredential = MongoCredential.createCredential(username, database, password.toCharArray());
        mongoClient = new MongoClient(new ServerAddress(host, 27017), Collections.singletonList(mongoCredential));
        mongoDatabase = mongoClient.getDatabase(database);
        banCollection = mongoDatabase.getCollection("bans");
        reasonCollection = mongoDatabase.getCollection("reasons");
    }



}
