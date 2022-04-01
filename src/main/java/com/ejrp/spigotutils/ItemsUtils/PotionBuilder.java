package com.ejrp.spigotutils.ItemsUtils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * Utility class to quickly create custom potions in a spigot plugin. This is a complement class to the ItemBuilder class.
 * Keep in note that this library is compiled using the 1.8 spigot API
 * and so some method like setColor() are not yet supported. If you want
 * to use them you will have to use the modifyMeta() method. Also in the Spigot 1.8 API you should use the
 * Potion class to create custom potions as the PotionMeta is very limited in this version of the API.
 */
public class PotionBuilder {

    @NotNull private final ItemStack item;
    @NotNull private final PotionMeta meta;

    /**
     * Creates a new PotionBuilder object with the specified item.
     * @param item The item to create the PotionBuilder object.
     * @throws IllegalArgumentException If the item meta is not an instance of the PotionMeta interface
     */
    public PotionBuilder(@NotNull ItemStack item) throws IllegalArgumentException {
        this.item = item;
        if (!(item.getItemMeta() instanceof PotionMeta))
            throw new IllegalArgumentException("Your item meta is not an instance of a potion meta!");
        meta = (PotionMeta) item.getItemMeta();
    }

    /**
     * Create a PotionBuilder object with the specified material.
     * @param material The material to create the PotionBuilder object.
     */
    public PotionBuilder(@NotNull Material material) { this(new ItemStack(material)); }

    /**
     * Created a PotionBuilder object with the item type being a potion.
     * @return The newly created PotionBuilder object.
     */
    @NotNull
    @Contract(" -> new")
    public static PotionBuilder getDrinkablePotion() { return new PotionBuilder(Material.POTION); }

    /**
     * Modify this item meta in the way you want!
     * @param consumer The consumer that will modify the item meta
     * @return The current object for method chaining
     */
    @NotNull public PotionBuilder modifyMeta(@NotNull Consumer<PotionMeta> consumer) {
        consumer.accept(meta);
        return this;
    }

    /**
     * Gets the potion meta of this custom potion
     * @return The potion meta of this custom potion
     */
    @NotNull public PotionMeta getPotionMeta() { return meta; }

    /**
     * Get the item and sets the right potion meta
     * @return The item with the right potion meta
     */
    @NotNull public ItemStack getItem() { item.setItemMeta(meta); return item; }

    /**
     * Adds a custom effect to this potion
     * @param effect The effect to add
     * @param override If the effect overrides other effects of the same type
     * @return The current object for method chaining
     */
    @NotNull public PotionBuilder addCustomEffect(PotionEffect effect, boolean override) {
        meta.addCustomEffect(effect,override);
        return this;
    }

}
