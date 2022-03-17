package com.ejrp.spigotutils.MenuUtils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
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
public class BasicMenu extends Menu {

    private final String name;
    private final int size;
    private final Inventory inventory;
    private Consumer<InventoryClickEvent> lowerInventoryListener;
    private final MenuItem[] items;
    private Menu parent;

    /**
     * Creates a new BasicMenu with the specified parameters
     * @param plugin The plugin to register the listener to
     * @param name The name of this inventory
     * @param size The size of this inventory
     * @param lowerInventoryListener The code to execute when the player is clicked when he is viewing this inventory
     * @param items The items contained in this inventory
     * @param parent The parent of this menu. This menu will open when this one is clicked.
     * @throws IllegalArgumentException If the size is not a multiple of nine between 9 and 54 or if an index of an item is invalid.
     */
    public BasicMenu(@NotNull JavaPlugin plugin,
                     String name,
                     int size,
                     @Nullable Consumer<InventoryClickEvent> lowerInventoryListener,
                     @Nullable Map<Integer, MenuItem> items,
                     @Nullable Menu parent) throws IllegalArgumentException {
        super(plugin);
        if (size % 9 != 0 || size < 9 || size > 54)
            throw new IllegalArgumentException("The size is not a multiple of nine between 9 and 54!");
        this.name = name;
        this.size = size;
        this.lowerInventoryListener = lowerInventoryListener != null ? lowerInventoryListener : event -> {};
        this.items = new MenuItem[this.size];
        if (items != null)
            items.forEach((integer, menuItem) -> {
                if (integer < size && integer >= 0)
                    this.items[integer] = menuItem;
                else
                    throw new IllegalArgumentException("You can only have items between the 0 and the inventory size - 1!");
            });
        this.parent = parent;
        this.inventory = Bukkit.createInventory(null, size, this.name);
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
            player.openInventory(this.inventory);
    }

    @Override
    public void closeTo(@NotNull Player... players) {
        for (Player player : players)
            player.closeInventory();
    }

    @Override
    public void updateInventory() {
        ItemStack[] contents = new ItemStack[size];
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null)
                contents[i] = items[i].getItem();
        }
        inventory.setContents(contents);
    }

    @Override @NotNull public Inventory getInventory() { return this.inventory; }
    @Override public int size() { return this.size; }
    @Override public String name() { return this.name; }
    @NotNull public MenuItem[] getItems() { return this.items; }

    /**
     * Sets the parent of this menu.
     * @param parent The new parent of this menu
     */
    public void setParent(@Nullable BasicMenu parent) { this.parent = parent; }

    /**
     * Gets the parent of this menu
     * @return The parent of this menu
     */
    @Nullable public Menu getParent() { return this.parent; }

    public void setLowerInventoryListener(Consumer<InventoryClickEvent> lowerInventoryListener) { this.lowerInventoryListener = lowerInventoryListener; }
    public Consumer<InventoryClickEvent> getLowerInventoryListener() { return this.lowerInventoryListener; }

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
