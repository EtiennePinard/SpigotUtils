package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public interface GenericMenuListener {

    /**
     * Gets called when a click happens in an inventory
     * @param event The inventory click event that has just happened.
     */
    void onClick(@NotNull InventoryClickEvent event);

    /**
     * Gets called when an item is dragged in an inventory.
     * @param event The inventory drag event that has just happened.
     */
    void onDrag(@NotNull InventoryDragEvent event);

    /**
     * Gets called when an inventory is closed.
     * @param event The inventory close event that has just happened.
     */
    void onExit(@NotNull InventoryCloseEvent event);

    /**
     * Gets the plugin that the listeners are registered to.
     * @return The plugin that the listeners are registered to.
     */
    @NotNull JavaPlugin getPlugin();

    /**
     * Sets if the inventory click event should be bypassed
     * @param bypassClickEvent If the inventory click event should be bypassed
     */
    void setBypassClickEvent(boolean bypassClickEvent);

    /**
     * Sets if the inventory drag event should be bypassed
     * @param bypassDragEvent If the inventory drag event should be bypassed
     */
    void setBypassDragEvent(boolean bypassDragEvent);

    /**
     * Sets if the inventory close event should be bypassed
     * @param bypassCloseEvent If the inventory close event should be bypassed
     */
    void setBypassCloseEvent(boolean bypassCloseEvent);

    /**
     * Gets if the inventory click event is bypassed
     * @return If the inventory`click event is bypassed
     */
    boolean isClickEventBypass();

    /**
     * Gets if the inventory drag event is bypassed
     * @return If the inventory`drag event is bypassed
     */
    boolean isDragEventBypass();

    /**
     * Gets if the inventory close event is bypassed
     * @return If the inventory`close event is bypassed
     */
    boolean isCloseEventBypass();
}
