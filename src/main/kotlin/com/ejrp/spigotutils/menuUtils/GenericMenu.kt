package com.ejrp.spigotutils.menuUtils

import org.bukkit.inventory.Inventory

interface GenericMenu : GenericMenuListener {
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
     * Adds an item to the inventory at the specified index.
     * I made it use a menu item instead of a regular item stack
     * because the menu item provides code when the item is clicked.
     * @param index The index of the item
     * @param item The item to add
     * @throws IllegalArgumentException If the index is out of bounds
     * @throws UnsupportedOperationException If the menu does not support this operation.
     */
    fun addItem(index: Int, item: MenuItem)

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