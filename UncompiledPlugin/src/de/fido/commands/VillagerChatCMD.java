package de.fido.commands;

import de.fido.VarManager.VManager;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.entity.Villager;

import java.io.*;
import java.lang.annotation.Target;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;



public class VillagerChatCMD implements CommandExecutor {

    public Entity lastTargetVillager = null;
    Random generate = new Random();

    static VManager classObjVMan = new VManager();

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

    public static void setItems(Player p) {
        ArrayList<ItemStack> curItems = new ArrayList<ItemStack>();
        int i = 0;
        if(p.getExp() >= classObjVMan.WoodPrice) {
            ItemStack testItem1 = createGuiItem(Material.WOOD, ChatColor.GREEN + "Wood ("+String.valueOf(classObjVMan.WoodPrice)+")", 64);
            curItems.add(testItem1);
        } else {
            ItemStack testItem1 = createGuiItem(Material.WOOD, ChatColor.RED + "Wood ("+String.valueOf(classObjVMan.WoodPrice)+", too Expensive!)", 64);
            curItems.add(testItem1);
        }


        for(ItemStack item : curItems) {
            classObjVMan.itemSelection1.setItem(i, item);
            i++;
        }

    }

    private static ItemStack createGuiItem(Material material, String name, int Amount, final String... lore) {
        final ItemStack item = new ItemStack(material, Amount);
        final ItemMeta meta = item.getItemMeta();

        ((ItemMeta) meta).setDisplayName(name);

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

                        Villager TargetVillager = null;

                        if(!(classObjVMan.curVillager == null)) {
                            TargetVillager = (Villager) classObjVMan.curVillager;
                        } else {
                            p.sendMessage(ChatColor.DARK_RED+"Sorry, there is no Villager or there too much.");
                            return true;
                        }

                        sendToPython(joinedString);

                        String PyResponse = getPythonReponse();

                        if(PyResponse.contains("%s")) {
                            PyResponse = String.format(PyResponse, TargetVillager.getCustomName());
                            System.out.println("[DEBUG]: Succefully Formatted.");
                        }

                        if(PyResponse.equals("Ja, ich hab Quests für dich. Ich öffne mal eine Übersicht von verfügbaren Aufträgen für dich.") || PyResponse.equals(
                                    "Ja, klar hab ich Quests für dich. Ich mach mal eine Übersicht von verfügbaren Aufträgen für dich auf.") || PyResponse.equals(
                                    "Ja, natürlich hab ich Quests. Ich öffne mal eine Übersicht von den verfügbaren Aufträgen.")) {
                                System.out.println("He want a Quest!");
                        }


                        if(PyResponse.equals("ASKHOWOLD")) {
                            System.out.println("He trys to get our Age.");
                            p.sendMessage(ChatColor.GOLD+"[DU] " + ChatColor.WHITE + joinedString);
                            p.sendMessage(ChatColor.DARK_PURPLE + "["+TargetVillager.getCustomName()+" - Villager] " + ChatColor.GRAY + weHaveNoAge[generate.nextInt(weHaveNoAge.length)]);
                        } else {
                                p.sendMessage(ChatColor.GOLD+"[DU] " + ChatColor.WHITE + joinedString);
                                p.sendMessage(ChatColor.DARK_PURPLE + "["+TargetVillager.getCustomName()+" - Villager] " + ChatColor.GRAY + PyResponse);
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
