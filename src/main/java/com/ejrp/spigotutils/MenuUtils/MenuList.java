package com.ejrp.spigotutils.MenuUtils;

import com.ejrp.spigotutils.ItemsUtils.ItemBuilder;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * An implementation of a list of menus that are linked together. When created it will two things to the list of Menu that you will provide.
 * It will add two items on the bottom left and bottom right corners of each of the inventories.
 * These buttons will be a back and a next "button". The exact index of the back item is (menu.size() - 9)
 * and of the next item is (menu.size() - 1). It will also bypass the inventory close event of each inventory.
 * The order of the menu are the order of the list.
 */
public class MenuList {

    private final List<Menu> standardMenus;
    private final ItemStack next;
    private final ItemStack back;

    /**
     * Creates a new instance of MenuChain
     * @param next The material for the next "button"
     * @param back The material for the back "button"
     * @param standardMenus The menus of this chain
     */
    public MenuList(Material next, Material back, @Nullable List<Menu> standardMenus) {
        this.next = new ItemBuilder(next)
                .setDisplayName(ChatColor.GRAY + "Next")
                .getItem();

        this.back = new ItemBuilder(back)
                .setDisplayName(ChatColor.GRAY + "Back")
                .getItem();

        this.standardMenus = standardMenus != null ? standardMenus : new ArrayList<>();
        this.updateMenus();
    }

    /**
     * Adds a menu at the specified index
     * @param index The index of the menu to add
     * @param menu The menu to add
     */
    public void addMenu(int index, Menu menu) { this.standardMenus.add(index, menu); updateMenus(); }

    /**
     * Adds a menu at the end of the list of menus
     * @param menu The menu to add
     */
    public void addMenu(Menu menu) { addMenu(standardMenus.size(),menu); }

    private void updateMenus() {

        for (int index = 0; index < this.standardMenus.size(); index++) {
            Menu standardMenu = this.standardMenus.get(index);
            standardMenu.setBypassCloseEvent(true);
            int lastSlot = standardMenu.size() - 1;
            Menu next = this.standardMenus.size() == index + 1 ? standardMenu : this.standardMenus.get(index + 1);
            Menu back = index == 0 ? standardMenu : this.standardMenus.get(index - 1);

            if (standardMenus.size() - 1 > index) // Any menu except the last one
                addNavigationArrow(standardMenu, next, lastSlot, this.next);
            if (index > 0) // Any menu except the first one
                addNavigationArrow(standardMenu, back, lastSlot - 8, this.back);
        }
    }

    private void addNavigationArrow(@NotNull Menu menuToAddItemTo, @NotNull Menu menuToNavigateTo, int slot, ItemStack navigateItem) {
        menuToAddItemTo.addItem(slot, new MenuItem(navigateItem) {
            @Override
            public void clicked(@NotNull InventoryClickEvent event) {
                if (event.getWhoClicked() instanceof Player)
                    event.getWhoClicked().openInventory(menuToNavigateTo.getInventory());
            }
        });
    }

    /**
     * Gets the Menu at the specific location in the list.
     * @param index The index of the Menu in the list.
     * @return The menu at the specific location in the list.
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    public Menu getInventoryAt(int index) throws IndexOutOfBoundsException { return this.standardMenus.get(index); }

    /**
     * Gets the list of Menus that this MenuList has.
     * @return The list of Menus that this MultiMenu has.
     */
    public List<Menu> getMenus() { return this.standardMenus; }
}
