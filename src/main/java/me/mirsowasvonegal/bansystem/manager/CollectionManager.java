package me.mirsowasvonegal.bansystem.manager;


import com.google.gson.Gson;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CollectionManager {


    private MongoCollection<Document> collection;
    private Gson gson;


    public CollectionManager(MongoCollection<Document> collection) {
        this.collection = collection;
        gson = new Gson();
    }

    public <T> void add(T target){
        Document document = Document.parse(gson.toJson(target));
        collection.insertOne(document);
    }

    public <T> ArrayList<T> findAll(Class classOfT){
        ArrayList<T> classArray = new ArrayList<>();
        for (Document document : collection.find()) {
            T t = gson.fromJson(document.toJson(), (Type) classOfT);
            classArray.add(t);
        }
        return classArray;
    }

    public long count(Bson filter){
        return collection.countDocuments(filter);
    }

    public long count(){
        return collection.countDocuments();
    }

    public <T> T find(Bson filter, Class classOfT){
        Document document;
        try {
            document = collection.find(filter).first();
        } catch (NoClassDefFoundError error) {
            document = collection.find(filter).first();
        }
        if(document == null) return null;
        T t = gson.fromJson(document.toJson(), (Type) classOfT);
        return t;
    }

    public void delete(Bson filter){
        collection.deleteOne(filter);
    }

    public <T> UpdateResult update(Bson filter, T update){
        Document updateDocument = Document.parse(gson.toJson(update));
        return collection.updateOne(filter, new Document("$set", updateDocument));
    }

}
