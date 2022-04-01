package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * This is a specialization of the Menu class. It is a static menu,
 * meaning that every player that uses this inventory sees the same thing.
 */
public abstract class StaticMenu extends Menu {

    @NotNull private final String name;
    private final int size;
    @NotNull private final Inventory inventory;

    /**
     * Creates a new instance of a static menu.
     * @param plugin The plugin to register the listeners too.
     * @param name The name of this inventory.
     * @param size The size of this inventory.
     * @throws IllegalArgumentException If the size is a multiple of 9 between 0 and 54
     */
    public StaticMenu(@NotNull JavaPlugin plugin, @NotNull String name, int size) {
        super(plugin);
        if (size % 9 != 0 || size < 9 || size > 54)
            throw new IllegalArgumentException("The size is not a multiple of nine between 9 and 54!");
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null,size,name);
    }

    /**
     * Opens the inventory to players.
     * @param players The players that will see this inventory.
     */
    public abstract void openTo(@NotNull Player... players);

    /**
     * Closes the inventory.
     * @param players The players that will have their inventory closed
     */
    public abstract void closeTo(@NotNull Player... players);

    /**
     * Adds an item to the inventory at the specified index.
     * I made it use a menu item instead of a regular item stack
     * because the menu item provides code when the item is clicked.
     * It is up to you to implement this feature or not.
     * @param index The index of the item
     * @param item The item to add
     */
    public abstract void addItem(int index, @NotNull MenuItem item);

    /**
     * Removes an item at the specified index
     * @param index The index of the item to remove
     */
    public abstract void removeItem(int index);

    /**
     * Gets the name of this inventory
     * @return The name of this inventory
     */
    @NotNull public final String name() { return name; }

    /**
     * Gets the size of this inventory
     * @return The size of this inventory
     */
    public final int size() { return size; }

    /**
     * Gets the inventory
     * @return The inventory
     */
    @NotNull public final Inventory getInventory() { return inventory; }

    /**
     * Updates the inventory. That usually means that the contents of the inventory will be updated
     */
    public abstract void updateInventory();
}
