package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This is a specialization of the Menu class. It is a static menu,
 * meaning that every player that uses this inventory sees the same thing.
 */
public abstract class Menu extends MenuListener {

    @NotNull private final Inventory inventory;

    /**
     * Creates a new instance of a static menu.
     * @param plugin The plugin to register the listeners too.
     * @param name The name of this inventory.
     * @param size The size of this inventory.
     * @throws IllegalArgumentException If the size is a multiple of 9 between 0 and 54
     */
    public Menu(@NotNull JavaPlugin plugin, @NotNull String name, int size, @Nullable InventoryHolder owner) {
        super(plugin);
        this.inventory = Bukkit.createInventory(owner,size,name);
    }

    /**
     * Adds an item to the inventory at the specified index.
     * I made it use a menu item instead of a regular item stack
     * because the menu item provides code when the item is clicked.
     * @param index The index of the item
     * @param item The item to add
     * @throws IllegalArgumentException If the index is out of bounds
     */
    public abstract void addItem(int index, @NotNull MenuItem item);

    /**
     * Removes an item at the specified index
     * @param index The index of the item to remove
     * @throws IllegalArgumentException If the index is out of bounds
     */
    public abstract void removeItem(int index);

    /**
     * Returns the name of the inventory
     * @return The name of the inventory
     */
    public final String name() { return inventory.getName(); }

    /**
     * Returns the size of the inventory
     * @return The size of the inventory
     */
    public final int size() { return inventory.getSize(); }

    /**
     * Gets the inventory
     * @return The inventory
     */
    @NotNull public final Inventory getInventory() { return inventory; }

    /**
     * Updates the contents of the inventory.
     */
    public abstract void updateInventory();
}
