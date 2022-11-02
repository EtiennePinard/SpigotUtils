package com.ejrp.spigotutils.menuUtils

import com.ejrp.spigotutils.itemsUtils.ItemBuilder
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.PlayerInventory
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

/**
 * This class is an abstract implementation of an inventory that scrolls up and down.
 * This is the index 0 is at the top left, and you could go all the way to Integer.MAX_VALUE if you wanted to.
 * It starts at the top, and you can only go down at the beginning.
 * You can set the line that you start at using the goToLine() method.
 * Scrolling up means going to higher index and scrolling down means going to lower index.
 * @param plugin The plugin to register the listeners to.
 * @param name The name of this menu
 * @param size The size of this menu. Keep in mind that the inventory will keep the same size has you scroll.
 * @param items The items that are contained in this inventory. The index only have to be bigger than 0, if not, they will be ignored.
 * @param parent The inventory to open if when this one closes.
 * @param lineScrolledOnScroll The amount of line that are scrolled on click. So you can skip one line if you set this value to 2.
 * @param scrollMaterial The material use by the item that when clicked will scroll the inventory up or down.
 * @param scrollUpInventoryCorner The corner of the item that will scroll the menu up when clicked.
 * @param scrollDownInventoryCorner The corner of the item that will scroll the menu down when clicked.
 * @param scrollSound The sound to play when the player scroll.
 * @param cannotScrollSound The sound to play when the player cannot scroll
 * @param isResetOnClose If the inventory should return to his original position when you close it.
 * @throws IllegalArgumentException If the size is invalid, if the line scrolled on scroll is invalid,
 * if the inventory are corner are the same, or they are the same side when the inventory size is 9.
 */
