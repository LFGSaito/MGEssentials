package com.minegusta.mgessentials.util;

import com.minegusta.mgessentials.Main;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class WGUtil {
    public static boolean canPVP(Entity e) {
        if (!Main.isWGEnabled()) return true;
        if (!(e instanceof LivingEntity)) return false;
        Location loc = e.getLocation();
        ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(e.getWorld()).getApplicableRegions(loc);
        if (set.size() > 0) {
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getFlags().containsKey(DefaultFlag.PVP) && r.getFlag(DefaultFlag.PVP) == StateFlag.State.DENY) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean canGetDamage(Entity e) {
        if (!Main.isWGEnabled()) return true;
        Location loc = e.getLocation();
        ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(e.getWorld()).getApplicableRegions(loc);
        if (set.size() > 0) {
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getFlags().containsKey(DefaultFlag.INVINCIBILITY) && r.getFlag(DefaultFlag.INVINCIBILITY) == StateFlag.State.ALLOW) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean canBuild(Player p) {
        if (!Main.isWGEnabled()) return true;
        Location loc = p.getLocation();
        ApplicableRegionSet set = WorldGuardPlugin.inst().getRegionManager(p.getWorld()).getApplicableRegions(loc);
        if (set.size() > 0) {
            for (ProtectedRegion r : set.getRegions()) {
                if (r.getFlags().containsKey(DefaultFlag.BUILD) && r.getFlag(DefaultFlag.BUILD) == StateFlag.State.ALLOW) {
                    return true;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean canFightEachother(Entity e1, Entity e2) {
        return canPVP(e1) && canGetDamage(e1) && canPVP(e2) && canGetDamage(e2);
    }
}
