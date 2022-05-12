package de.fido.VarManager;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class VManager {
    public static List<Entity> listOfAllEntitys = new ArrayList<Entity>();
    public static Entity curVillager;
    public static int ScannEveryXSec = 1;
    public static List<Player> listOfPlayers = new ArrayList<Player>();
    public static int FoundCount;
}
