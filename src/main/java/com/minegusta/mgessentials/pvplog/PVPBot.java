package com.minegusta.mgessentials.pvplog;

import com.google.common.collect.Lists;
import com.minegusta.mgessentials.Main;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;
import java.util.UUID;

public class PVPBot {
    private UUID uuid;
    private Location loc;
    private String name;
    private int TASK = -1;
    private Zombie v;
    private double health;
    private Inventory inv;
    private int seconds = 0;
    private ItemStack[] armour;
    private List<Chunk> chunks = Lists.newArrayList();

    public PVPBot(Player p) {
        this.uuid = p.getUniqueId();
        this.loc = p.getLocation();
        this.name = p.getName();
        this.health = p.getHealth();
        this.inv = p.getInventory();
        this.armour = p.getInventory().getArmorContents();
        //Getting 9 chunks around the location.
        for (int x = loc.getChunk().getX() - 1; x <= loc.getChunk().getX() + 1; x++) {
            for (int z = loc.getChunk().getZ() - 1; z <= loc.getChunk().getZ() + 1; z++) {
                chunks.add(loc.getWorld().getChunkAt(x, z));
            }
        }

        LogData.chunkMap.put(uuid.toString(), chunks);

        this.v = spawnBot();
        TASK = start();
        LogData.bots.put(v.getUniqueId().toString(), true);
    }


    private int start() {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(Main.PLUGIN, new Runnable() {
            @Override
            public void run() {
                loopTask();
            }
        }, 0, 20);
    }

    public void stop() {
        if (TASK != -1) {
            Bukkit.getScheduler().cancelTask(TASK);
        }
        LogData.remove(uuid);
        LogData.chunkMap.remove(uuid.toString());
    }

    private void loopTask() {
        if (v.isDead()) {

            LogData.bots.remove(v.getUniqueId().toString());

            for (ItemStack i : inv) {
                if (i != null && i.getType() != Material.AIR) loc.getWorld().dropItemNaturally(loc, i);
            }

            for (ItemStack i : armour) {
                if (i != null && i.getType() != Material.AIR) loc.getWorld().dropItemNaturally(loc, i);
            }
            LogoutManager.set(uuid, true);
            stop();
            return;
        }
        v.teleport(loc);
        seconds++;

        if (seconds > 10) {
            removeBot();
            stop();
        }
    }

    public void removeBot() {
        LogData.bots.remove(v.getUniqueId().toString());
        v.getEquipment().setArmorContents(new ItemStack[]{null, null, null, null});
        v.setHealth(0);
        v.damage(100);
        v.addPotionEffect(new PotionEffect(PotionEffectType.HARM, 20, 20));
        v.remove();
        v = null;
    }

    private Zombie spawnBot() {
        Bukkit.broadcastMessage(ChatColor.DARK_RED + name + ChatColor.RED + " just combat logged! Their NPC has been spawned.");
        Zombie v = (Zombie) loc.getWorld().spawnEntity(loc, EntityType.ZOMBIE);
        v.setBaby(false);
        v.setCustomNameVisible(true);
        v.setCustomName(name);
        v.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 20 * 180, 10));
        v.setVillagerProfession(null);
        v.setHealth(health / 2 + 1);
        v.getEquipment().setArmorContents(armour);

        return v;
    }

    //10 seconds but this can be changed in the listener class. Has to be updated by hand.
    public int getRemainingTime() {
        return 10 - seconds;
    }
}
