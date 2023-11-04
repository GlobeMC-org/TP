package org.globemc.temppres;

import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;
import java.util.logging.Logger;

public final class Temp_Pres extends JavaPlugin implements Listener {
    public Logger l = getLogger();
    public Server s = getServer();
    public double NuaseaHeight;
    public double DamageHeight;

    @Override
    public void onEnable() {
        l.info("Pressure plugin started");
        s.getPluginManager().registerEvents(this, this);

        this.saveDefaultConfig();
        FileConfiguration config = getConfig();
        NuaseaHeight = (Double) config.get("NauseaHeight");
        DamageHeight = (Double) config.get("DamageHeight");

        ShapelessRecipe helmet = makeReceptie(
                Material.CHAINMAIL_HELMET, 1, "PPA Helmet", "ppa_helmet",
                Arrays.asList("Pressure Protect Armor - Helmet", "Full set of this armor will protect you from underground pressure"));
        helmet.addIngredient(Material.DIAMOND_HELMET);
        helmet.addIngredient(4, Material.IRON_BLOCK);
        Bukkit.addRecipe(helmet);

        ShapelessRecipe chestplate = makeReceptie(
                Material.CHAINMAIL_CHESTPLATE, 1, "PPA Chestplate", "ppa_chestplate",
                Arrays.asList("Pressure Protect Armor - Chestplate", "Full set of this armor will protect you from underground pressure"));
        chestplate.addIngredient(Material.DIAMOND_CHESTPLATE);
        chestplate.addIngredient(4, Material.IRON_BLOCK);
        Bukkit.addRecipe(chestplate);

        ShapelessRecipe leggings = makeReceptie(
                Material.CHAINMAIL_LEGGINGS, 1, "PPA Leggings", "ppa_leggings",
                Arrays.asList("Pressure Protect Armor - Leggings", "Full set of this armor will protect you from underground pressure"));
        leggings.addIngredient(Material.DIAMOND_LEGGINGS);
        leggings.addIngredient(4, Material.IRON_BLOCK);
        Bukkit.addRecipe(leggings);

        ShapelessRecipe boots = makeReceptie(
                Material.CHAINMAIL_BOOTS, 1, "PPA Boots", "ppa_boots",
                Arrays.asList("Pressure Protect Armor - Boots", "Full set of this armor will protect you from underground pressure"));
        boots.addIngredient(Material.DIAMOND_BOOTS);
        boots.addIngredient(4, Material.IRON_BLOCK);
        Bukkit.addRecipe(boots);
    }

    private ShapelessRecipe makeReceptie(Material resultMaterial, int amount, String name, String namespaceKay, List<String> description) {
        ItemStack item = new ItemStack(resultMaterial, amount);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(name);
        itemMeta.setLore(description);
        item.setItemMeta(itemMeta);
        ShapelessRecipe result = new ShapelessRecipe(new NamespacedKey(this, namespaceKay), item);
        return result;
    }

    public boolean playerHasArmor(Player p) {
        int protection = 0;
        if (p.getInventory().getHelmet() != null)
            if (p.getInventory().getHelmet().getType() == Material.CHAINMAIL_HELMET) protection++;
        if (p.getInventory().getChestplate() != null)
            if (p.getInventory().getChestplate().getType() == Material.CHAINMAIL_CHESTPLATE) protection++;
        if (p.getInventory().getLeggings() != null)
            if (p.getInventory().getLeggings().getType() == Material.CHAINMAIL_LEGGINGS) protection++;
        if (p.getInventory().getBoots() != null)
            if (p.getInventory().getBoots().getType() == Material.CHAINMAIL_BOOTS) protection++;
        if (protection == 4) return true;
        return false;
    }

    public boolean playerHasLeatherArmor(Player p) {
        int protection = 0;
        if (p.getInventory().getHelmet() != null)
            if (p.getInventory().getHelmet().getType() == Material.LEATHER_HELMET) protection++;
        if (p.getInventory().getChestplate() != null)
            if (p.getInventory().getChestplate().getType() == Material.LEATHER_CHESTPLATE) protection++;
        if (p.getInventory().getLeggings() != null)
            if (p.getInventory().getLeggings().getType() == Material.LEATHER_LEGGINGS) protection++;
        if (p.getInventory().getBoots() != null)
            if (p.getInventory().getBoots().getType() == Material.LEATHER_BOOTS) protection++;
        if (protection == 4) return true;
        return false;
    }

    @EventHandler
    public void OnPlayerMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        Location pl = p.getLocation();

        if (!playerHasArmor(p)) {
            if (pl.y() < NuaseaHeight) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 500, 3));
            }
            if (pl.y() < DamageHeight) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 1));
                if (pl.y() < DamageHeight - 10) {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 2));
                    if (pl.y() < DamageHeight - 20) {
                        p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 500, 3));
                    }
                }
            }
        }

        Biome[] biomes = {
                Biome.FROZEN_PEAKS, Biome.SNOWY_TAIGA, Biome.SNOWY_SLOPES, Biome.GROVE, Biome.SNOWY_PLAINS,
                Biome.FROZEN_RIVER, Biome.SNOWY_BEACH
        };
        boolean coldBiome = false;
        for (int i = 0; i < biomes.length; i++) {
            if (p.getWorld().getBiome(new Location(p.getWorld(), pl.x(), pl.y(), pl.z())) == biomes[i]) coldBiome = true;
        }
        if (!playerHasLeatherArmor(p))
            if (coldBiome)
                p.addPotionEffect(new PotionEffect(PotionEffectType.HUNGER, 500, 3));
    }
}
