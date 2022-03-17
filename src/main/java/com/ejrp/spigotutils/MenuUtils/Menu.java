package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * Contains methods to override when you want to create a menu.
 * This class will register the listeners, and you can toggle them
 * on and aff using the three bypass boolean.
 */
public abstract class Menu {

    private final JavaPlugin plugin;
    private boolean bypassClickEvent = false;
    private boolean bypassDragEvent = false;
    private boolean bypassCloseEvent = false;

    /**
     * Creates a new instance of a menu.
     * @param plugin The plugin to register the listeners too.
     */
    public Menu(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new MenuListener(this), plugin);
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
    public abstract String name();

    /**
     * Gets the size of this inventory
     * @return The size of this inventory
     */
    public abstract int size();

    /**
     * Gets the inventory
     * @return The inventory
     */
    @NotNull public abstract Inventory getInventory();

    /**
     * Updates the inventory. That usually means that the contents of the inventory will be updated
     */
    public abstract void updateInventory();

    /**
     * Gets called when a click happens in an inventory
     * @param event The inventory click event that has just happened.
     */
    public abstract void onClick(@NotNull InventoryClickEvent event);

    /**
     * Gets called when an item is dragged in an inventory.
     * @param event The inventory drag event that has just happened.
     */
    public abstract void onDrag(@NotNull InventoryDragEvent event);

    /**
     * Gets called when an inventory is closed.
     * @param event The inventory close event that has just happened.
     */
    public abstract void onExit(@NotNull InventoryCloseEvent event);

    /**
     * Gets the plugin that the listeners are registered to.
     * @return The plugin that the listeners are registered to.
     */
    @NotNull public JavaPlugin getPlugin() { return this.plugin; }

    /**
     * Sets if the inventory click event should be bypassed
     * @param bypassClickEvent If the inventory click event should be bypassed
     */
    public void setBypassClickEvent(boolean bypassClickEvent) { this.bypassClickEvent = bypassClickEvent; }

    /**
     * Sets if the inventory drag event should be bypassed
     * @param bypassDragEvent If the inventory drag event should be bypassed
     */
    public void setBypassDragEvent(boolean bypassDragEvent) { this.bypassDragEvent = bypassDragEvent; }

    /**
     * Sets if the inventory close event should be bypassed
     * @param bypassCloseEvent If the inventory close event should be bypassed
     */
    public void setBypassCloseEvent(boolean bypassCloseEvent) { this.bypassCloseEvent = bypassCloseEvent; }

    /**
     * Gets if the inventory click event is bypassed
     * @return If the inventory`click event is bypassed
     */
    public boolean isBypassClickEvent() { return bypassClickEvent; }

    /**
     * Gets if the inventory drag event is bypassed
     * @return If the inventory`drag event is bypassed
     */
    public boolean isBypassDragEvent() { return bypassDragEvent; }

    /**
     * Gets if the inventory close event is bypassed
     * @return If the inventory`close event is bypassed
     */
    public boolean isBypassCloseEvent() { return bypassCloseEvent; }
}
