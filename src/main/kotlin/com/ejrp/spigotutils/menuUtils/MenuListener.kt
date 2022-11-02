package com.ejrp.spigotutils.menuUtils

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.inventory.InventoryOpenEvent
import org.bukkit.plugin.java.JavaPlugin

/**
 * This class will register the listeners the inventory click, drag and close
 * listener. This is class is only use to quickly register Menu listeners and nothing else.
 * You can extend this class to make other menu or abstract class like I did
 * in this library (Menu, StaticSizeMenu, BasicMenu, ScrollingMenu)
 * @param plugin The plugin to register the listeners too.
 */

abstract class MenuListener(final override val plugin: JavaPlugin) : GenericMenuListener {

    init {
        plugin.server.pluginManager.registerEvents(MenuListenerRegister(this), plugin)
    }

    /**
     * This is the class that is used to register the inventory listener
     * from the Menu class because you cannot register listener from an abstract class.
     */
    internal class MenuListenerRegister(private val staticMenuListener: MenuListener) : Listener {
        @EventHandler
        fun onClick(event: InventoryClickEvent) {
           staticMenuListener.onClick(event)
        }

        @EventHandler
        fun onDrag(event: InventoryDragEvent) {
            staticMenuListener.onDrag(event)
        }

        @EventHandler
        fun onExit(event: InventoryCloseEvent) {
            staticMenuListener.onExit(event)
        }

        @EventHandler
        fun onOpen(event: InventoryOpenEvent) {
            staticMenuListener.onOpen(event)
        }
    }
}