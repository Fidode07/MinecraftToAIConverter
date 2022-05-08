package de.fido.commands;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Villager;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Array;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.nio.charset.Charset;


public class VillagerChatCMD implements CommandExecutor {

    public Entity lastTargetVillager = null;
    public String[] tellMyName = {"Wir besitzen zwar keine Nachnamen, mein Vorname lautet aber ",
            "Ich hab keinen Nachnamen, aber mein Vorname ist ",
            "Zwar hab ich keinen Nachnamen, aber mein Vorname ist "};

    public String[] weHaveNoAge = {"Tut mir leid, ein genaues Alter haben wir leider nicht.",
            "Sorry, bedauerlicherweise haben wir kein genaues Alter.",
            "Wir Dorfbewohner haben enttäuschenderweise kein genaues Alter, tut mir leid.",
            "Wir haben bedauerlicherweise kein genaues Alter, sorry."};
    public int Quarzpreis = 500;

    public static void sendToPython(String message) throws IOException, InterruptedException {
        // need host and port, we want to connect to the ServerSocket at port 7777
        Socket socket = new Socket("localhost", 7777);
        System.out.println("Connected!");

        // get the output stream from the socket.
        OutputStream outputStream = socket.getOutputStream();
        // create a data output stream from the output stream so we can send data through it
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        System.out.println("Sending string to the ServerSocket");

        // write the message we want to send
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush(); // send the message

        System.out.println("Closing socket and terminating program.");
        socket.close();
    }

    public static String getPythonReponse() throws IOException {
        ServerSocket ss = new ServerSocket(4839);
        Socket s = ss.accept();

        System.out.println("Client Connected!");

        InputStreamReader in = new InputStreamReader(s.getInputStream());
        BufferedReader bf = new BufferedReader(in);

        String str = bf.readLine();
        System.out.println("Client: " + str);

        s.close();
        ss.close();
        return str;
    }

    public static boolean isInBorder(Location center, Location notCenter, int range) {
        int x = center.getBlockX(), z = center.getBlockZ();
        int x1 = notCenter.getBlockX(), z1 = notCenter.getBlockZ();

        if (x1 >= (x + range) || z1 >= (z + range) || x1 <= (x - range) || z1 <= (z - range)) {
            return false;
        }
        return true;
    }


