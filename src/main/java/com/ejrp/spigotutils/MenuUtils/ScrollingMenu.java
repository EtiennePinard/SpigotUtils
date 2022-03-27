package com.ejrp.spigotutils.MenuUtils;

import com.ejrp.spigotutils.ItemsUtils.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * This class is an implementation of a scrolling inventory.
 * This is the index 0 is at the top left, and you could go all the way to Integer.MAX_VALUE if you wanted to.
 * It starts at the top, and you can only go down at the beginning.
 * You can set the line that you start at using the goToLine() method.
 * Scrolling up means going to higher index and scrolling down means going to lower index.
 */
public class ScrollingMenu extends Menu {

    private final String name;
    private final int size;
    private final Inventory inventory;
    private Consumer<InventoryClickEvent> lowerInventoryListener;
    private final Map<Integer, MenuItem> items;
    private Menu parent;

    private int lineScrolledOnScroll;
    private Material scrollMaterial;
    private ItemStack scrollUpItem;
    private ItemStack scrollDownItem;
    private InventoryCorner scrollUpInventoryCorner;
    private InventoryCorner scrollDownInventoryCorner;
    private Sound scrollSound;
    private Sound cannotScrollSound;
    private boolean resetOnClose;

    private int lineWeAreOn;

    /**
     * Creates a new instance of a scrolling menu, with the specified parameters.
     * @param plugin The plugin to register the listeners to.
     * @param name The name of this menu
     * @param size The size of this menu. Keep in mind that the inventory will keep the same size has you scroll.
     * @param lowerInventoryListener The listener to apply when the player clicks his inventory while viewing this one
     * @param items The items that are contained in this inventory. The index only have to be bigger than 0, if not, they will be ignored.
     * @param parent The inventory to open if when this one closes.
     * @param lineScrolledOnScroll The amount of line that are scrolled on click. So you can skip one line if you set this value to 2.
     * @param scrollMaterial The material use by the item that when clicked will scroll the inventory up or down.
     * @param scrollUpInventoryCorner The corner of the item that will scroll the menu up when clicked.
     * @param scrollDownInventoryCorner The corner of the item that will scroll the menu down when clicked.
     * @param scrollSound The sound to play when the player scroll.
     * @param cannotScrollSound The sound to play when the player cannot scroll
     * @param resetOnClose If the inventory should return to his original position when you close it.
     * @throws IllegalArgumentException If the size is invalid, if the line scrolled on scroll is invalid,
     * if the inventory are corner are the same, or they are the same side when the inventory size is 9.
     */
    public ScrollingMenu(@NotNull JavaPlugin plugin,
                         @NotNull String name, int size,
                         @Nullable Consumer<InventoryClickEvent> lowerInventoryListener,
                         @Nullable Map<Integer, MenuItem> items,
                         @Nullable Menu parent,
                         int lineScrolledOnScroll,
                         @NotNull Material scrollMaterial,
                         @NotNull InventoryCorner scrollUpInventoryCorner,
                         @NotNull InventoryCorner scrollDownInventoryCorner,
                         @Nullable Sound scrollSound, @Nullable Sound cannotScrollSound, boolean resetOnClose)
    throws IllegalArgumentException {
        super(plugin);
        if (size % 9 != 0 || size < 9 || size > 54)
            throw new IllegalArgumentException("The size is not a multiple of nine between 9 and 54!");
        this.name = name;
        this.size = size;
        this.inventory = Bukkit.createInventory(null,this.size,this.name);
        this.lowerInventoryListener = lowerInventoryListener != null ? lowerInventoryListener : event -> {};
        this.parent = parent;

        if (lineScrolledOnScroll < 0)
            throw new IllegalArgumentException("The line scrolled on scroll needs to be a number bigger than 0!");
        this.lineScrolledOnScroll = lineScrolledOnScroll;

        this.items = items != null ? items : new HashMap<>();
        if (scrollDownInventoryCorner.ordinal() == scrollUpInventoryCorner.ordinal())
            throw new IllegalArgumentException("The corner of the scroll down and scroll up item cannot be the same!");
        if (size == 9) {
            if (scrollDownInventoryCorner.name().toLowerCase().contains("right")
                    && scrollUpInventoryCorner.name().toLowerCase().contains("right"))
                throw new IllegalArgumentException("When the inventory size is 9 (only one layer), the scroll items must be in opposites sides.");
            if (scrollDownInventoryCorner.name().toLowerCase().contains("left")
                    && scrollUpInventoryCorner.name().toLowerCase().contains("left"))
                throw new IllegalArgumentException("When the inventory size is 9 (only one layer), the scroll items must be in opposites sides.");
        }
        this.scrollUpInventoryCorner = scrollUpInventoryCorner;
        this.scrollDownInventoryCorner = scrollDownInventoryCorner;

        this.scrollSound = scrollSound;
        this.cannotScrollSound = cannotScrollSound;
        this.scrollMaterial = scrollMaterial;
        this.resetOnClose = resetOnClose;

        this.lineWeAreOn = 0;
        updateButtons();
    }

