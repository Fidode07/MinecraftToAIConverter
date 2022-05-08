package de.fido.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class VillagerHandler implements Listener {

    public int MaxNames = 43;

    public String [] VillagerNames= {"Tom", "Manfred", "Thomas", "Christoph", "Luke", "Falko", "Flavio", "Fady", "Fynn", "Mark", "Morris", "Matti", "Gustav", "Phillipp",
    "Felix", "Julian", "Finn", "Paul", "Luis", "Henry", "Peter", "Quirin", "Liam", "Oswald", "Henryk", "Haku", "Hakan", "Ali", "Mohammed", "Quinn", "Kasimir", "Rene",
    "Arik", "Jack", "Jannik", "Jonathan", "Joel", "Jarin", "Jörn", "Calvin", "Kilian", "Jeremy", "Lars"};

    public static int foundCount = 0;

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();
        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }

        return true;
    }

    public static List<Entity> getNearbyEntities(Location where, int range) {
        foundCount = 0;
        List<Entity> found = new ArrayList<Entity>();

        for (Entity entity : where.getWorld().getEntities()) {
            if (entity.getType() == EntityType.VILLAGER) {
                if (isInBorder(where, entity.getLocation(), range)) {
                    found.add(entity);
                    foundCount = foundCount + 1;
                } else {
                    if (entity.isGlowing()) {
                        entity.setGlowing(false);
                    }
                }
            }
        }
        return found;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        List<Entity> listOfEntities = getNearbyEntities(e.getPlayer().getLocation(), 2);

        if (foundCount == 1) {
            for (Entity entity : listOfEntities) {
                if (!(entity.isGlowing())) {
                    entity.setGlowing(true);
                }
            }
        } else if (foundCount >= 2) {
            for (Entity entity : listOfEntities) {
                if (entity.isGlowing()) {
                    entity.setGlowing(false);
                }
            }
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        Random generate = new Random();
        String TargetName = VillagerNames[generate.nextInt(VillagerNames.length)];
        if(e.getEntityType() == EntityType.VILLAGER) {
            if(e.getEntity().getCustomName() == null) {
                e.getEntity().setCustomName(TargetName);
                e.getEntity().setCustomNameVisible(false);
                System.out.println("Set Name to: " + TargetName);
            }
            System.out.println("New Villager Found!");
        }
    }
}
