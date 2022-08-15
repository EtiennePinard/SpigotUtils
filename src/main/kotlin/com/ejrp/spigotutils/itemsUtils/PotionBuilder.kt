package com.ejrp.spigotutils.itemsUtils

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionEffect
import org.jetbrains.annotations.Contract
import java.lang.IllegalArgumentException
import java.util.function.Consumer

/**
 * Utility class to quickly create custom potions in a spigot plugin. This is a complement class to the ItemBuilder class.
 * Keep in note that this library is compiled using the 1.8 spigot API
 * and so some method like setColor() are not yet supported. If you want
 * to use them you will have to use the modifyMeta() method. Also in the Spigot 1.8 API you should use the
 * Potion class to create custom potions as the PotionMeta is very limited in this version of the API.
 */
class PotionBuilder(private val item: ItemStack) {
    /**
     * Gets the potion meta of this custom potion
     * @return The potion meta of this custom potion
     */
    private val potionMeta: PotionMeta

    /**
     * Creates a new PotionBuilder object with the specified item.
     * @param item The item to create the PotionBuilder object.
     * @throws IllegalArgumentException If the item meta is not an instance of the PotionMeta interface
     */
    init {
        require(item.itemMeta is PotionMeta) { "Your item meta is not an instance of a potion meta!" }
        potionMeta = item.itemMeta as PotionMeta
    }

    /**
     * Create a PotionBuilder object with the specified material.
     * @param material The material to create the PotionBuilder object.
     */
    constructor(material: Material) : this(ItemStack(material))

    /**
     * Modify this item meta in the way you want!
     * @param consumer The consumer that will modify the item meta
     * @return The current object for method chaining
     */
    fun modifyMeta(consumer: Consumer<PotionMeta>): PotionBuilder {
        consumer.accept(potionMeta)
        return this
    }

    /**
     * Get the item and sets the right potion meta
     * @return The item with the right potion meta
     */
    fun getItem(): ItemStack {
        item.itemMeta = potionMeta
        return item
    }

    /**
     * Adds a custom effect to this potion
     * @param effect The effect to add
     * @param override If the effect overrides other effects of the same type
     * @return The current object for method chaining
     */
    fun addCustomEffect(effect: PotionEffect, override: Boolean): PotionBuilder {
        potionMeta.addCustomEffect(effect, override)
        return this
    }

    companion object {
        /**
         * Created a PotionBuilder object with the item type being a potion.
         * @return The newly created PotionBuilder object.
         */
        @get:Contract(" -> new")
        val drinkablePotion: PotionBuilder
            get() = PotionBuilder(Material.POTION)
    }
}