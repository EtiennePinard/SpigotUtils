package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public abstract class PlayerSpecificMenu extends StaticSizeMenu {

    @Nullable private Menu parent;
    @NotNull private final UUID playerUUID;

    /**
     * Creates a new BasicMenu with the specified parameters
     * @param plugin The plugin to register the listener to
     * @param name The name of this inventory
     * @param size The size of this inventory
     * @param items The items contained in this inventory
     * @param parent The parent of this menu. This menu will open when this one is clicked.
     * @param playerUUID The UUID of the player that this inventory belongs to
     * @throws IllegalArgumentException If an index of an item is invalid.
     */
    public PlayerSpecificMenu(@NotNull JavaPlugin plugin,
                              String name,
                              int size,
                              @Nullable Map<Integer, MenuItem> items,
                              @Nullable Menu parent,
                              @NotNull UUID playerUUID) {
        super(plugin,name,size,Bukkit.getPlayer(playerUUID),items);
        this.parent = parent;
        this.playerUUID = playerUUID;
    }

    /**
     * Sets the parent of this menu.
     * @param parent The new parent of this menu
     */
    public final void setParent(@Nullable Menu parent) { this.parent = parent; }

    /**
     * Gets the parent of this menu
     * @return The parent of this menu
     */
    @Nullable public final Menu getParent() { return this.parent; }

    /**
     * This is the code that will be executed when the player inventory
     * is clicked when he is viewing this inventory.
     * Override this method if you want to execute some code when this event happens.
     * @param event The inventory clicked event that just occurred
     */
    public void onPlayerInventoryClick(@NotNull InventoryClickEvent event) {}

    @Override
    public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        if (event.getInventory().equals(getInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() instanceof PlayerInventory)
                onPlayerInventoryClick(event);
            else if (getItems()[event.getSlot()] != null)
                getItems()[event.getSlot()].clicked(event);
        }
    }

    @Override
    public void onDrag(@NotNull InventoryDragEvent event) {
        if (event.getInventory().equals(getInventory()))
            event.setCancelled(true);
    }

    /**
     * Opens the parent inventory if it is not null.
     * @param event The inventory close event that has just happened.
     */
    @Override
    public void onExit(@NotNull InventoryCloseEvent event) {
        if (event.getInventory().equals(getInventory())) {
            if (parent != null)
                Bukkit.getScheduler().runTaskLater(getPlugin(),
                        () -> event.getPlayer().openInventory(parent.getInventory()),
                        1);
        }
    }

    public void openInventory() {
        if (Bukkit.getPlayer(playerUUID) != null)
            Bukkit.getPlayer(playerUUID).openInventory(getInventory());
    }

    /**
     * Gets the UUID of the player that this inventory belongs to
     * @return The UUID of the player that this inventory belongs to
     */
    @NotNull public final UUID getPlayerUUID() { return playerUUID; }
}
