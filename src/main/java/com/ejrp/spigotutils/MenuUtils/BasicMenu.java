package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Is a basic implementation of the Menu abstract class.
 * The only fancy thing it does is that it has a parent menu.
 * When this inventory is closed, the parent menu inventory will be opened
 * only if the parent menu is not null.
 */
public class BasicMenu extends StaticMenu {

    @NotNull private Consumer<InventoryClickEvent> lowerInventoryListener;
    @NotNull private final MenuItem[] items;
    @Nullable private StaticMenu parent;

    /**
     * Creates a new BasicMenu with the specified parameters
     * @param plugin The plugin to register the listener to
     * @param name The name of this inventory
     * @param size The size of this inventory
     * @param lowerInventoryListener The code to execute when the player is clicked when he is viewing this inventory
     * @param items The items contained in this inventory
     * @param parent The parent of this menu. This menu will open when this one is clicked.
     * @throws IllegalArgumentException If an index of an item is invalid.
     */
    public BasicMenu(@NotNull JavaPlugin plugin,
                     String name,
                     int size,
                     @Nullable Consumer<InventoryClickEvent> lowerInventoryListener,
                     @Nullable Map<Integer, MenuItem> items,
                     @Nullable StaticMenu parent) throws IllegalArgumentException {
        super(plugin, name, size);
        this.lowerInventoryListener = lowerInventoryListener != null ? lowerInventoryListener : event -> {};
        this.items = new MenuItem[size()];
        if (items != null)
            items.forEach((slot, menuItem) -> {
                if (slot > size && slot < 0)
                    throw new IllegalArgumentException("You can only have items between the 0 and the inventory size - 1!");
                this.items[slot] = menuItem;
            });
        this.parent = parent;
    }

    /**
     * Creates an empty BasicMenu
     * @param plugin The plugin to register the inventory events to
     * @param name The name of the inventory
     * @param size The size of the inventory
     */
    public BasicMenu(@NotNull JavaPlugin plugin, String name, int size) { this(plugin, name, size, event -> {},null,null); }

    @Override
    public void addItem(int index, @NotNull MenuItem item) {
        if (index < 0 || index > size())
            throw new IllegalArgumentException("The index needs to be between the size of the inventory and 0!");
        Objects.requireNonNull(item,"The menu item is null! If you want to delete an item, use the delete() method!");
        this.items[index] = item;
        updateInventory();
    }

    @Override
    public void removeItem(int index) {
        if (index < 0 || index > size())
            throw new IllegalArgumentException("The index needs to be between the size of the inventory and 0!");
        this.items[index] = null;
        updateInventory();
    }

    @Override
    public void openTo(@NotNull Player... players) {
        for (Player player : players)
            player.openInventory(getInventory());
    }

    @Override
    public void closeTo(@NotNull Player... players) {
        for (Player player : players)
            player.closeInventory();
    }

    @Override
    public void updateInventory() {
        ItemStack[] contents = new ItemStack[size()];
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null)
                contents[i] = items[i].getItem();
        }
        getInventory().setContents(contents);
    }

    @NotNull public MenuItem[] getItems() { return this.items; }

    /**
     * Sets the parent of this menu.
     * @param parent The new parent of this menu
     */
    public void setParent(@Nullable StaticMenu parent) { this.parent = parent; }

    /**
     * Gets the parent of this menu
     * @return The parent of this menu
     */
    @Nullable public StaticMenu getParent() { return this.parent; }

    /**
     * Sets the code to be executed when the player clicks on his inventory when he is viewing this menu.
     * @param lowerInventoryListener The listener to be invoked when the player clicks on his inventory when he is viewing this menu.
     */
    public void setLowerInventoryListener(@NotNull Consumer<InventoryClickEvent> lowerInventoryListener) { this.lowerInventoryListener = lowerInventoryListener; }

    /**
     * Gets the listener to be invoked when the player clicks on his inventory when he is viewing this one
     * @return The listener to be invoked when the player clicks on his inventory when he is viewing this one
     */
    @NotNull public Consumer<InventoryClickEvent> getLowerInventoryListener() { return this.lowerInventoryListener; }

    /**
     * Invokes the clicked() method if the player clicked a menu item
     * otherwise, applies the lower inventory listener.
     * @param event The inventory click event that has just happened.
     */
    @Override
     public void onClick(@NotNull InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        if (event.getInventory().equals(getInventory())) {
            event.setCancelled(true);
            if (event.getClickedInventory() instanceof PlayerInventory)
                lowerInventoryListener.accept(event);
            else if (items[event.getSlot()] != null)
                items[event.getSlot()].clicked(event);
        }
    }

    /**
     * Cancels the event.
     * @param event The inventory drag event that has just happened.
     */
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
}
