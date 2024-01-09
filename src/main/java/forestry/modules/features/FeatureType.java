package forestry.modules.features;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import net.minecraftforge.registries.ForgeRegistries;

public enum FeatureType {
	MACHINE(ForgeRegistries.Keys.BLOCKS),
	FLUID(ForgeRegistries.Keys.FLUIDS),
	BLOCK(ForgeRegistries.Keys.BLOCKS),
	ENTITY(ForgeRegistries.Keys.ENTITY_TYPES),
	ITEM(ForgeRegistries.Keys.ITEMS),
	TILE(ForgeRegistries.Keys.BLOCK_ENTITY_TYPES),
	CONTAINER(ForgeRegistries.Keys.MENU_TYPES);

	public final ResourceKey<? extends Registry<?>> superType;

	FeatureType(ResourceKey<? extends Registry<?>> superType) {
		this.superType = superType;
	}
}
