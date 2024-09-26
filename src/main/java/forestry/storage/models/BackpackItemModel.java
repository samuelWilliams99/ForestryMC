package forestry.storage.models;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;

import java.util.Collection;
import java.util.Set;
import java.util.function.Function;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.item.ItemStack;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.resources.ResourceLocation;

import com.mojang.datafixers.util.Pair;

import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

import forestry.api.storage.EnumBackpackType;
import forestry.core.config.Constants;
import forestry.core.models.AbstractItemModel;
import forestry.storage.BackpackMode;
import forestry.storage.items.ItemBackpack;

public class BackpackItemModel extends AbstractItemModel {
	private static ImmutableMap<EnumBackpackType, ImmutableMap<BackpackMode, BakedModel>> cachedBakedModels = ImmutableMap.of();

	public BackpackItemModel() {
	}

	@Override
	protected BakedModel getOverride(BakedModel model, ItemStack stack) {
		BackpackMode mode = ItemBackpack.getMode(stack);
		EnumBackpackType type = ItemBackpack.getType(stack);
		return cachedBakedModels.getOrDefault(type, ImmutableMap.of()).getOrDefault(mode, model);
	}

	private static class Geometry implements IUnbakedGeometry<BackpackItemModel.Geometry> {

		@Override
		public BakedModel bake(IGeometryBakingContext owner, ModelBakery bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelTransform, ItemOverrides overrides, ResourceLocation modelLocation) {
			if (cachedBakedModels.isEmpty()) {
				ImmutableMap.Builder<EnumBackpackType, ImmutableMap<BackpackMode, BakedModel>> modelBuilder = new ImmutableMap.Builder<>();
				for (EnumBackpackType backpackType : EnumBackpackType.values()) {
					ImmutableMap.Builder<BackpackMode, BakedModel> modeModels = new ImmutableMap.Builder<>();
					for (BackpackMode mode : BackpackMode.values()) {
						modeModels.put(mode, bakery.bake(backpackType.getLocation(mode)
							, modelTransform
							, spriteGetter)
						);
					}
					modelBuilder.put(backpackType, modeModels.build());
				}
				cachedBakedModels = modelBuilder.build();
			}
			return new BackpackItemModel();
		}

		@Override
		public Collection<Material> getMaterials(IGeometryBakingContext owner, Function<ResourceLocation, UnbakedModel> modelGetter, Set<Pair<String, String>> missingTextureErrors) {
			return ImmutableList.of();
		}
	}

	public static class Loader implements IGeometryLoader<Geometry> {
		public static final ResourceLocation LOCATION = new ResourceLocation(Constants.MOD_ID, "backpacks");

		@Override
		public BackpackItemModel.Geometry read(JsonObject modelContents, JsonDeserializationContext deserializationContext) {
			return new BackpackItemModel.Geometry();
		}
	}
}
