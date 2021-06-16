package me.mirsowasvonegal.bansystem.manager;

import com.mongodb.client.model.Filters;
import me.mirsowasvonegal.bansystem.BanSystem;
import me.mirsowasvonegal.bansystem.data.Ban;
import me.mirsowasvonegal.bansystem.data.Reason;
import me.mirsowasvonegal.bansystem.data.Type;
import me.mirsowasvonegal.bansystem.data.User;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class ReasonManager {

    CollectionManager collectionManager;

    public ReasonManager() {
        collectionManager = new CollectionManager(BanSystem.getMongoManger().getReasonCollection());
    }

    public void addReason(String name, long time, Type type, String perms) {
        int reasonId = (int) (collectionManager.count() + 1);
        Reason reason = new Reason(reasonId, name, time+"", type, perms);
        collectionManager.add(reason);
    }

    public Boolean addReason(int reasonId, String name, long time, Type type, String perms) {
        Reason reason = getReason(reasonId);
        if (reason == null) {
            reason = new Reason(reasonId, name, time+"", type, perms);
            collectionManager.add(reason);
            return true;
        } else {
            return false;
        }
    }

    public void removeReason(int reasonId) {
        collectionManager.delete(Filters.eq("reasonId", reasonId));
    }

    public ArrayList<Reason> getAllReasons() {
        return collectionManager.findAll(Reason.class);
    }

    public Reason getReason(int reasonId) {
        return collectionManager.find(Filters.eq("reasonId", reasonId), Reason.class);
    }

}