abstract class ScrollingMenu(
    plugin: JavaPlugin,
    name: String,
    size: Int,
    private val items: MutableMap<Int, MenuItem> = HashMap(),
    private var parent: GenericMenu? = null,
    lineScrolledOnScroll: Int = 1,
    var scrollMaterial: Material = Material.LADDER,
    var scrollUpInventoryCorner: InventoryCorner = InventoryCorner.TOP_RIGHT,
    var scrollDownInventoryCorner: InventoryCorner = InventoryCorner.BOTTOM_RIGHT,
    var scrollSound: Sound? = null,
    var cannotScrollSound: Sound? = null,
    var isResetOnClose: Boolean = true
) : MenuListener(plugin), GenericMenu {
    var lineScrolledOnScroll: Int = lineScrolledOnScroll
    set(value) {
        require(value > 0) { "Cannot scroll a negative amount of lines!" }
        field = value
    }
    final override val inventory: Inventory = Bukkit.createInventory(null, size, name)
    private var scrollUpItem: ItemStack? = null
    private var scrollDownItem: ItemStack? = null
    private var lineWeAreOn: Int = 0

    init {
        require(lineScrolledOnScroll > 0) { "The line scrolled on scroll needs to be a number bigger than 0!" }
        require(scrollDownInventoryCorner != scrollUpInventoryCorner) { "The corner of the scroll down and scroll up item cannot be the same!" }
        if (size == 9) {
            require(
                !(scrollDownInventoryCorner.name.lowercase().contains("right")
                        && scrollUpInventoryCorner.name.lowercase().contains("right"))
            ) { "When the inventory size is 9 (only one layer), the scroll items must be in opposites sides." }
            require(
                !(scrollDownInventoryCorner.name.lowercase().contains("left")
                        && scrollUpInventoryCorner.name.lowercase().contains("left"))
            ) { "When the inventory size is 9 (only one layer), the scroll items must be in opposites sides." }
        }
        updateButtons()
    }

    fun addItem(index: Int, item: MenuItem) {
        items[index] = item
        updateInventory()
    }

    override fun removeItem(index: Int) {
        items.remove(index)
        updateInventory()
    }

    override fun updateInventory() {
        updateButtons()
        val contents = arrayOfNulls<ItemStack>(size)
        getItems().forEach { (index: Int, menuItem: MenuItem) ->
            if (index < 0) return
            if (index / 9 >= lineWeAreOn && index / 9 < lineWeAreOn + size / 9)
                /* Index is not lower than the current line and index is not bigger than the highest line that is being shown */
                contents[index - 9 * lineWeAreOn] = menuItem.item
        }
        contents[scrollDownInventoryCorner.getIndex(size)] = scrollDownItem
        contents[scrollUpInventoryCorner.getIndex(size)] = scrollUpItem
        inventory.contents = contents
    }

    /**
     * This is the code that will be executed when the player inventory
     * is clicked when he is viewing this inventory.
     * Override this method if you want to execute some code when this event happens.
     * @param event The inventory clicked event that just occurred
     */
    open fun onPlayerInventoryClick(event: InventoryClickEvent) {}

    /**
     * If the player clicked his inventory while viewing this one the onPlayerInventoryClick() method will be invoked.
     * If the player clicked a menu item the clicked() method will be called.
     * And if the player clicked on one of the scrolled item the inventory will scroll if it can be scrolled.
     * @param event The inventory click event that has just happened.
     */
    override fun onClick(event: InventoryClickEvent) {
        if (event.clickedInventory == null) return
        if (event.whoClicked !is Player) return
        if (event.inventory == inventory) {
            event.isCancelled = true
            if (event.clickedInventory is PlayerInventory)
                onPlayerInventoryClick(event)
            else if (event.slot == scrollUpInventoryCorner.getIndex(size))
                scrollUp(event.whoClicked as Player)
            else if (event.slot == scrollDownInventoryCorner.getIndex(size))
                scrollDown(event.whoClicked as Player)
            else if (items.containsKey(event.slot + 9 * lineWeAreOn))
                items[event.slot + 9 * lineWeAreOn]!!.clicked(event)
        }
    }

    /**
     * Cancels the drag event.
     * @param event The inventory drag event that has just happened.
     */
    override fun onDrag(event: InventoryDragEvent) {
        if (event.inventory == inventory) event.isCancelled = true
    }

    /**
     * Opens the parent inventory if the parent is not null.
     * @param event The inventory close event that has just happened.
     */
    override fun onExit(event: InventoryCloseEvent) {
        if (event.inventory != inventory) return
        if (isResetOnClose) {
            lineWeAreOn = 0
            updateInventory()
        }
        if (parent != null) Bukkit.getScheduler().runTaskLater(
            plugin,
            { event.player.openInventory(parent!!.inventory) },
            1)

    }

    /**
     * Scrolls the inventory down and plays the scroll or the cannotScroll sound.
     * @param player The player to play the sound.
     */
    @JvmOverloads
    fun scrollDown(player: Player? = null) {
        val scrollAmount = remainingScrollAmount(ScrollDirection.DOWN)
        if (scrollAmount == 0) {
            player?.playSound(player.location, cannotScrollSound, 25.0f, 50.0f)
        } else {
            lineWeAreOn += scrollAmount
            player?.playSound(player.location, scrollSound, 50.0f, 50.0f)
            updateInventory()
        }
    }

    /**
     * Scroll up the inventory and plays the scroll or the cannotScroll sound.
     * @param player The player to play the sound
     */
    @JvmOverloads
    fun scrollUp(player: Player? = null) {
        val scrollAmount = remainingScrollAmount(ScrollDirection.UP)
        if (scrollAmount == 0) {
            player?.playSound(player.location, cannotScrollSound, 50.0f, 50.0f)
        } else {
            lineWeAreOn -= scrollAmount
            player?.playSound(player.location, scrollSound, 50.0f, 50.0f)
            updateInventory()
        }
    }

    /**
     * Sets the current line to the lineToGoTo arg.
     * @param lineToGoTo The line to go to
     */
    fun goToLine(lineToGoTo: Int) {
        require(lineToGoTo > highestIndex / 9 || lineToGoTo < 0)
        { "You can only have go to a line between 0 and the line of the highest item index which in this case is ${highestIndex / 9 + 1}" }
        lineWeAreOn = lineToGoTo
        updateInventory()
    }

    /**
     * The maximum amount you can scroll in one direction.
     * The maximum you can scroll in any direction is the lineScrolledOnScroll parameter and the minimum is 0.
     * @param direction The direction to scroll in
     * @return If you can scroll in the specified direction
     */
    fun remainingScrollAmount(direction: ScrollDirection): Int {
        return when (direction) {
            ScrollDirection.DOWN -> lineScrolledOnScroll.coerceAtMost(highestIndex / 9 + 1 - (lineWeAreOn + size / 9))
            ScrollDirection.UP -> if (lineWeAreOn - lineScrolledOnScroll < 0) lineWeAreOn else lineScrolledOnScroll
        }
    }

    /**
     * Gets the item of this inventory. There could be negative index, that are not being displayed.
     * @return The item of this inventory
     */
    fun getItems(): Map<Int, MenuItem> = items

    private val highestIndex: Int
        get() = items.keys.stream()
            .reduce { integer: Int, integer2: Int -> if (integer >= integer2) integer else integer2 }
            .orElse(0)

    private fun updateButtons() {
        val displayNameUp =
            if (remainingScrollAmount(ScrollDirection.UP) > 0) ChatColor.YELLOW.toString() + "Scroll Up"
            else ChatColor.GRAY.toString() + "Cannot Scroll Up!"
        val displayNameDown =
            if (remainingScrollAmount(ScrollDirection.DOWN) > 0) ChatColor.YELLOW.toString() + "Scroll Down"
            else ChatColor.GRAY.toString() + "Cannot Scroll Down!"
        scrollUpItem = ItemBuilder(scrollMaterial).setDisplayName(displayNameUp).getItem()
        scrollDownItem = ItemBuilder(scrollMaterial).setDisplayName(displayNameDown).getItem()
    }

    /**
     * The scroll direction that the inventory can scroll in.
     */
    enum class ScrollDirection { UP, DOWN }

    /**
     * Represents the four corners of an inventory.
     */
    enum class InventoryCorner(private val x: Int, private val y: Int) {
        TOP_LEFT(0, 1), TOP_RIGHT(8, 1), BOTTOM_LEFT(0, 0), BOTTOM_RIGHT(8, 0);
        fun getIndex(inventorySize: Int): Int {
            val height = inventorySize / 9 - 1
            return y * height * 9 + x
        }
    }
}