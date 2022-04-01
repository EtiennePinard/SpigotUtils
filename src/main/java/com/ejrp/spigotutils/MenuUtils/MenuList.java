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

    private final List<StaticMenu> staticMenus;
    private final ItemStack next;
    private final ItemStack back;

    /**
     * Creates a new instance of MenuChain
     * @param next The material for the next "button"
     * @param back The material for the back "button"
     * @param staticMenus The menus of this chain
     */
    public MenuList(Material next, Material back, @Nullable List<StaticMenu> staticMenus) {
        this.next = new ItemBuilder(next)
                .setDisplayName(ChatColor.GRAY + "Next")
                .getItem();

        this.back = new ItemBuilder(back)
                .setDisplayName(ChatColor.GRAY + "Back")
                .getItem();

        this.staticMenus = staticMenus != null ? staticMenus : new ArrayList<>();
        this.updateMenus();
    }

    /**
     * Adds a menu at the specified index
     * @param index The index of the menu to add
     * @param menu The menu to add
     */
    public void addMenu(int index, BasicMenu menu) { this.staticMenus.add(index, menu); updateMenus(); }

    /**
     * Adds a menu at the end of the list of menus
     * @param menu The menu to add
     */
    public void addMenu(BasicMenu menu) { addMenu(staticMenus.size(),menu); }

    private void updateMenus() {

        for (int index = 0; index < this.staticMenus.size(); index++) {
            StaticMenu staticMenu = this.staticMenus.get(index);
            staticMenu.setBypassCloseEvent(true);
            int lastSlot = staticMenu.size() - 1;
            StaticMenu next = this.staticMenus.size() == index + 1 ? staticMenu : this.staticMenus.get(index + 1);
            StaticMenu back = index == 0 ? staticMenu : this.staticMenus.get(index - 1);

            // Setting the back and the next item to the bottom right and left corner
            if (staticMenus.size() - 1 > index && index > 0) {
                staticMenu.addItem(lastSlot - 8, new MenuItem(this.back) {
                    @Override
                    public void clicked(@NotNull InventoryClickEvent event) {
                        if (event.getWhoClicked() instanceof Player)
                        back.openTo((Player) event.getWhoClicked());
                    }
                });
                staticMenu.addItem(lastSlot, new MenuItem(this.next) {
                    @Override
                    public void clicked(@NotNull InventoryClickEvent event) {
                        if (event.getWhoClicked() instanceof Player)
                            next.openTo((Player) event.getWhoClicked());
                    }
                });
            } else if (index == 0) {
                staticMenu.addItem(lastSlot, new MenuItem(this.next) {
                    @Override
                    public void clicked(@NotNull InventoryClickEvent event) {
                        if (event.getWhoClicked() instanceof Player)
                            next.openTo((Player) event.getWhoClicked());
                    }
                });
            } else {
                staticMenu.addItem(lastSlot - 8, new MenuItem(this.back) {
                    @Override
                    public void clicked(@NotNull InventoryClickEvent event) {
                        if (event.getWhoClicked() instanceof Player)
                            back.openTo((Player) event.getWhoClicked());
                    }
                });
            }
        }
    }

    /**
     * Gets the Menu at the specific location in the list.
     * @param index The index of the Menu in the list.
     * @return The menu at the specific location in the list.
     * @throws IndexOutOfBoundsException If the index is out of bounds
     */
    public StaticMenu getInventoryAt(int index) throws IndexOutOfBoundsException { return this.staticMenus.get(index); }

    /**
     * Gets the list of Menus that this MenuList has.
     * @return The list of Menus that this MultiMenu has.
     */
    public List<StaticMenu> getMenus() { return this.staticMenus; }
}
