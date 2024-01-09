package forestry.modules.features;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import net.minecraft.world.item.Item;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import net.minecraftforge.network.IContainerFactory;

import forestry.api.core.IContainerTypeProvider;
import net.minecraftforge.registries.RegisterEvent;

public interface IContainerTypeFeature<C extends AbstractContainerMenu> extends IContainerTypeProvider<C>, IModFeature {

	@Override
	default void create() {
		MenuType<C> containerType = IForgeMenuType.create(getContainerFactory());
		setContainerType(containerType);
	}

	@Override
	@SuppressWarnings("unchecked")
	default void register(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.MENU_TYPES) && hasContainerType()) {
			IForgeRegistry<MenuType<C>> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), containerType());
		}
	}

	@Override
	default MenuType<C> containerType() {
		MenuType<C> containerType = getContainerType();
		if (containerType == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return containerType;
	}

	void setContainerType(MenuType<C> containerType);

	IContainerFactory<C> getContainerFactory();
}
