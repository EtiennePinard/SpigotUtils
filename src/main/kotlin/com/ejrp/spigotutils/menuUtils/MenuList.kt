package com.ejrp.spigotutils.menuUtils

import com.ejrp.spigotutils.itemsUtils.ItemBuilder
import net.md_5.bungee.api.ChatColor
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack

/**
 * An implementation of a list of menus that are linked together. When created it will two things to the list of Menu that you will provide.
 * It will add two items on the bottom left and bottom right corners of each of the inventories.
 * These buttons will be a back and a next "button". The exact index of the back item is (menu.size() - 9)
 * and of the next item is (menu.size() - 1). It will also bypass the inventory close event of each inventory.
 * The order of the menu are the order of the list.
 */
class MenuList(next: Material, back: Material, private val menus: MutableList<StaticSizeMenu>) {

    private val next: ItemStack = ItemBuilder(next)
        .setDisplayName(ChatColor.GRAY.toString() + "Next")
        .getItem()
    private val back: ItemStack =  ItemBuilder(back)
        .setDisplayName(ChatColor.GRAY.toString() + "Back")
        .getItem()

    /**
     * Adds a menu at the specified index
     * @param index The index of the menu to add
     * @param menu The menu to add
     */
    fun addMenu(index: Int, menu: StaticSizeMenu) {
        menus.add(index, menu)
        updateMenus()
    }

    /**
     * Adds a menu at the end of the list of menus
     * @param menu The menu to add
     */
    fun addMenu(menu: StaticSizeMenu) {
        addMenu(menus.size, menu)
    }

    private fun updateMenus() {
        for (index in menus.indices) {
            val standardMenu = menus[index]
            val lastSlot: Int = standardMenu.size - 1
            val next = if (menus.size == index + 1) standardMenu else menus[index + 1]
            val back = if (index == 0) standardMenu else menus[index - 1]
            if (menus.size - 1 > index) // Any menu except the last one
                addNavigationArrow(standardMenu, next, lastSlot, this.next)
            if (index > 0) // Any menu except the first one
                addNavigationArrow(standardMenu, back, lastSlot - 8, this.back)
        }
    }

    private fun addNavigationArrow(
        menuToAddItemTo: StaticSizeMenu,
        menuToNavigateTo: StaticSizeMenu,
        slot: Int,
        navigateItem: ItemStack
    ) {
        menuToAddItemTo.addItem(slot, object : MenuItem {
            override val item: ItemStack
                get() = navigateItem
            override fun clicked(event: InventoryClickEvent) {
                if (event.whoClicked is Player) event.whoClicked.openInventory(menuToNavigateTo.inventory)
            }
        })
    }

    /**
     * Gets the Menu at the specific location in the list.
     * @param index The index of the Menu in the list.
     * @return The menu at the specific location in the list.
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    @Throws(IndexOutOfBoundsException::class)
    fun getInventoryAt(index: Int): StaticSizeMenu = menus[index]
}