package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item in a menu. It has an item that will be displayed in the inventory.
 * You need to override the clicked method in order to instantiate this class.
 * It is up to the implementation of the inventory to decide if they should use the clicked method or not.
 */
public abstract class MenuItem {

    @NotNull private final ItemStack item;

    /**
     * Creates a new Menu Item object
     * @param item The item that will be displayed
     */
    public MenuItem(@NotNull ItemStack item) { this.item = item; }

    /**
     * Gets the item of this Menu Item object
     * @return The item of this Menu Item object
     */
    @NotNull public final ItemStack getItem() { return this.item; }

    /**
     * Applies the listener of this menu item
     * @param event The event that was passed when the player clicked on this item.
     */
    public abstract void clicked(@NotNull InventoryClickEvent event);

}