    /**
     * Creates an empty scrolling menu with no sound, a scroll material of ladder, 1 line scrolled on scroll,
     * the scroll up item in the top right and the scroll down item in the bottom right and an inventory that resets
     * on close.
     * @param plugin The plugin to register the listeners to.
     * @param name The name of this menu
     * @param size The size of this menu. Keep in mind that the inventory will keep the same size has you scroll.
     */
    public ScrollingMenu(@NotNull JavaPlugin plugin,
                         @NotNull String name, int size) {
        this(plugin,name,size,
                null, null,null, 1,
                Material.LADDER,InventoryCorner.TOP_RIGHT, InventoryCorner.BOTTOM_RIGHT,
                null,null,true);
    }

    @Override
    public void openTo(@NotNull Player... players) {
        for (Player player : players)
            player.openInventory(inventory);
    }

    @Override
    public void closeTo(@NotNull Player... players) {
        for (Player player : players)
            player.closeInventory();
    }

    @Override
    public void addItem(int index, @NotNull MenuItem item) {
        this.items.put(index, item);
        updateInventory();
    }

    @Override
    public void removeItem(int index) {
        this.items.remove(index);
        updateInventory();
    }

    @Override
    public void updateInventory() {
        updateButtons();
        ItemStack[] contents = new ItemStack[size()];
        getItems().forEach(((index, menuItem) -> {
            if (index < 0) return;
            if ((index / 9) >= lineWeAreOn && (index / 9) < (lineWeAreOn + size() / 9))
                // Index is not lower than the current line
                // and index is not bigger than the highest line that is being shown
                contents[index - 9 * lineWeAreOn] = menuItem.getItem();
        }));
        contents[scrollDownInventoryCorner.getIndex(size)] = scrollDownItem;
        contents[scrollUpInventoryCorner.getIndex(size)] = scrollUpItem;
        this.inventory.setContents(contents);
    }

    /**
     * If the player clicked his inventory while viewing this one, the lower listener consumer will be applied.
     * If the player clicked a menu item the clicked() method will be called.
     * And if the player clicked on one of the scrolled item the inventory will scroll if it can be scrolled.
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
            else if (event.getSlot() == scrollUpInventoryCorner.getIndex(size()))
                scrollUp((Player) event.getWhoClicked());
            else if (event.getSlot() == scrollDownInventoryCorner.getIndex(size()))
                scrollDown((Player) event.getWhoClicked());
            else if (items.containsKey(event.getSlot() + 9 * lineWeAreOn))
                items.get(event.getSlot() + 9 * lineWeAreOn).clicked(event);
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
     * Opens the parent inventory if the parent is not null.
     * @param event The inventory close event that has just happened.
     */
    @Override
    public void onExit(@NotNull InventoryCloseEvent event) {
        if (event.getInventory().equals(getInventory())) {
            if (resetOnClose) {
                this.lineWeAreOn = 0;
                updateInventory();
            }
            if (parent != null)
                Bukkit.getScheduler().runTaskLater(getPlugin(),
                        () -> event.getPlayer().openInventory(parent.getInventory()),
                        1);
        }
    }

