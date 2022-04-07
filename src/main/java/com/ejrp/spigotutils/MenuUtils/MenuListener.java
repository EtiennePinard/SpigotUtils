package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

/**
 * This class will register the listeners the inventory click, drag and close
 * listener. This is class is only use to quickly register Menu listeners and nothing else.
 * You can extend this class to make other menu or abstract class like I did
 * in this library (Menu, StaticSizeMenu, BasicMenu, ScrollingMenu)
 */
public abstract class MenuListener implements GenericMenu {

    private final JavaPlugin plugin;
    private boolean bypassClickEvent = false;
    private boolean bypassDragEvent = false;
    private boolean bypassCloseEvent = false;

    /**
     * Creates a new instance of a menu.
     * @param plugin The plugin to register the listeners too.
     */
    public MenuListener(@NotNull JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(new MenuListenerRegister(this), plugin);
    }

    @NotNull public final JavaPlugin getPlugin() { return this.plugin; }

    public final void setBypassClickEvent(boolean bypassClickEvent) { this.bypassClickEvent = bypassClickEvent; }

    public final void setBypassDragEvent(boolean bypassDragEvent) { this.bypassDragEvent = bypassDragEvent; }

    public final void setBypassCloseEvent(boolean bypassCloseEvent) { this.bypassCloseEvent = bypassCloseEvent; }

    public final boolean isClickEventBypass() { return bypassClickEvent; }

    public final boolean isDragEventBypass() { return bypassDragEvent; }

    public final boolean isCloseEventBypass() { return bypassCloseEvent; }

    /**
     * This is the class that is used to register the inventory listener
     * from the Menu class because you cannot register listener from an abstract class.
     */
    public static final class MenuListenerRegister implements Listener {

        private final MenuListener staticMenuListener;

        private MenuListenerRegister(MenuListener staticMenuListener) { this.staticMenuListener = staticMenuListener; }

        @EventHandler
        public void onClick(@NotNull InventoryClickEvent event) { if (!staticMenuListener.isClickEventBypass()) staticMenuListener.onClick(event); }

        @EventHandler
        public void onDrag(@NotNull InventoryDragEvent event) { if (!staticMenuListener.isDragEventBypass()) staticMenuListener.onDrag(event); }

        @EventHandler
        public void onExit(@NotNull final InventoryCloseEvent event) { if (!staticMenuListener.isCloseEventBypass()) staticMenuListener.onExit(event); }
    }
}
