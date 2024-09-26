/*******************************************************************************
 * Copyright (c) 2011-2014 SirSengir.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v3
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/lgpl-3.0.txt
 *
 * Various Contributors including, but not limited to:
 * SirSengir (original work), CovertJaguar, Player, Binnie, MysteriousAges
 ******************************************************************************/
package forestry.core.utils;

import javax.annotation.Nullable;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.ItemStack;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.SimpleModelState;

/**
 * Util methods used at the installation of the game or at the reloading or baking of resources like models or
 * textures.
 */
@OnlyIn(Dist.CLIENT)
public class ResourceUtil {

	private ResourceUtil() {
	}

	public static Minecraft client() {
		return Minecraft.getInstance();
	}

	public static ResourceManager resourceManager() {
		return client().getResourceManager();
	}

	public static TextureAtlasSprite getMissingTexture() {
		return getSprite(InventoryMenu.BLOCK_ATLAS, MissingTextureAtlasSprite.getLocation());
	}

	public static TextureAtlasSprite getSprite(ResourceLocation atlas, ResourceLocation sprite) {
		return client().getTextureAtlas(atlas).apply(sprite);
	}

	public static TextureAtlasSprite getBlockSprite(ResourceLocation location) {
		return getSprite(InventoryMenu.BLOCK_ATLAS, location);
	}

	public static TextureAtlasSprite getBlockSprite(String location) {
		return getBlockSprite(new ResourceLocation(location));
	}

	public static boolean resourceExists(ResourceLocation location) {
		return resourceManager().getResource(location).isPresent();
	}

	public static BufferedReader createReader(Resource resource) throws IOException {
		return new BufferedReader(new InputStreamReader(resource.open(), StandardCharsets.UTF_8));
	}

	@Nullable
	public static Resource getResource(ResourceLocation location) {
		return resourceManager().getResource(location).orElse(null);
	}

	public static List<Resource> getResources(ResourceLocation location) {
		return resourceManager().getResourceStack(location);
	}

	/**
	 * @return The model from the item of the stack.
	 */
	@Nullable
	public static BakedModel getModel(ItemStack stack) {
		ItemRenderer renderItem = client().getItemRenderer();
		if (renderItem == null || renderItem.getItemModelShaper() == null) {
			return null;
		}
		return renderItem.getItemModelShaper().getItemModel(stack);
	}

	public static ItemTransforms loadTransforms(ResourceLocation location) {
		return loadTransformFromJson(location);
	}

	public static ItemTransforms loadTransformFromJson(ResourceLocation location) {
		try (Reader reader = getReaderForResource(location)) {
			return BlockModel.fromStream(reader)
					.getTransforms();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ItemTransforms.NO_TRANSFORMS;
	}

	private static Reader getReaderForResource(ResourceLocation location) throws IOException {
		ResourceLocation file = new ResourceLocation(location.getNamespace(),
				"models/" + location.getPath() + ".json");
		Resource iresource = resourceManager().getResource(file).orElseThrow(IOException::new);
		return new BufferedReader(new InputStreamReader(iresource.open(), StandardCharsets.UTF_8));
	}
}
