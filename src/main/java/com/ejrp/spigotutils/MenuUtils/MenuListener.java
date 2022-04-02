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
 * in this library (StaticMenu, DynamicMenu, ScrollingMenu)
 */
public abstract class MenuListener {

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

    /**
     * Gets called when a click happens in an inventory
     * @param event The inventory click event that has just happened.
     */
    public abstract void onClick(@NotNull InventoryClickEvent event);

    /**
     * Gets called when an item is dragged in an inventory.
     * @param event The inventory drag event that has just happened.
     */
    public abstract void onDrag(@NotNull InventoryDragEvent event);

    /**
     * Gets called when an inventory is closed.
     * @param event The inventory close event that has just happened.
     */
    public abstract void onExit(@NotNull InventoryCloseEvent event);

    /**
     * Gets the plugin that the listeners are registered to.
     * @return The plugin that the listeners are registered to.
     */
    @NotNull public final JavaPlugin getPlugin() { return this.plugin; }

    /**
     * Sets if the inventory click event should be bypassed
     * @param bypassClickEvent If the inventory click event should be bypassed
     */
    public final void setBypassClickEvent(boolean bypassClickEvent) { this.bypassClickEvent = bypassClickEvent; }

    /**
     * Sets if the inventory drag event should be bypassed
     * @param bypassDragEvent If the inventory drag event should be bypassed
     */
    public final void setBypassDragEvent(boolean bypassDragEvent) { this.bypassDragEvent = bypassDragEvent; }

    /**
     * Sets if the inventory close event should be bypassed
     * @param bypassCloseEvent If the inventory close event should be bypassed
     */
    public final void setBypassCloseEvent(boolean bypassCloseEvent) { this.bypassCloseEvent = bypassCloseEvent; }

    /**
     * Gets if the inventory click event is bypassed
     * @return If the inventory`click event is bypassed
     */
    public final boolean isBypassClickEvent() { return bypassClickEvent; }

    /**
     * Gets if the inventory drag event is bypassed
     * @return If the inventory`drag event is bypassed
     */
    public final boolean isBypassDragEvent() { return bypassDragEvent; }

    /**
     * Gets if the inventory close event is bypassed
     * @return If the inventory`close event is bypassed
     */
    public final boolean isBypassCloseEvent() { return bypassCloseEvent; }

    /**
     * This is the class that is used to register the inventory listener
     * from the Menu class because you cannot register listener from an abstract class.
     */
    public static final class MenuListenerRegister implements Listener {

        private final MenuListener staticMenuListener;

        private MenuListenerRegister(MenuListener staticMenuListener) { this.staticMenuListener = staticMenuListener; }

        @EventHandler
        public void onClick(@NotNull InventoryClickEvent event) { if (!staticMenuListener.isBypassClickEvent()) staticMenuListener.onClick(event); }

        @EventHandler
        public void onDrag(@NotNull InventoryDragEvent event) { if (!staticMenuListener.isBypassDragEvent()) staticMenuListener.onDrag(event); }

        @EventHandler
        public void onExit(@NotNull final InventoryCloseEvent event) { if (!staticMenuListener.isBypassCloseEvent()) staticMenuListener.onExit(event); }
    }
}
