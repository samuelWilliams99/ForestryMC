package forestry.core.multiblock;

import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;

import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import net.minecraftforge.fml.common.Mod;

import forestry.core.config.Constants;

/**
 * In your mod, subscribe this on both the client and server sides side to handle chunk
 * load events for your multiblock machines.
 * Chunks can load asynchronously in environments like MCPC+, so we cannot
 * process any blocks that are in chunks which are still loading.
 */
@Mod.EventBusSubscriber(modid = Constants.MOD_ID)
public class MultiblockEventHandler {
	@SubscribeEvent
	public static void onChunkLoad(ChunkEvent.Load loadEvent) {
		ChunkAccess chunk = loadEvent.getChunk();
		LevelAccessor world = loadEvent.getLevel();
		MultiblockRegistry.onChunkLoaded(world, chunk.getPos().getRegionX(), chunk.getPos().getRegionZ());
	}

	// Cleanup, for nice memory usageness
	@SubscribeEvent
	public static void onWorldUnload(LevelEvent.Unload unloadWorldEvent) {
		MultiblockRegistry.onWorldUnloaded(unloadWorldEvent.getLevel());
	}
}
