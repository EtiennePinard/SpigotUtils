package com.ejrp.spigotutils.menuUtils

import org.bukkit.inventory.Inventory

interface GenericMenu {
    /**
     * Gets the inventory
     * @return The inventory
     */
    val inventory: Inventory
    /**
     * The name of this inventory
     */
    val name : String
    get() = inventory.name

    /**
     * The size of this inventory
     */
    val size: Int
    get() = inventory.size

    /**
     * Removes an item at the specified index
     * @param index The index of the item to remove
     * @throws IllegalArgumentException If the index is out of bounds
     * @throws UnsupportedOperationException If the menu does not support this operation.
     */
    fun removeItem(index: Int)


    /**
     * Updates the contents of the inventory.
     */
    fun updateInventory()
}