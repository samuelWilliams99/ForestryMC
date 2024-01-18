package forestry.modules.features;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.Util;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.ForgeFlowingFluid;

import forestry.core.config.Constants;
import forestry.core.fluids.BlockForestryFluid;
import forestry.core.items.definitions.DrinkProperties;

public class FeatureFluid implements IFluidFeature {
	private final FeatureBlock<BlockForestryFluid, BlockItem> block;
	private final FluidProperties properties;
	private final String moduleID;
	private final String identifier;
	private final ForgeFlowingFluid.Properties internal;
	@Nullable
	private volatile FlowingFluid fluid;
	@Nullable
	private FlowingFluid flowing;

	@Nullable FluidType fluidType;

	public FeatureFluid(Builder builder) {
		this.moduleID = builder.moduleID;
		this.identifier = builder.identifier;
		this.block = builder.registry.block(() -> new BlockForestryFluid(this), "fluid_" + builder.identifier);
		this.properties = new FluidProperties(builder);
		this.internal = new ForgeFlowingFluid.Properties(this::getFluidType, this::getFluid, this::getFlowing).block(block::getBlock).bucket(properties().bucket);
	}

	@Override
	public FeatureBlock<BlockForestryFluid, BlockItem> fluidBlock() {
		return block;
	}

	@Override
	public void setFluid(FlowingFluid fluid) {
		this.fluid = fluid;
	}

	@Override
	public void setFlowing(@Nullable FlowingFluid flowing) {
		this.flowing = flowing;
	}

	@Override
	public void setFluidType(@Nullable FluidType fluidType) {
		this.fluidType = fluidType;
	}

	@Override
	public Supplier<FlowingFluid> getFluidConstructor(boolean flowing) {
		return () -> flowing ? new ForgeFlowingFluid.Flowing(internal) : new ForgeFlowingFluid.Source(internal);
	}

	@Override
	public FluidType createFluidType() {
		FluidType.Properties fluidProps = FluidType.Properties.create()
				.density(properties().density)
				.viscosity(properties().viscosity)
				.temperature(properties().temperature)
				.descriptionId(Util.makeDescriptionId("fluid", new ResourceLocation(getModId(), getIdentifier())));
		return new FluidType(fluidProps) {
			@Override
			public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
				consumer.accept(new IClientFluidTypeExtensions() {
					@Override
					public ResourceLocation getStillTexture() {
						return new ResourceLocation(getModId(), "block/liquid/" + getIdentifier() + "_still");
					}

					@Override
					public ResourceLocation getFlowingTexture() {
						return new ResourceLocation(getModId(), "block/liquid/" + getIdentifier() + "_still");
					}
				});
			}
		};
	}

	@Nullable
	@Override
	public FlowingFluid getFluid() {
		// im sure double-checked locking is overkill but why not
		if (fluid == null) {
			synchronized (this) {
				if (fluid == null) {
					create();
				}
			}
		}

		return fluid;
	}

	@Nullable
	@Override
	public FlowingFluid getFlowing() {
		return flowing;
	}

	@Override
	@Nullable
	public FluidType getFluidType() {
		return fluidType;
	}

	@Override
	public FluidProperties properties() {
		return properties;
	}

	@Override
	public boolean hasFlowing() {
		return flowing != null;
	}

	@Override
	public boolean hasFluid() {
		return fluid != null;
	}

	@Override
	public FeatureType getType() {
		return FeatureType.FLUID;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String getModId() {
		return Constants.MOD_ID;
	}

	@Override
	public String getModuleId() {
		return moduleID;
	}

	public static class Builder {
		private final IFeatureRegistry registry;
		private final String moduleID;
		final String identifier;

		int density = 1000;
		int viscosity = 1000;
		int temperature = 295;
		Color particleColor = Color.WHITE;
		int flammability = 0;
		boolean flammable = false;
		@Nullable
		DrinkProperties properties = null;
		Supplier<Item> bucket = () -> Items.AIR;

		public Builder(IFeatureRegistry registry, String moduleID, String identifier) {
			this.registry = registry;
			this.moduleID = moduleID;
			this.identifier = identifier;
		}

		public Builder flammable() {
			this.flammable = true;
			return this;
		}

		public Builder flammability(int flammability) {
			this.flammability = flammability;
			return this;
		}

		public Builder density(int density) {
			this.density = density;
			return this;
		}

		public Builder viscosity(int viscosity) {
			this.viscosity = viscosity;
			return this;
		}

		public Builder temperature(int temperature) {
			this.temperature = temperature;
			return this;
		}

		public Builder particleColor(Color color) {
			this.particleColor = color;
			return this;
		}

		public Builder bucket(Supplier<Item> bucket) {
			this.bucket = bucket;
			return this;
		}

		public Builder drinkProperties(int healAmount, float saturationModifier, int maxItemUseDuration) {
			this.properties = new DrinkProperties(healAmount, saturationModifier, maxItemUseDuration);
			return this;
		}

		public FeatureFluid create() {
			return registry.register(new FeatureFluid(this));
		}
	}
}
