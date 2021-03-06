/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.base;

import hellfirepvp.astralsorcery.common.data.config.Config;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: OreTypes
 * Created by HellFirePvP
 * Date: 03.11.2016 / 01:16
 */
public class OreTypes {

    private static Map<String, OreEntry> oreDictWeights = new HashMap<>();
    private static double totalWeight = 0D;
    private static double totalVanillaWeight = 0D;

    private static Map<String, OreEntry> localFallback = new HashMap<>();
    private static double fallbackWeight = 0D;
    private static double fallbackVanillaWeight = 0D;

    public static void init() {
        //Vanilla
        registerOreEntry("oreCoal",        5200D, true);
        registerOreEntry("oreIron",        2500D, true);
        registerOreEntry("oreGold",         550D, true);
        registerOreEntry("oreLapis",        140D, true);
        registerOreEntry("oreRedstone",     700D, true);
        registerOreEntry("oreDiamond",      180D, true);
        registerOreEntry("oreEmerald",      100D, true);
        //Modded
        registerOreEntry("oreAluminum",     600D);
        registerOreEntry("oreCopper",      1100D);
        registerOreEntry("oreTin",         1500D);
        registerOreEntry("oreLead",        1000D);
        registerOreEntry("oreCertusQuartz", 500D);
        registerOreEntry("oreNickel",       270D);
        registerOreEntry("orePlatinum",      90D);
        registerOreEntry("oreSilver",       180D);
        registerOreEntry("oreMithril",        1D);
        registerOreEntry("oreRuby",         400D);
        registerOreEntry("oreSapphire",     400D);
        registerOreEntry("oreUranium",      550D);
        registerOreEntry("oreYellorite",    560D);
        registerOreEntry("oreZinc",         300D);
        registerOreEntry("oreSulfur",       600D);
        registerOreEntry("oreOsmium",       950D);

        cacheLocalFallback();
    }

    private static void cacheLocalFallback() {
        if(localFallback.isEmpty()) {
            localFallback.putAll(oreDictWeights);
            fallbackWeight = totalWeight;
            fallbackVanillaWeight = totalVanillaWeight;
        }
    }

    public static void loadFromFallback() {
        oreDictWeights.clear();
        totalWeight = fallbackWeight;
        totalVanillaWeight = fallbackVanillaWeight;
        oreDictWeights.putAll(localFallback);
    }

    public static void removeOreEntry(String oreDictName) {
        if(oreDictWeights.containsKey(oreDictName)) {
            double weight = oreDictWeights.get(oreDictName).weight;
            boolean val = oreDictWeights.get(oreDictName).isVanilla;
            oreDictWeights.remove(oreDictName);
            totalWeight -= weight;
            if(val) {
                totalVanillaWeight -= weight;
            }
        }
    }

    public static void registerOreEntry(String oreDictName, Double weight) {
        registerOreEntry(oreDictName, weight, false);
    }

    public static void registerOreEntry(String oreDictName, Double weight, boolean vanilla) {
        oreDictWeights.put(oreDictName, new OreEntry(weight, vanilla));
        totalWeight += weight;
        if(vanilla) {
            totalVanillaWeight += weight;
        }
    }

    @Nonnull
    public static ItemStack getRandomOre(Random random) {
        return getRandomOre(random, false);
    }

    @Nonnull
    public static ItemStack getRandomOre(Random random, boolean onlyVanilla) {
        ItemStack result = ItemStack.EMPTY;
        int runs = 0;
        while (result.isEmpty() && runs < 150) {

            String key = null;
            double randWeight = random.nextFloat() * (onlyVanilla ? totalVanillaWeight : totalWeight);
            for (Map.Entry<String, OreEntry> entry : oreDictWeights.entrySet()) {
                if(onlyVanilla && !entry.getValue().isVanilla) {
                    continue;
                }
                randWeight -= entry.getValue().weight;
                if(randWeight <= 0) {
                    key = entry.getKey();
                    break;
                }
            }
            if(key == null) {
                runs++;
                continue;
            }
            NonNullList<ItemStack> ores = OreDictionary.getOres(key);

            for (ItemStack stack : ores) {
                if(stack.isEmpty() || Block.getBlockFromItem(stack.getItem()) == Blocks.AIR) continue;
                Item i = stack.getItem();
                String regModid = i.getRegistryName().getResourceDomain();
                if(Config.modidOreGenBlacklist.contains(regModid)) continue;

                String className = i.getClass().getName();
                if(!className.toLowerCase().contains("greg")) {
                    if(stack.getItemDamage() == OreDictionary.WILDCARD_VALUE) stack.setItemDamage(0);
                    result = stack;
                }
            }
            runs++;
        }

        return result;
    }

    private static class OreEntry {

        private final double weight;
        private final boolean isVanilla;

        OreEntry(double weight, boolean isVanilla) {
            this.weight = weight;
            this.isVanilla = isVanilla;
        }

    }

}
