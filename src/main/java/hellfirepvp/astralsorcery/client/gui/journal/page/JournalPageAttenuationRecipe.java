package hellfirepvp.astralsorcery.client.gui.journal.page;

import hellfirepvp.astralsorcery.client.util.TextureHelper;
import hellfirepvp.astralsorcery.client.util.resource.AssetLibrary;
import hellfirepvp.astralsorcery.client.util.resource.AssetLoader;
import hellfirepvp.astralsorcery.client.util.resource.BindableResource;
import hellfirepvp.astralsorcery.common.crafting.altar.recipes.AttenuationRecipe;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: JournalPageAttenuationRecipe
 * Created by HellFirePvP
 * Date: 27.10.2016 / 01:10
 */
public class JournalPageAttenuationRecipe implements IJournalPage {

    private final AttenuationRecipe recipe;

    public JournalPageAttenuationRecipe(AttenuationRecipe recipe) {
        this.recipe = recipe;
    }

    @Override
    public IGuiRenderablePage buildRenderPage() {
        return new Render(recipe);
    }

    public static class Render extends JournalPageDiscoveryRecipe.RecipePage {

        private static final BindableResource texGrid = AssetLibrary.loadTexture(AssetLoader.TextureLocation.GUI, "gridAtt");

        private final AttenuationRecipe recipe;

        public Render(AttenuationRecipe recipe) {
            super(recipe);
            this.recipe = recipe;
        }

        protected void renderAltarSlots(float offsetX, float offsetY, float zLevel, AttenuationRecipe recipe) {
            RenderHelper.enableGUIStandardItemLighting();
            renderAltarSlot(offsetX + 30, offsetY + 78, zLevel, recipe.getAttItem(AttenuationRecipe.AltarSlot.UPPER_LEFT));
            renderAltarSlot(offsetX + 131, offsetY + 78, zLevel, recipe.getAttItem(AttenuationRecipe.AltarSlot.UPPER_RIGHT));
            renderAltarSlot(offsetX +  30, offsetY + 178, zLevel, recipe.getAttItem(AttenuationRecipe.AltarSlot.LOWER_LEFT));
            renderAltarSlot(offsetX + 131, offsetY + 178, zLevel, recipe.getAttItem(AttenuationRecipe.AltarSlot.LOWER_RIGHT));
            RenderHelper.disableStandardItemLighting();
            TextureHelper.refreshTextureBindState();
        }

        private void renderAltarSlot(float offsetX, float offsetY, float zLevel, ItemStack stack) {
            if(stack == null) return;
            TextureHelper.refreshTextureBindState();
            GL11.glPushMatrix();
            GL11.glTranslated(offsetX, offsetY, zLevel + 60);
            GL11.glScaled(1.1, 1.1, 1.1);
            drawItemStack(stack, 0, 0, 0);
            GL11.glPopMatrix();
        }

        @Override
        public void render(float offsetX, float offsetY, float pTicks, float zLevel) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
            GL11.glColor4f(1F, 1F, 1F, 1F);

            renderStandartRecipeGrid(offsetX, offsetY, zLevel, texGrid);

            renderOutputOnGrid(offsetX, offsetY, zLevel);

            renderDefaultExpectedItems(offsetX, offsetY, zLevel, recipe.getNativeRecipe());

            renderStarlightRequirementString(offsetX, offsetY, zLevel, recipe.getPassiveStarlightRequired());

            renderAltarSlots(offsetX, offsetY, zLevel, recipe);

            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopAttrib();
        }

    }

}
