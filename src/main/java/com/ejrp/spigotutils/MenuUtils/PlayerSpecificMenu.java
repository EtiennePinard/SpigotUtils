package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public abstract class PlayerSpecificMenu extends Menu {

    /**
     * Creates a new instance of a menu.
     *
     * @param plugin The plugin to register the listeners too.
     */
    public PlayerSpecificMenu(@NotNull JavaPlugin plugin) { super(plugin); }

    public abstract Inventory getInventory(@NotNull Player player);

    public abstract String name(Player player);

    public abstract int size(Player player);

    public abstract void updateInventory(Player player);
}
