package de.fido.VarManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class VManager {
    public static List<Entity> listOfAllEntitys = new ArrayList<Entity>();
    public static Entity curVillager;
    public static int ScannEveryXSec = 1;
    public static List<Player> listOfPlayers = new ArrayList<Player>();
    public static int FoundCount;
    public static Inventory itemSelection1 = Bukkit.createInventory(null, 9*3, ChatColor.GREEN + "Item Selection");
    public static int WoodPrice = 10;
}
