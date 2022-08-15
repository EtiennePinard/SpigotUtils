package com.ejrp.spigotutils.menuUtils

import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.plugin.java.JavaPlugin

interface GenericMenuListener {
    /**
     * Gets called when a click happens in an inventory
     * @param event The inventory click event that has just happened.
     */
    fun onClick(event: InventoryClickEvent)

    /**
     * Gets called when an item is dragged in an inventory.
     * @param event The inventory drag event that has just happened.
     */
    fun onDrag(event: InventoryDragEvent)

    /**
     * Gets called when an inventory is closed.
     * @param event The inventory close event that has just happened.
     */
    fun onExit(event: InventoryCloseEvent)

    /**
     * Gets the plugin that the listeners are registered to.
     * @return The plugin that the listeners are registered to.
     */
    val plugin: JavaPlugin

    /**
     * Gets if the inventory click event is bypassed
     * @return If the inventory`click event is bypassed
     */
    var isClickEventBypass: Boolean

    /**
     * Gets if the inventory drag event is bypassed
     * @return If the inventory`drag event is bypassed
     */
    var isDragEventBypass: Boolean

    /**
     * Gets if the inventory close event is bypassed
     * @return If the inventory`close event is bypassed
     */
    var isCloseEventBypass: Boolean
}