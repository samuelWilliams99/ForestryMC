package forestry.modules.features;

import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import forestry.api.core.IItemProvider;
import forestry.core.proxy.Proxies;
import net.minecraftforge.registries.RegisterEvent;

public interface IItemFeature<I extends Item> extends IModFeature, IItemProvider<I>, net.minecraft.world.level.ItemLike {

	Supplier<I> getItemConstructor();

	void setItem(I item);

	default I item() {
		I item = getItem();
		if (item == null) {
			throw new IllegalStateException("Called feature getter method before content creation was called in the pre init.");
		}
		return item;
	}

	@Override
	default Item asItem() {
		return item();
	}

	@Override
	default void create() {
		I item = getItemConstructor().get();
		setItem(item);
	}

	@SuppressWarnings("unchecked")
	default void register(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ITEMS) && hasItem()) {
			IForgeRegistry<Item> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), item());
			Proxies.common.registerItem(item());
		}
	}
}
