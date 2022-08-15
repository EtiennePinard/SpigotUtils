package com.ejrp.spigotutils.menuUtils

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.java.JavaPlugin

/**
 * Is a basic abstract implementation of the StaticSizeMenu abstract class.
 * The only fancy thing it does is that it has a parent menu.
 * When this inventory is closed, the parent menu inventory will be opened
 * only if the parent menu is not null.
 */
abstract class BasicMenu
/**
 * Creates a new BasicMenu with the specified parameters
 * @param plugin The plugin to register the listener to
 * @param name The name of this inventory
 * @param size The size of this inventory
 * @param items The items contained in this inventory
 * @param parent The parent of this menu. This menu will open when this one is clicked.
 * @throws IllegalArgumentException If an index of an item is invalid.
 */ @JvmOverloads constructor(
    plugin: JavaPlugin,
    name: String,
    size: Int,
    items: Map<Int, MenuItem>,
    var parent: GenericMenu? = null
) : StaticSizeMenu(plugin, name, size, null, items) {

    /**
     * This is the code that will be executed when the player inventory
     * is clicked when he is viewing this inventory.
     * Override this method if you want to execute some code when this event happens.
     * @param event The inventory clicked event that just occurred
     */
    open fun onPlayerInventoryClick(event: InventoryClickEvent) {}

    /**
     * Invokes the clicked() method if the player clicked a menu item
     * otherwise, invokes the onPlayerInventoryClick() method.
     * @param event The inventory click event that has just happened.
     */
    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return
        if (event.whoClicked !is Player) return
        if (event.inventory == inventory) {
            event.isCancelled = true
            if (event.clickedInventory is PlayerInventory) onPlayerInventoryClick(event) else if (items[event.slot] != null) items[event.slot]!!
                .clicked(event)
        }
    }

    /**
     * Cancels the event drag event.
     * @param event The inventory drag event that has just happened.
     */
    override fun onDrag(event: InventoryDragEvent) {
        if (event.inventory == inventory) event.isCancelled = true
    }

    /**
     * Opens the parent inventory if it is not null.
     * @param event The inventory close event that has just happened.
     */
    override fun onExit(event: InventoryCloseEvent) {
        if (event.inventory == inventory) {
            if (parent != null) Bukkit.getScheduler().runTaskLater(
                plugin,
                { event.player.openInventory(parent!!.inventory) }, 1)
        }
    }
}