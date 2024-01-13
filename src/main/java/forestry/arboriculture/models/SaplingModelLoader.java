package forestry.arboriculture.models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import net.minecraft.server.packs.resources.ResourceManager;

import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public enum SaplingModelLoader implements IGeometryLoader {
	INSTANCE;

	@Override
	public IUnbakedGeometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
		return new ModelSapling();
	}
}
