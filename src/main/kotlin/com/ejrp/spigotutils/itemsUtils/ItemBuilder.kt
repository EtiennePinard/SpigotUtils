package com.ejrp.spigotutils.itemsUtils

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import java.util.function.Consumer

/**
 * Utility class to quickly create custom items in a spigot plugin.
 * Keep in note that this library is compiled using spigot the 1.8 spigot api
 * and so some method like setUnbreakable() are not yet supported. If you want
 * to use them you will have to use the modifyMeta() method.
 */
class ItemBuilder(private val item: ItemStack) {
    /**
     * Gets the meta of this item
     * @return The meta of this item
     */
    private val itemMeta: ItemMeta = item.itemMeta

    /**
     * Creates a new instance of the custom item class with the specified type and an amount of one.
     * @param type The type of the item.
     */
    constructor(type: Material) : this(ItemStack(type))

    /**
     * Sets the type of this custom item
     * @param type The new type of this custom item
     * @return The current object for method chaining
     */
    fun setMaterial(type: Material): ItemBuilder {
        item.type = type
        return this
    }

    /**
     * Sets the amount of the item
     * @param amount The new amount of the item
     * @return The current object for method chaining
     */
    fun setItemAmount(amount: Int): ItemBuilder {
        item.amount = amount
        return this
    }

    /**
     * Sets the display name of this custom item
     * @param displayName The new display name of this custom item
     * @return The current object for method chaining
     */
    fun setDisplayName(displayName: String): ItemBuilder {
        itemMeta.displayName = displayName
        return this
    }

    /**
     * Sets the lore of this custom item
     * @param lore The new lore of this custom item
     * @return The current object for method chaining
     */
    fun setLore(lore: List<String>): ItemBuilder {
        itemMeta.lore = lore
        return this
    }

    /**
     * Sets the lore of this custom item
     * @param lines The new lore of this custom item
     * @return The current object for method chaining
     */
    fun setLore(vararg lines: String): ItemBuilder = setLore(listOf(*lines))

    /**
     * Adds an item flag to this custom item
     * @param itemFlags The item flags to add to this custom item
     * @return The current object for method chaining
     */
    fun addItemFlags(vararg itemFlags: ItemFlag): ItemBuilder {
        itemMeta.addItemFlags(*itemFlags)
        return this
    }

    /**
     * Adds an enchantment to this custom item
     * @param enchantment The enchantment to add
     * @param level The level of the enchantment
     * @param ignoreLevelRestriction If the enchantment should ignore the default minecraft level restriction
     * @return The current object for method chaining
     */
    fun addEnchants(enchantment: Enchantment, level: Int, ignoreLevelRestriction: Boolean): ItemBuilder {
        itemMeta.addEnchant(enchantment, level, ignoreLevelRestriction)
        return this
    }

    /**
     * Modify this item in the way you want!
     * @param consumer The consumer that will modify this item
     * @return The current object for method chaining
     */
    fun modifyItem(consumer: Consumer<ItemStack>): ItemBuilder {
        consumer.accept(item)
        return this
    }

    /**
     * Modify this item meta in the way you want!
     * @param consumer The consumer that will modify the item meta
     * @return The current object for method chaining
     */
    fun modifyMeta(consumer: Consumer<ItemMeta>): ItemBuilder {
        consumer.accept(itemMeta)
        return this
    }

    /**
     * Gets the item and sets the item meta on the item.
     * @return The item with the current item meta.
     */
    fun getItem(): ItemStack {
        item.itemMeta = itemMeta
        return item
    }
}