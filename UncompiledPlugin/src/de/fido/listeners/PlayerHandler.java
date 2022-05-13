package de.fido.listeners;

import de.fido.VarManager.VManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerHandler implements Listener {

    VManager vMan = new VManager();

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        vMan.listOfPlayers.add(e.getPlayer());
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent e) {
        vMan.listOfPlayers.remove(e.getPlayer());
    }
}
