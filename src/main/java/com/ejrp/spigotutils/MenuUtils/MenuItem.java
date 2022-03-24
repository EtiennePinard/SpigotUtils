package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

/**
 * Represents an item in a menu. It has an item that will be displayed in the inventory and a consumer that will be applied when a player clicks on that item.
 * This depends though on how the implementation of the Menu class is constructed.
 * Note: In all the implementation of the menu abstract class in this library does apply the consumer when this
 * item is clicked.
 */
public class MenuItem {

    @NotNull private final ItemStack item;
    @NotNull private final Consumer<InventoryClickEvent> onItemClick;

    /**
     * Creates a new Menu Item object
     * @param item The item that will be displayed
     * @param onItemClick The consumer that will be applied when the item is clicked. If it is null it will be an empty consumer.
     */
    public MenuItem(@NotNull ItemStack item, @Nullable Consumer<InventoryClickEvent> onItemClick) {
        this.item = item;
        this.onItemClick = onItemClick != null ? onItemClick : event -> {};
    }

    /**
     * Creates a new Menu Item object with an empty consumer
     * @param item The item that will be displayed
     */
    public MenuItem(@NotNull ItemStack item) { this(item,null); }

    /**
     * Gets the item of this Menu Item object
     * @return The item of this Menu Item object
     */
    @NotNull public ItemStack getItem() { return this.item; }

    /**
     * Applies the listener of this menu item
     * @param event The event that was passed when the player clicked on this item.
     */
    public void clicked(InventoryClickEvent event) { this.onItemClick.accept(event); }

}