    @Override public int size() { return size; }
    @Override public String name() { return name; }
    @NotNull @Override public Inventory getInventory() { return inventory; }

    /**
     * Scrolls the inventory down and plays the scroll or the cannotScroll sound.
     * @param player The player to play the sound.
     */
    public void scrollDown(@Nullable Player player) {
        int scrollAmount = this.remainingScrollAmount(ScrollDirection.DOWN);
        if (scrollAmount == 0) {
            if (player != null)
                player.playSound(player.getLocation(), cannotScrollSound, 25.0F, 50.0F);
        } else {
            this.lineWeAreOn += scrollAmount;
            if (player != null)
                player.playSound(player.getLocation(), scrollSound, 50.0F, 50.0F);
            updateInventory();
        }
    }

    /**
     * Scroll down the inventory but the player is null so no sound.
     */
    public void scrollDown() { this.scrollDown(null); }

    /**
     * Scroll up the inventory and plays the scroll or the cannotScroll sound.
     * @param player The player to play the sound
     */
    public void scrollUp(@Nullable Player player) {
        int scrollAmount = this.remainingScrollAmount(ScrollDirection.UP);
        if (scrollAmount == 0) {
            if (player != null)
                player.playSound(player.getLocation(), cannotScrollSound, 50.0F, 50.0F);
        } else {
            this.lineWeAreOn -= scrollAmount;
            if (player != null)
                player.playSound(player.getLocation(), scrollSound, 50.0F, 50.0F);
            updateInventory();
        }
    }

    /**
     * Scroll up the inventory but the player is null so no sound.
     */
    public void scrollUp() { this.scrollUp(null); }

    public void goToLine(int lineToGoTo) {
        if (lineToGoTo > (getHighestIndex() / 9) || lineToGoTo < 0)
            throw new IllegalArgumentException("You can only have go to a line between 0 and the line of the highest item index which in this case is " + getHighestIndex() / 9 + 1);
        this.lineWeAreOn = lineToGoTo;
    }

    /**
     * The maximum amount you can scroll in one direction.
     * The maximum you can scroll in any direction is the lineScrolledOnScroll parameter and the minimum is 0.
     * @param direction The direction to scroll in
     * @return If you can scroll in the specified direction
     */
    public int remainingScrollAmount(@NotNull ScrollDirection direction) {
        switch(direction) {
            case DOWN: return Math.min(lineScrolledOnScroll, this.getHighestIndex() / 9 + 1 - (lineWeAreOn + size / 9));
            case UP: return lineWeAreOn - lineScrolledOnScroll < 0 ? lineWeAreOn : lineScrolledOnScroll;
            default: return 0;
        }
    }

    /**
     * Gets the item of this inventory. There could be negative index, that are not being displayed.
     * @return The item of this inventory
     */
    public Map<Integer, MenuItem> getItems() { return items; }

    /**
     * Sets the line scrolled on scroll.
     * You will always be able to scroll to the bottom of the inventory or the top, no matter how big this value is.
     * This will set the absolute value of the value passed.
     * @param lineScrolledOnScroll The line scrolled on scroll
     */
    public void setLineScrolledOnScroll(int lineScrolledOnScroll) { this.lineScrolledOnScroll = Math.abs(lineScrolledOnScroll); }

    /**
     * Gets the line scrolled on scroll.
     * @return The line scrolled on scroll
     */
    public int getLineScrolledOnScroll() { return lineScrolledOnScroll; }

    /**
     * Sets the inventory corner of the scroll up item.
     * @param scrollUpInventoryCorner The inventory corner of the scroll up item
     */
    public void setScrollUpInventoryCorner(InventoryCorner scrollUpInventoryCorner) { this.scrollUpInventoryCorner = scrollUpInventoryCorner; }

