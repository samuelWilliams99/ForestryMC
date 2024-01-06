package forestry.arboriculture.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.server.packs.resources.ResourceManager;

import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public enum SaplingModelLoader implements IGeometryLoader {
	INSTANCE;


	@Override
	public void onResourceManagerReload(ResourceManager resourceManager) {
		// NOOP, handled in loader
	}

	@Override
	public IUnbakedGeometry read(JsonDeserializationContext deserializationContext, JsonObject modelContents) {
		return new ModelSapling();
	}
}
