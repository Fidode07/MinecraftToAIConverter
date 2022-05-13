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

    public static String[] VillagerNames = {"Tom", "Manfred", "Thomas", "Christoph", "Luke", "Falko", "Flavio", "Fady", "Fynn", "Mark", "Morris", "Matti", "Gustav", "Phillipp",
            "Felix", "Julian", "Finn", "Paul", "Luis", "Henry", "Peter", "Quirin", "Liam", "Oswald", "Henryk", "Haku", "Hakan", "Ali", "Mohammed", "Quinn", "Kasimir", "Rene",
            "Arik", "Jack", "Jannik", "Jonathan", "Joel", "Jarin", "Jörn", "Calvin", "Kilian", "Jeremy", "Lars", "Ben", "Jonas", "Niklas", "David", "Oskar", "Philipp", "Leon",
            "Dewis", "Donald", "Enzo", "Danzo", "Erik", "Dario"};
}