    /**
     * Sets the inventory corner of the scroll down item.
     * @param scrollDownInventoryCorner The inventory corner of the scroll down item
     */
    public void setScrollDownInventoryCorner(InventoryCorner scrollDownInventoryCorner) { this.scrollDownInventoryCorner = scrollDownInventoryCorner; }

    /**
     * Sets the sound to play on scroll
     * @param scrollSound The sound to play on scroll
     */
    public void setScrollSound(Sound scrollSound) { this.scrollSound = scrollSound; }

    /**
     * Sets the sound to play when you cannot scroll
     * @param cannotScrollSound The sound to play when you cannot scroll
     */
    public void setCannotScrollSound(Sound cannotScrollSound) { this.cannotScrollSound = cannotScrollSound; }

    /**
     * Sets if the inventory should go back to the beginning when a player closes it.
     * @param resetOnClose If the inventory should go back to the beginning when a player closes it
     */
    public void setResetOnClose(boolean resetOnClose) { this.resetOnClose = resetOnClose; }

    /**
     * Gets if this inventory goes back to line 0 on close.
     * @return If this inventory goes back to line 0 on close.
     */
    public boolean isResetOnClose() { return resetOnClose; }

    /**
     * Sets the parent of this inventory
     * @param parent The parent of this inventory
     */
    public void setParent(@Nullable Menu parent) { this.parent = parent; }

    /**
     * Gets the parent of this menu
     * @return The parent of this menu
     */
    @Nullable public Menu getParent() { return this.parent; }

    /**
     * Sets the code to be executed when the player clicks on his inventory when he is viewing this menu.
     * @param lowerInventoryListener The listener to be invoked when the player clicks on his inventory when he is viewing this menu.
     */
    public void setLowerInventoryListener(Consumer<InventoryClickEvent> lowerInventoryListener) { this.lowerInventoryListener = lowerInventoryListener; }

    /**
     * Sets the material to use for the scroll up or down buttons
      * @param scrollMaterial The material to be used for the scroll up or down buttons
     */
    public void setScrollMaterial(Material scrollMaterial) { this.scrollMaterial = scrollMaterial; }

    /**
     * Gets the material to be used for the scroll up or down buttons
     * @return The material to be used for the scroll up or down buttons
     */
    public Material getScrollMaterial() { return scrollMaterial; }

    private int getHighestIndex() {
        return items.keySet().stream()
                .reduce((integer, integer2) -> integer >= integer2 ? integer : integer2)
                .orElse(0);
    }

    private void updateButtons() {
        String displayNameUp = this.remainingScrollAmount(ScrollDirection.UP) > 0 ?
                ChatColor.YELLOW + "Scroll Up" :
                ChatColor.GRAY + "Cannot Scroll Up!";

        String displayNameDown = this.remainingScrollAmount(ScrollDirection.DOWN) > 0 ?
                ChatColor.YELLOW + "Scroll Down" :
                ChatColor.GRAY + "Cannot Scroll Down!";

        this.scrollUpItem = new ItemBuilder(scrollMaterial).setDisplayName(displayNameUp).getItem();
        this.scrollDownItem = new ItemBuilder(scrollMaterial).setDisplayName(displayNameDown).getItem();
    }

    /**
     * The scroll direction that the inventory can scroll in.
     */
    public enum ScrollDirection {
        UP,
        DOWN
    }

    /**
     * Represents the four corners of an inventory.
     */
    public enum InventoryCorner {
        TOP_LEFT(0,1),
        TOP_RIGHT(8,1),

        BOTTOM_LEFT(0,0),
        BOTTOM_RIGHT(8,0);

        private final int x;
        private final int y;

        InventoryCorner(int x, int y) {
            this.x = x;
            this.y = y;
        }

        private int getIndex(int inventorySize) {
            int height = inventorySize / 9 - 1;
            return y * height * 9 + x;
        }
    }
}
