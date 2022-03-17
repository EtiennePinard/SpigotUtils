package com.ejrp.spigotutils.ItemsUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

/**
 * Utility class to quickly create custom items in a spigot plugin.
 * Keep in note that this library is compiled using spigot the 1.8 spigot api
 * and so some method like setUnbreakable() are not yet supported. If you want
 * to use them you will have to use the modifyMeta() method.
 */
public class ItemBuilder {

    private final ItemStack item;
    private final ItemMeta meta;

    /**
     * Creates a new instance of the custom item class with the specified parameter.
     * @param item The item of the custom item.
     */
    public ItemBuilder(@NotNull ItemStack item) {
        this.item = item;
        meta = item.getItemMeta();
    }

    /**
     * Creates a new instance of the custom item class with the specified type and an amount of one.
     * @param type The type of the item.
     */
    public ItemBuilder(Material type) {
        this(new ItemStack(type));
    }

    /**
     * Sets the type of this custom item
     * @param type The new type of this custom item
     * @return The current object for method chaining
     */
    public ItemBuilder setMaterial(Material type) { item.setType(type); return this; }

    /**
     * Sets the amount of the item
     * @param amount The new amount of the item
     * @return The current object for method chaining
     */
    public ItemBuilder setItemAmount(int amount) { item.setAmount(amount); return this; }

    /**
     * Sets the display name of this custom item
     * @param displayName The new display name of this custom item
     * @return The current object for method chaining
     */
    public ItemBuilder setDisplayName(String displayName) { meta.setDisplayName(displayName); return this; }

    /**
     * Sets the lore of this custom item
     * @param lore The new lore of this custom item
     * @return The current object for method chaining
     */
    public ItemBuilder setLore(List<String> lore) { meta.setLore(lore); return this; }

    /**
     * Sets the lore of this custom item
     * @param lines The new lore of this custom item
     * @return The current object for method chaining
     */
    public ItemBuilder setLore(String... lines) { return setLore(Arrays.asList(lines)); }

    /**
     * Adds an item flag to this custom item
     * @param itemFlags The item flags to add to this custom item
     * @return The current object for method chaining
     */
    public ItemBuilder addItemFlags(ItemFlag... itemFlags) { meta.addItemFlags(itemFlags); return this; }

    /**
     * Adds an enchantment to this custom item
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @param ignoreLevelRestriction If the enchantment should ignore the default minecraft level restriction
     * @return The current object for method chaining
     */
    public ItemBuilder addEnchants(Enchantment enchantment, int level, boolean ignoreLevelRestriction) { meta.addEnchant(enchantment,level,ignoreLevelRestriction); return this; }

    /**
     * Modify this item in the way you want!
     * @param consumer The consumer that will modify this item
     * @return The current object for method chaining
     */
    public ItemBuilder modifyItem(@NotNull Consumer<ItemStack> consumer)  {
        consumer.accept(item);
        return this;
    }

    /**
     * Modify this item meta in the way you want!
     * @param consumer The consumer that will modify the item meta
     * @return The current object for method chaining
     */
    public ItemBuilder modifyMeta(@NotNull Consumer<ItemMeta> consumer) {
        consumer.accept(meta);
        return this;
    }

    /**
     * Gets the meta of this item
     * @return The meta of this item
     */
    public ItemMeta getItemMeta() { return meta; }

    /**
     * Gets the item and sets the item meta on the item.
     * @return The item with the current item meta.
     */
    public ItemStack getItem() { item.setItemMeta(meta); return item; }

}
