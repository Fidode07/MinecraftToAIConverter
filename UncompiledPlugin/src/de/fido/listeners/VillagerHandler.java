package de.fido.listeners;

import de.fido.VarManager.VManager;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import java.util.Random;

public class VillagerHandler implements Listener {

    public int MaxNames = 43;
    public Entity curVillager = null;
    VManager classObjVMan = new VManager();

    public static int foundCount = 0;

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();
        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }

        return true;
    }

//    @EventHandler
//    public void onMove(PlayerMoveEvent e) {
//        foundCount = 0;
//
//        for(Entity entity : VManager.listOfAllEntitys) {
//            if (isInBorder(e.getPlayer().getLocation(), entity.getLocation(), 2)) {
//                foundCount = foundCount + 1;
//                curVillager = entity;
//            } else {
//                if (entity.isGlowing()) {
//                    entity.setGlowing(false);
//                }
//            }
//        }
//
//        if (foundCount == 1 && curVillager != null) {
//            if (!(curVillager.isGlowing())) {
//                curVillager.setGlowing(true);
//            }
//        } else if (foundCount >= 2) {
//            for (Entity entity : VManager.listOfAllEntitys) {
//                if (entity.isGlowing()) {
//                    entity.setGlowing(false);
//                }
//            }
//        }
//    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        Random generate = new Random();
        String TargetName = classObjVMan.VillagerNames[generate.nextInt(classObjVMan.VillagerNames.length)];
        if (e.getEntityType() == EntityType.VILLAGER) {
            VManager.listOfAllEntitys.add(e.getEntity());
            if (e.getEntity().getCustomName() == null) {
                e.getEntity().setCustomName(TargetName);
                e.getEntity().setCustomNameVisible(false);
                System.out.println("Set Name to: " + TargetName);
            }
            System.out.println("New Villager Found!");
        }
    }

    @EventHandler
    public void onMobDeath(EntityDeathEvent e) {
        if(e.getEntityType() == EntityType.VILLAGER) {
            VManager.listOfAllEntitys.remove(e);
        }
    }
}
