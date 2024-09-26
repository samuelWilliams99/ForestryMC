package forestry.modules.features;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import forestry.api.core.ITileTypeProvider;
import net.minecraftforge.registries.RegisterEvent;

public interface ITileTypeFeature<T extends BlockEntity> extends IModFeature, ITileTypeProvider<T> {

	@Override
	default void create() {
		BlockEntityType<T> tileEntityType = getTileTypeConstructor().build(null);
		setTileType(tileEntityType);
	}

	@Override
	@SuppressWarnings("unchecked")
	default void register(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES) && hasTileType()) {
			IForgeRegistry<BlockEntityType> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), tileType());
		}
	}

	@Override
	default BlockEntityType<T> tileType() {
		BlockEntityType<T> tileType = getTileType();
		if (tileType == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return tileType;
	}

	void setTileType(BlockEntityType<T> tileType);

	BlockEntityType.Builder<T> getTileTypeConstructor();
}
