/*******************************************************************************
 * HellFirePvP / Astral Sorcery 2017
 *
 * This project is licensed under GNU GENERAL PUBLIC LICENSE Version 3.
 * The source code is available on github: https://github.com/HellFirePvP/AstralSorcery
 * For further details, see the License file there.
 ******************************************************************************/

package hellfirepvp.astralsorcery.common.event.listener;

import com.google.common.collect.Lists;
import hellfirepvp.astralsorcery.AstralSorcery;
import hellfirepvp.astralsorcery.client.render.tile.TESRTranslucentBlock;
import hellfirepvp.astralsorcery.common.constellation.distribution.ConstellationSkyHandler;
import hellfirepvp.astralsorcery.common.data.config.Config;
import hellfirepvp.astralsorcery.common.data.world.WorldCacheManager;
import hellfirepvp.astralsorcery.common.tile.TileOreGenerator;
import hellfirepvp.astralsorcery.common.world.WorldProviderBrightnessInj;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProviderEnd;
import net.minecraft.world.WorldProviderHell;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Iterator;
import java.util.List;

/**
 * This class is part of the Astral Sorcery Mod
 * The complete source code for this mod can be found on github.
 * Class: EventHandlerIO
 * Created by HellFirePvP
 * Date: 01.08.2017 / 18:45
 */
public class EventHandlerIO {

    public static List<TileOreGenerator> generatorQueue = Lists.newLinkedList();

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onLoad(WorldEvent.Load event) {
        World w = event.getWorld();
        int id = w.provider.getDimension();
        if (!(w.provider instanceof WorldProviderEnd) && !(w.provider instanceof WorldProviderHell) && !Config.weakSkyRendersWhitelist.contains(w.provider.getDimension())) {
            AstralSorcery.log.info("[AstralSorcery] Found worldProvider in Dimension " + id + " : " + w.provider.getClass().getName());
            w.provider = new WorldProviderBrightnessInj(w, w.provider);
            AstralSorcery.log.info("[AstralSorcery] Injected WorldProvider into dimension " + id + " (chaining old provider.)");
        }
    }

    @SubscribeEvent
    public void onUnload(WorldEvent.Unload event) {
        World w = event.getWorld();
        ConstellationSkyHandler.getInstance().informWorldUnload(w);
        if (w.isRemote) {
            clientUnload();
        }
    }

    @SideOnly(Side.CLIENT)
    private void clientUnload() {
        AstralSorcery.proxy.scheduleClientside(TESRTranslucentBlock::cleanUp);
    }

    @SubscribeEvent
    public void onSave(WorldEvent.Save event) {
        WorldCacheManager.getInstance().doSave(event.getWorld());
    }

    @SubscribeEvent
    public void onChunkLoad(ChunkEvent.Load event) {
        if(!event.getWorld().isRemote) {
            Iterator<TileOreGenerator> iterator = generatorQueue.iterator();
            while (iterator.hasNext()) {
                TileOreGenerator gen = iterator.next();
                BlockPos at = gen.getPos();
                if(event.getChunk().getPos().equals(new ChunkPos(at))) {
                    event.getChunk().getTileEntityMap().put(gen.getPos(), gen);
                    iterator.remove();
                }
            }
        }
    }

}
