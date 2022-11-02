package com.ejrp.spigotutils.menuUtils

import org.bukkit.Bukkit
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

/**
 * This is an abstract implementation of the Menu class.
 * This class has one field, a final MenuItem array. It is an
 * array because the size of this menu will not change.
 * Extend this class to create your own custom static menus.
 * @param plugin The plugin to register the listeners to.
 * @param name The name of the inventory.
 * @param size The size of the inventory.
 * @param owner The owner of the inventory, null of there is no owner.
 * @param items The items in this inventory.
 */
abstract class StaticSizeMenu(
    plugin: JavaPlugin,
    name: String,
    size: Int,
    owner: InventoryHolder?,
    val items: Array<MenuItem?>
) : MenuListener(plugin), GenericMenu {
    final override val inventory: Inventory = Bukkit.createInventory(owner, size, name)

    init {
        require(items.size == size) { "The item array size needs to be the same size as the inventory" }
    }

    /**
     * Creates a new StaticSizeMenu object with the given parameters.
     * @param plugin The plugin to register the listeners to.
     * @param name The name of the inventory.
     * @param size The size of the inventory.
     * @param owner The owner of the inventory, null of there is no owner.
     * @param items The items in this inventory, null for an empty inventory.
     */
    constructor(
        plugin: JavaPlugin,
        name: String,
        size: Int,
        owner: InventoryHolder?,
        items: Map<Int, MenuItem>
    ) : this(plugin, name, size, owner, arrayOfNulls(size)) {
        items.forEach { (index: Int, item: MenuItem) -> addItem(index, item) }
    }

    fun addItem(index: Int, item: MenuItem) {
        require(index < 0 || index > size) { "The index needs to be between the size of the inventory and 0!" }
        items[index] = item
        updateInventory()
    }

    override fun removeItem(index: Int) {
        require(index < 0 || index > size) { "The index needs to be between the size of the inventory and 0!" }
        items[index] = null
        updateInventory()
    }

    /**
     * Updates the contents of the inventory.
     * Call this method if the content of the inventory
     * and the MenuItem array are out of sync.
     */
    override fun updateInventory() {
        val contents = arrayOfNulls<ItemStack>(size)
        for (i in items.indices) contents[i] = items[i]?.item
        inventory.contents = contents
    }
}