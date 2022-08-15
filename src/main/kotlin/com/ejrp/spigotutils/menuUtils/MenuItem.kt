package com.ejrp.spigotutils.menuUtils

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * Represents an item in a menu. It has an item that will be displayed in the inventory.
 * You can override the clicked method to set the code to run when this item is clicked.
 * Please note that it is up to the implementation of the inventory to execute the clicked
 * method or not.
 */
interface MenuItem {

    val item: ItemStack

    /**
     * This will run when the item is clicked.
     * It is nothing by default.
     * @param event The event that was passed when the player clicked on this item.
     */
    fun clicked(event: InventoryClickEvent) {}
}