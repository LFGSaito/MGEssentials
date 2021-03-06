package com.minegusta.mgessentials.pvplog;

import com.google.common.collect.Maps;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

public class LogData {
    public static ConcurrentMap<String, PVPBot> logMap = Maps.newConcurrentMap();
    public static ConcurrentMap<String, Boolean> bots = Maps.newConcurrentMap();
    public static ConcurrentMap<String, List<Chunk>> chunkMap = Maps.newConcurrentMap();


    public static void add(Player p, PVPBot bot) {
        logMap.put(p.getUniqueId().toString(), bot);
    }

    public static void remove(Player p) {
        if (logMap.containsKey(p.getUniqueId().toString())) logMap.remove(p.getUniqueId().toString());
    }

    public static void remove(UUID uuid) {
        if (logMap.containsKey(uuid.toString())) logMap.remove(uuid.toString());
    }

    public static PVPBot get(Player p) {
        return logMap.get(p.getUniqueId().toString());
    }

    public static boolean contains(Player p) {
        return logMap.containsKey(p.getUniqueId().toString());
    }

    public static int remainingTime(Player p) {
        return logMap.get(p.getUniqueId().toString()).getRemainingTime();
    }
}
