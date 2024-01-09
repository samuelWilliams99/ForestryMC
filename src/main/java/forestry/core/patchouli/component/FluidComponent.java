package forestry.core.patchouli.component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.UnaryOperator;

import mezz.jei.library.render.FluidTankRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;

import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class FluidComponent implements ICustomComponent {
	public IVariable fluid;
	public IVariable amount;
	public IVariable max;
	public IVariable width;
	public IVariable height;

	private transient int x, y, w, h, level, maxLevel;
	private transient FluidStack fluidStack;

	@Override
	public void build(int componentX, int componentY, int pageNum) {
		this.x = componentX;
		this.y = componentY;
	}

	@Override
	public void render(PoseStack ms, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
		ms.pushPose();
		Fluid _fluid = fluidStack.getFluid();
		IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(_fluid);

		ResourceLocation fluidStill = renderProperties.getStillTexture(fluidStack);
		TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(fluidStill);
		ResourceLocation spriteLocation = sprite.getName();
		RenderSystem.setShaderTexture(0, new ResourceLocation(spriteLocation.getNamespace(), "textures/" + spriteLocation.getPath() + ".png"));
		setGLColorFromInt(renderProperties.getTintColor(fluidStack));

		// MatrixStack transform, int x, int y, float u, float v, int width, int height, int ?, int ?
		GuiComponent.blit(ms, x, (int) (y + h - Math.floor(h * ((float) level / maxLevel))), sprite.getU0(), sprite.getV0(), w, h * level / maxLevel, 8, 8);

		if (context.isAreaHovered(mouseX, mouseY, x, y, w, h)) {
			List<Component> toolTips = new ArrayList<>();
			toolTips.add(Component.translatable(fluidStack.getTranslationKey()));
			Component liquidAmount = Component.translatable("for.gui.tooltip.liquid.amount", level, maxLevel);
			toolTips.add(liquidAmount);

			context.setHoverTooltipComponents(toolTips);
		}

		ms.popPose();
	}

	@Override
	public void onVariablesAvailable(UnaryOperator<IVariable> lookup) {
		ResourceLocation id = new ResourceLocation(lookup.apply(fluid).asString());
		int mb = lookup.apply(amount).asNumber().intValue();

		try {
			this.fluidStack = new FluidStack(ForgeRegistries.FLUIDS.getValue(id), mb);
		} catch (Exception e) {
			this.fluidStack = FluidStack.EMPTY;
		}

		this.w = lookup.apply(width).asNumber().intValue();
		this.h = lookup.apply(height).asNumber().intValue();
		this.level = lookup.apply(amount).asNumber().intValue();
		this.maxLevel = lookup.apply(max).asNumber().intValue();
	}

	private static void setGLColorFromInt(int color) {
		float red = (color >> 16 & 0xFF) / 255.0F;
		float green = (color >> 8 & 0xFF) / 255.0F;
		float blue = (color & 0xFF) / 255.0F;

		RenderSystem.setShaderColor(red, green, blue, 1.0F);
	}
}
