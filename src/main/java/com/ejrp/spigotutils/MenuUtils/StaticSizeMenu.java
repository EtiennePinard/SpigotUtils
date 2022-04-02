package com.ejrp.spigotutils.MenuUtils;

import org.apache.commons.lang.Validate;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Objects;

public abstract class StaticSizeMenu extends Menu {

    @NotNull private final MenuItem[] items;

    public StaticSizeMenu(@NotNull JavaPlugin plugin, @NotNull String name, int size, @Nullable InventoryHolder owner, @NotNull MenuItem[] items) {
        super(plugin, name, size, owner);
        this.items = items;
    }

    public StaticSizeMenu(@NotNull JavaPlugin plugin, @NotNull String name, int size, @Nullable InventoryHolder owner, @Nullable Map<Integer, MenuItem> items) {
        super(plugin, name, size, owner);
        this.items = new MenuItem[size()];
        if (items != null) items.forEach(this::addItem);
    }

    @Override
    public final void addItem(int index, @NotNull MenuItem item) {
        Validate.isTrue(index < 0 || index > size(),"The index needs to be between the size of the inventory and 0!");
        Objects.requireNonNull(item,"The menu item is null! If you want to delete an item, use the delete() method!");
        this.items[index] = item;
        updateInventory();
    }

    @Override
    public final void removeItem(int index) {
        Validate.isTrue(index < 0 || index > size(),"The index needs to be between the size of the inventory and 0!");
        this.items[index] = null;
        updateInventory();
    }

    /**
     * Updates the contents of the inventory.
     * Call this method if the content of the inventory
     * and the MenuItem array are out of sync.
     */
    @Override
    public final void updateInventory() {
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
