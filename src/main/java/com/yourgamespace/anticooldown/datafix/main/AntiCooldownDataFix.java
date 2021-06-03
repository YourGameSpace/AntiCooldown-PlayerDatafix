package com.yourgamespace.anticooldown.datafix.main;

import de.tr7zw.changeme.nbtapi.NBTFile;
import de.tr7zw.changeme.nbtapi.NBTList;
import de.tr7zw.changeme.nbtapi.NBTListCompound;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class AntiCooldownDataFix extends JavaPlugin {

    private final String PREFIX = "§7[§3AntiCooldown-PlayerDataFix§7] ";
    private final ConsoleCommandSender ccs = Bukkit.getConsoleSender();

    @Override
    public void onEnable() {
        fixData();

        ccs.sendMessage(PREFIX + "§aShutdown Server... Please remove the plugin now if no errors occurred!");
        Bukkit.shutdown();
    }

    @Override
    public void onDisable() {

    }

    private void fixData() {
        ArrayList<String> worlds = new ArrayList<>();
        for(World world : Bukkit.getWorlds()) {
            worlds.add(world.getName());
        }

        for(String world : worlds) {
            File playerDataFolder = new File(world + "/playerdata");
            String[] uuids = playerDataFolder.list();
            for(String playerDataFileName : uuids) {
                ccs.sendMessage(PREFIX + "§aStarting fix for Playerdata-File §e" + playerDataFileName);

                try {
                    File playerDataFile = new File(world + "/playerdata/" + playerDataFileName);
                    NBTFile nbtPlayerFile = new NBTFile(playerDataFile);
                    NBTList list = nbtPlayerFile.getCompoundList("Attributes");
                    for (int i = 0; i < list.size(); i++) {
                        NBTListCompound lc = (NBTListCompound) list.get(i);
                        if (lc.getString("Name").equals("generic.attackSpeed")) {
                            lc.setDouble("Base", 4D);
                        }
                    }
                    nbtPlayerFile.save();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    ccs.sendMessage(PREFIX + "§cAn error has occurred! A detailed error report can be taken from the log!");
                }
            }
        }
    }
}
