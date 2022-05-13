package de.fido.MobScanner;

import de.fido.VarManager.VManager;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MScanner {

    static VManager vMan = new VManager();

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();
        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }

        return true;
    }

    public static void scann() {
        System.out.println("GOT IT!");
        Runnable helloRunnable = new Runnable() {
            public void run() {
                for (Player p : vMan.listOfPlayers) {
                    for (Entity entity : VManager.listOfAllEntitys) {
                        if (isInBorder(p.getLocation(), entity.getLocation(), 2)) {
                            vMan.FoundCount = vMan.FoundCount + 1;
                            vMan.curVillager = entity;
                        } else {
                            if (entity.isGlowing()) {
                                entity.setGlowing(false);
                            }
                        }
                    }

                    if (vMan.FoundCount == 1 && vMan.curVillager != null) {
                        if (!(vMan.curVillager.isGlowing())) {
                            vMan.curVillager.setGlowing(true);
                        }
                    } else if (vMan.FoundCount >= 2) {
                        for (Entity entity : VManager.listOfAllEntitys) {
                            if (entity.isGlowing()) {
                                entity.setGlowing(false);
                            }
                        }

                        vMan.curVillager = null;
                    } else {
                        vMan.curVillager = null;
                    }
                }
                vMan.FoundCount = 0;
            }
        };

        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(helloRunnable, 0, vMan.ScannEveryXSec, TimeUnit.SECONDS);
    }
}
