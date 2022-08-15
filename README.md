# Spigot Utils

Spigot Utils is a library to aid you in Spigot plugin development. 
It currently contains some command utilities, menu utilities and item utilities.
If you want to use this library in your plugin, you will have to either clone this repo 
and run mvn clean install or download the jars in the release and add them to your local maven repo.
You can now add the Spigot Utils dependency to your project!

## Add Spigot Utils to your project

Once you have added the jars in your local maven repository, 
here is how you can add the Spigot Utils dependency to your poject.

```xml
<dependency>
    <groupId>com.ejrp</groupId>
    <artifactId>SpigotUtils</artifactId>
    <version>1.0</version>
</dependency>
```

## Code examples: 

This is how to create a command using the PluginCommand class:

```java
import com.ejrp.spigotutils.commandUtils.*;

@CommandInfo(name = "example", permission = "admin", requiresPlayer = true)
public class CommandExample {

    @Override
    public void execute(Player player, String[] args) {
        // If requiresPlayer is equal false, then override the execute method with the commandSender parameter
    }

}
```

This is how to create an item and add lore:

```java
ItemStack item = new ItemBuilder(Material.DIAMOND)
        .setItemAmount(42)
        .setDisplayName(ChatColor.GOLD + "What is 42?")
        .setLore(ChatColor.GREEN + "The answer to the universe and everything!")
        .getItem();
```

Here is how you can create a speed potion with a custom effect, color and hidden attributes. 
Please take note that this only works in 1.9 or above. If you are using 1.8, then use the Potion class in the spigot API

```java
ItemStack potion = PotionBuilder.getDrinkablePotion()
        .addCustomEffect(new PotionEffect(
                PotionEffectType.SPEED,
                45 * 20,
                1,
                false,
                false,
                true),true)
        .modifyMeta(potionMeta -> potionMeta.setBasePotionData(new PotionData(PotionType.SPEED)))
        .modifyMeta(potionMeta -> potionMeta.setColor(Color.PURPLE))
        .modifyMeta(potionMeta -> potionMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES,ItemFlag.HIDE_POTION_EFFECTS))
        .getItem();
```

This is how to create a scrolling menu and add items to it.
Note that an item index can be as big as it wants but bigger not smaller than 0.
```java
ScrollingMenu menu = new ScrollingMenu(
        pluginInstance, // The plugin instance to register the listeners
        "Example", // Name of the inventory
        36, // How many item you can see at once
        new HashMap<>(), // Empty inventory for now
        null, // There is no inventory to open when this one closes
        1, // Line scrolled when the player clicks the scroll button
        Material.LADDER, // The scroll button material
        ScrollingMenu.InventoryCorner.BOTTOM_RIGHT, // Scroll down button corner
        ScrollingMenu.InventoryCorner.TOP_RIGHT, // Scroll up botton corner
        Sound.UI_BUTTON_CLICK, // Sound to play on scroll
        Sound.BLOCK_ANVIL_BREAK, // Scroll to play when you cannot scroll
        false // Do not go back to the first line when you reopen the inventory
) {
    @Override
    public void onPlayerInventoryClick(@NotNull InventoryClickEvent event) {
        event.getWhoClicked().sendMessage("You just clicked your inventory while viewing this one!");
    }
};

menu.addItem(10, // The index to add the item
        new MenuItem(
                new ItemStack(Material.WATER_BUCKET)) {}); 
menu.addItem(60,
        new MenuItem(
                new ItemStack(Material.GOLD_BLOCK)) {
    @Override
    public void clicked(@NotNull InventoryClickEvent event) { // The code to run when you click this item
        event.getWhoClicked().sendMessage(ChatColor.GOLD + "You found me!");
    }
});
```
Please note that you can extend the Menu class to create your own custom menu, like a menu that can scroll sideways and upwards, for example.
