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
 * Keep in note that this library is compiled using spigot the 1.8 spigot api
 * and so some method like setColor() are not yet supported. If you want
 * to use them you will have to use the modifyMeta() method.
 */
public class PotionBuilder {

    private final ItemStack item;
    private final PotionMeta meta;

    public PotionBuilder(@NotNull Material material) {
        this.item = new ItemStack(material);
        if (!(item.getItemMeta() instanceof PotionMeta))
            throw new IllegalArgumentException("Your items needs to have a potion meta!");
        meta = (PotionMeta) item.getItemMeta();
    }

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
    public PotionBuilder modifyMeta(@NotNull Consumer<PotionMeta> consumer) {
        consumer.accept(meta);
        return this;
    }

    /**
     * Gets the potion meta of this custom potion
     * @return The potion meta of this custom potion
     */
    public PotionMeta getPotionMeta() { return meta; }

    /**
     * Get the item and sets the right potion meta
     * @return The item with the right potion meta
     */
    public ItemStack getItem() { item.setItemMeta(meta); return item; }

    /**
     * Adds a custom effect to this potion
     * @param effect The effect to add
     * @param override If the effect overrides other effects of the same type
     * @return The current object for method chaining
     */
    public PotionBuilder addCustomEffect(PotionEffect effect, boolean override) {
        meta.addCustomEffect(effect,override);
        return this;
    }

}
