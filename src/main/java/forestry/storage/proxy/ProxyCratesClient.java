package forestry.storage.proxy;

import net.minecraft.client.resources.model.ModelResourceLocation;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.ModelEvent.RegisterGeometryLoaders;

import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import forestry.api.storage.EnumBackpackType;
import forestry.core.config.Constants;
import forestry.core.utils.ForgeUtils;
import forestry.modules.IClientModuleHandler;
import forestry.storage.BackpackMode;
import forestry.storage.models.BackpackItemModel;
import forestry.storage.models.CrateModel;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public class ProxyCratesClient extends ProxyCrates implements IClientModuleHandler {

	public ProxyCratesClient() {
		ForgeUtils.registerSubscriber(this);
	}

	@Override
	public void registerModels(ModelEvent.RegisterAdditional event) {
		for (EnumBackpackType backpackType : EnumBackpackType.values()) {
			for (BackpackMode mode : BackpackMode.values()) {
				event.register(backpackType.getLocation(mode));
			}
		}

		event.register(new ModelResourceLocation(Constants.MOD_ID + ":crate-filled", "inventory"));
	}

	@Override
	public void registerModelLoaders(RegisterGeometryLoaders event) {
		event.register(CrateModel.Loader.LOCATION.getPath(), new CrateModel.Loader());
		event.register(BackpackItemModel.Loader.LOCATION.getPath(), new BackpackItemModel.Loader());
	}
}
