package de.fido.main;

import de.fido.MobScanner.MScanner;
import de.fido.VarManager.VManager;
import de.fido.commands.VillagerChatCMD;
import de.fido.listeners.PlayerHandler;
import de.fido.listeners.VillagerHandler;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.Random;


public class Main extends JavaPlugin {

    public String [] VillagerNames= {"Tom", "Manfred", "Thomas", "Christoph", "Luke", "Falko", "Flavio", "Fady", "Fynn", "Mark", "Morris", "Matti", "Gustav", "Phillipp",
            "Felix", "Julian", "Finn", "Paul", "Luis", "Henry", "Peter", "Quirin", "Liam", "Oswald", "Henryk", "Haku", "Hakan", "Ali", "Mohammed", "Quinn", "Kasimir", "Rene",
            "Arik", "Jack", "Jannik", "Jonathan", "Joel", "Jarin", "Jörn", "Calvin", "Kilian", "Jeremy", "Lars"};

    VManager classObjVMan = new VManager();
    MScanner mScann = new MScanner();


    public void onEnable() {
        FileConfiguration config = this.getConfig();
        saveConfig();
        Random generate = new Random();

        getCommand("chat").setExecutor(new VillagerChatCMD());

        PluginManager PlManager = getServer().getPluginManager();

        PlManager.registerEvents(new VillagerHandler(), this);
        PlManager.registerEvents(new PlayerHandler(), this);

        mScann.scann();

        System.out.println(ChatColor.GREEN + "Succefully initialized Commands and Listeners. Start Villager Setup ...");

        Bukkit.getWorld("world").getEntities().forEach(entity -> {
            if(entity.getType() == EntityType.VILLAGER) {
                VManager.listOfAllEntitys.add(entity);
                System.out.println(ChatColor.GOLD + prefix + ChatColor.GREEN + "Villager Detected! Set Name if there is no Name ...");
                String curTargetName = VillagerNames[generate.nextInt(VillagerNames.length)];
                if(entity.getCustomName() == null) {
                    entity.setCustomName(curTargetName);
                    entity.setCustomNameVisible(false);
                    System.out.println(ChatColor.GOLD + prefix + ChatColor.GREEN + "Set Name to: " + ChatColor.LIGHT_PURPLE + curTargetName);
                }
            }
        });

        for(Player p : Bukkit.getOnlinePlayers()){
            classObjVMan.listOfPlayers.add(p);
        }

        System.out.println(prefix + "Succefully Booted!");
    }

    public String prefix = "[MinecraftToAIConverter] ";
}