    public static List<Entity> getNearbyEntities(Location where, int range) {
        List<Entity> found = new ArrayList<Entity>();

        for (Entity entity : where.getWorld().getEntities()) {
            if(entity.getType() == EntityType.VILLAGER) {
                if (isInBorder(where, entity.getLocation(), range)) {
                    found.add(entity);
                    System.out.println("Entity: " + entity.getName());
                } else {
                    if(entity.isGlowing()) {
                        entity.setGlowing(false);
                    }
                }
            }
        }
        return found;
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        item.setAmount(64);

        // Set the name of the item
        ((ItemMeta) meta).setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(sender instanceof Player) {
            Player p = (Player) sender;
            if (args.length >= 1) {
                try {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < args.length; i++) {
                        stringBuilder.append(args[i] + " ");
                    }

                    String joinedString = stringBuilder.toString();

                    List<Entity> listOfVillagers = getNearbyEntities(p.getLocation(), 2);

                    int FoundCount = 0;

                    for(Entity entity : listOfVillagers) {
                        FoundCount = FoundCount+1;
                    }

                    if(FoundCount == 1) {

                        Villager TargetVillager = null;

                        for (Entity entity : listOfVillagers) {
                            TargetVillager = (Villager) entity;
                            if (!(lastTargetVillager == null)) {
                                if (!(lastTargetVillager == entity)) {
                                    lastTargetVillager.setGlowing(false);
                                }
                            }

                            lastTargetVillager = entity;
                        }

                        assert TargetVillager != null;

                        if (!(TargetVillager.isGlowing())) {
                            TargetVillager.setGlowing(true);
                        }

                        sendToPython(joinedString);

                        String PyResponse = getPythonReponse();

                        if (PyResponse.equalsIgnoreCase("Ja, ich kann dir Items besorgen. Ich öffne eine Auswahl an Items für dich. Vergiss aber nicht, dass ich nicht kostenlos arbeite!") || PyResponse.equalsIgnoreCase("Klar kann ich dir Items besorgen. Ich öffne mal eine Auswahl für dich, vergiss aber nicht das ich nicht Kostenlos bin!") || PyResponse.equalsIgnoreCase("Ja, Items kann ich besorgen. Allerdings arbeite ich nicht kostenlos, ich öffne dir mal eine Auswahl an Items.")) {
                            File file = new File("plugins/Info" + "money.yml");
                            FileConfiguration cfg = YamlConfiguration.loadConfiguration(file);

                            Inventory inv = Bukkit.createInventory(null, 9, "Items Auswahl");

                            ChatColor k3CoinsColor;

                            if (cfg.getInt(p.getUniqueId() + "money.value") >= Quarzpreis) {
                                k3CoinsColor = ChatColor.GREEN;
                            } else {
                                k3CoinsColor = ChatColor.RED;
                            }

                            ItemStack QuarzBlockStack = createGuiItem(Material.QUARTZ_BLOCK, ChatColor.LIGHT_PURPLE + "Quarzblöcke (Stack)", k3CoinsColor + String.valueOf(Quarzpreis) + " Coins");

                            inv.setItem(0, QuarzBlockStack);

                            p.openInventory(inv);
                        } else
                            if(PyResponse.equals("Ja, ich hab Quests für dich. Ich öffne mal eine Übersicht von verfügbaren Aufträgen für dich.") || PyResponse.equals(
                                    "Ja, klar hab ich Quests für dich. Ich mach mal eine Übersicht von verfügbaren Aufträgen für dich auf.") || PyResponse.equals(
                                    "Ja, natürlich hab ich Quests. Ich öffne mal eine Übersicht von den verfügbaren Aufträgen.")) {
                                System.out.println("He want a Quest!");
                            }

                        try {
                            System.out.println(" ");
                            for (ItemStack item : TargetVillager.getInventory()) {
                                System.out.println("ITEM: " + item.getType().name());
                            }
                            p.openInventory(TargetVillager.getInventory());
                        } catch (NullPointerException e) {
                            System.out.println("Villager dosnt have a Inventory!");
                            p.sendMessage(ChatColor.RED+"Warning: We cant find Villagers Inventory. You have to open it by Click!");
                        }

                        Random generate = new Random();

                        if(PyResponse.equals("NONERESP")) {
                            System.out.println("Stopword detected!");
                        } else if(PyResponse.equals("HEWANTSOURNAME")) {
                            System.out.println("He try to get our name ...");
                            p.sendMessage(ChatColor.GOLD+"[DU] " + ChatColor.WHITE + joinedString);
                            p.sendMessage(ChatColor.DARK_PURPLE + "["+TargetVillager.getCustomName()+" - Villager] " + ChatColor.GRAY + tellMyName[generate.nextInt(tellMyName.length)] + TargetVillager.getCustomName());
                        } else if(PyResponse.equals("ASKHOWOLD")) {
                            System.out.println("He trys to get our Age.");
                            p.sendMessage(ChatColor.GOLD+"[DU] " + ChatColor.WHITE + joinedString);
                            p.sendMessage(ChatColor.DARK_PURPLE + "["+TargetVillager.getCustomName()+" - Villager] " + ChatColor.GRAY + weHaveNoAge[generate.nextInt(weHaveNoAge.length)]);
                        } else {
                                p.sendMessage(ChatColor.GOLD+"[DU] " + ChatColor.WHITE + joinedString);
                                p.sendMessage(ChatColor.DARK_PURPLE + "["+TargetVillager.getCustomName()+" - Villager] " + ChatColor.GRAY + PyResponse);
                        }

                    } else if(FoundCount >= 2) {
                        p.sendMessage(ChatColor.DARK_RED+"Sorry, there a too Many Villagers!");
                    } else {
                        p.sendMessage(ChatColor.DARK_RED+"Sorry, there is no Villager. You have to get closer!");
                    }

                } catch (IOException | InterruptedException e) {
                    sender.sendMessage(ChatColor.RED+"Sorry, there was a Error!");
                    e.printStackTrace();
                }
            } else {
                sender.sendMessage(ChatColor.RED+"WRONG USAGE! Usage: /chat (message)");
            }
        } else {
            sender.sendMessage(ChatColor.RED+"Sorry, this Command is only for Players!");
        }
        return false;
    }
}
