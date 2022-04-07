package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

public interface GenericMenu extends GenericMenuListener {

    /**
     * Adds an item to the inventory at the specified index.
     * I made it use a menu item instead of a regular item stack
     * because the menu item provides code when the item is clicked.
     * @param index The index of the item
     * @param item The item to add
     * @throws IllegalArgumentException If the index is out of bounds
     * @throws UnsupportedOperationException If the menu does not support this operation.
     */
    void addItem(int index, @NotNull MenuItem item);

    /**
     * Removes an item at the specified index
     * @param index The index of the item to remove
     * @throws IllegalArgumentException If the index is out of bounds
     * @throws UnsupportedOperationException If the menu does not support this operation.
     */
    void removeItem(int index);

    /**
     * Returns the name of the inventory
     * @return The name of the inventory
     */
    @NotNull String name();

    /**
     * Returns the size of the inventory
     * @return The size of the inventory
     */
    int size();

    /**
     * Gets the inventory
     * @return The inventory
     */
    @NotNull Inventory getInventory();

    /**
     * Updates the contents of the inventory.
     */
    void updateInventory();

}