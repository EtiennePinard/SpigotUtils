package com.ejrp.spigotutils.MenuUtils;

import org.apache.commons.lang.Validate;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

/**
 * This is an abstract implementation of the Menu class.
 * This class has one field, a final MenuItem array. It is an
 * array because the size of this menu will not change.
 * Extend this class to create your own custom static menus.
 */
public abstract class StaticSizeMenu extends MenuListener {

    @NotNull private final MenuItem[] items;
    @NotNull private final Inventory inventory;

    /**
     * Creates a new StaticSizeMenu object with the given parameters.
     * @param plugin The plugin to register the listeners to.
     * @param name The name of the inventory.
     * @param size The size of the inventory.
     * @param owner The owner of the inventory, null of there is no owner.
     * @param items The items in this inventory.
     */
    public StaticSizeMenu(@NotNull JavaPlugin plugin, @NotNull String name, int size, @Nullable InventoryHolder owner, @NotNull MenuItem[] items) {
        super(plugin);
        this.inventory = Bukkit.createInventory(owner,size,name);
        this.items = items;
    }

    /**
     * Creates a new StaticSizeMenu object with the given parameters.
     * @param plugin The plugin to register the listeners to.
     * @param name The name of the inventory.
     * @param size The size of the inventory.
     * @param owner The owner of the inventory, null of there is no owner.
     * @param items The items in this inventory, null for an empty inventory.
     */
    public StaticSizeMenu(@NotNull JavaPlugin plugin, @NotNull String name, int size, @Nullable InventoryHolder owner, @Nullable Map<Integer, ? extends MenuItem> items) {
        super(plugin);
        this.inventory = Bukkit.createInventory(owner,size,name);
        this.items = new MenuItem[size()];
        if (items != null) items.forEach(this::addItem);
    }

    @Override
    public void addItem(int index, @NotNull MenuItem item) {
        Validate.isTrue(index < 0 || index > size(),"The index needs to be between the size of the inventory and 0!");
        Objects.requireNonNull(item,"The menu item is null! If you want to delete an item, use the delete() method!");
        this.items[index] = item;
        updateInventory();
    }

    @Override
    public void removeItem(int index) {
        Validate.isTrue(index < 0 || index > size(),"The index needs to be between the size of the inventory and 0!");
        this.items[index] = null;
        updateInventory();
    }

    @Override
    @NotNull public final String name() { return inventory.getName(); }

    @Override
    public final int size() { return inventory.getSize(); }

    @Override
    @NotNull public final Inventory getInventory() { return inventory; }

    /**
     * Updates the contents of the inventory.
     * Call this method if the content of the inventory
     * and the MenuItem array are out of sync.
     */
    @Override
    public void updateInventory() {
        ItemStack[] contents = new ItemStack[size()];
        for (int i = 0; i < items.length; i++)
            contents[i] = items[i].getItem();
        getInventory().setContents(contents);
    }

    /**
     * Gets the item of this inventory.
     * @return The item of this inventory
     */
    @NotNull public final MenuItem[] getItems() { return this.items; }

}
