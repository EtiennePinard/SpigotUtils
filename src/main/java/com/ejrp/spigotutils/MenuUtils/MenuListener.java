package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.jetbrains.annotations.NotNull;

/**
 * This is the class that is used to register the inventory listener
 * from the Menu class because you cannot register listener from an abstract class.
 */
public class MenuListener implements Listener {

    private final Menu menu;

    protected MenuListener(Menu menu) { this.menu = menu; }

    @EventHandler
    public void onClick(@NotNull InventoryClickEvent event) { if (!menu.isBypassClickEvent()) menu.onClick(event); }

    @EventHandler
    public void onDrag(@NotNull InventoryDragEvent event) { if (!menu.isBypassDragEvent()) menu.onDrag(event); }

    @EventHandler
    public void onExit(@NotNull final InventoryCloseEvent event) { if (!menu.isBypassCloseEvent()) menu.onExit(event); }

}
