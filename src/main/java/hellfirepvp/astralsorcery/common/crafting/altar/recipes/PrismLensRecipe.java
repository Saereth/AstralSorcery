/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.crafting.altar.recipes;

import hellfirepvp.astralsorcery.client.effect.EffectHandler;
import hellfirepvp.astralsorcery.client.util.SpriteLibrary;
import hellfirepvp.astralsorcery.common.block.BlockMarble;
import hellfirepvp.astralsorcery.common.crafting.ItemHandle;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapeMap;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipe;
import hellfirepvp.astralsorcery.common.crafting.helper.ShapedRecipeSlot;
import hellfirepvp.astralsorcery.common.item.ItemCraftingComponent;
import hellfirepvp.astralsorcery.common.item.crystal.CrystalProperties;
import hellfirepvp.astralsorcery.common.lib.BlocksAS;
import hellfirepvp.astralsorcery.common.tile.TileAltar;
import hellfirepvp.astralsorcery.common.util.ItemUtils;
import hellfirepvp.astralsorcery.common.util.OreDictAlias;
import hellfirepvp.astralsorcery.common.util.data.Vector3;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: PrismLensRecipe
 * Created by HellFirePvP
 * Date: 02.11.2016 / 21:07
 */
public class PrismLensRecipe extends ConstellationRecipe {

    public PrismLensRecipe() {
        super(new ShapedRecipe(BlocksAS.lensPrism)
                .addPart(OreDictAlias.BLOCK_GLASS_PANE_NOCOLOR,
                        ShapedRecipeSlot.UPPER_LEFT,
                        ShapedRecipeSlot.LOWER_LEFT,
                        ShapedRecipeSlot.UPPER_RIGHT,
                        ShapedRecipeSlot.LOWER_RIGHT)
                .addPart(ItemCraftingComponent.MetaType.GLASS_LENS.asStack(),
                        ShapedRecipeSlot.LEFT,
                        ShapedRecipeSlot.RIGHT)
                .addPart(ItemCraftingComponent.MetaType.STARDUST.asStack(),
                        ShapedRecipeSlot.UPPER_CENTER,
                        ShapedRecipeSlot.LOWER_CENTER)
                .addPart(ItemHandle.getCrystalVariant(false, false),
                        ShapedRecipeSlot.CENTER));

        setAttItem(BlockMarble.MarbleBlockType.RUNED.asStack(),
                AltarSlot.LOWER_LEFT,
                AltarSlot.LOWER_RIGHT);
        setCstItem(BlockMarble.MarbleBlockType.RUNED.asStack(),
                AltarAdditionalSlot.DOWN_DOWN_LEFT,
                AltarAdditionalSlot.DOWN_DOWN_RIGHT);
        setCstItem(OreDictAlias.BLOCK_WOOD_LOGS,
                AltarAdditionalSlot.DOWN_RIGHT_RIGHT,
                AltarAdditionalSlot.DOWN_LEFT_LEFT);
        setCstItem(OreDictAlias.ITEM_GOLD_INGOT,
                AltarAdditionalSlot.UP_LEFT_LEFT,
                AltarAdditionalSlot.UP_RIGHT_RIGHT);
    }

    @Nullable
    @Override
    public ItemStack getOutput(ShapeMap centralGridMap, TileAltar altar) {
        ItemStack lens = super.getOutput(centralGridMap, altar);
        CrystalProperties crystalProp = CrystalProperties.getCrystalProperties(centralGridMap.get(ShapedRecipeSlot.CENTER).getApplicableItems().get(0));
        CrystalProperties.applyCrystalProperties(lens, crystalProp);
        return lens;
    }

    @Override
    public void onCraftClientTick(TileAltar altar, long tick, Random rand) {
        super.onCraftClientTick(altar, tick, rand);

        Vector3 altarVec = new Vector3(altar);
        if(tick % 48 == 0 && rand.nextBoolean()) {
            EffectHandler.getInstance().textureSpritePlane(SpriteLibrary.spriteCraftBurst, Vector3.RotAxis.Y_AXIS.clone()).setPosition(altarVec.add(0.5, 0.05, 0.5)).setScale(5 + rand.nextInt(2)).setNoRotation(rand.nextInt(360));
        }
    }
}
