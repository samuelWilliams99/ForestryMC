package forestry.modules.features;

import javax.annotation.Nullable;
import java.util.function.Supplier;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.item.BlockItem;

import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import forestry.core.fluids.BlockForestryFluid;
import net.minecraftforge.registries.RegisterEvent;

public interface IFluidFeature extends IModFeature {

	FeatureBlock<BlockForestryFluid, BlockItem> fluidBlock();

	default Fluid apply(Fluid fluid) {
		return fluid;
	}

	void setFluid(FlowingFluid fluid);

	void setFlowing(FlowingFluid flowing);

	void setFluidType(FluidType fluidType);

	Supplier<FlowingFluid> getFluidConstructor(boolean flowing);

	FluidType createFluidType();

	@Nullable
	FlowingFluid getFluid();

	@Nullable
	FlowingFluid getFlowing();

	@Nullable
	FluidType getFluidType();

	FluidProperties properties();

	boolean hasFluid();

	boolean hasFlowing();

	default FlowingFluid fluid() {
		FlowingFluid fluid = getFluid();
		if (fluid == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return fluid;
	}

	default FlowingFluid flowing() {
		FlowingFluid flowing = getFlowing();
		if (flowing == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return flowing;
	}

	default FluidType fluidType() {
		FluidType fluidType = getFluidType();
		if (fluidType == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return fluidType;
	}

	default FluidStack fluidStack(int amount) {
		if (hasFluid()) {
			return new FluidStack(fluid(), amount);
		}
		return FluidStack.EMPTY;
	}

	default FluidStack fluidStack() {
		return fluidStack(FluidType.BUCKET_VOLUME);
	}

	@Override
	default void create() {
		FlowingFluid fluid = getFluidConstructor(false).get();
		FlowingFluid flowing = getFluidConstructor(true).get();
		FluidType fluidType = createFluidType();
		setFluid(fluid);
		setFlowing(flowing);
		setFluidType(fluidType);
	}

	@Override
	@SuppressWarnings("unchecked")
	default void register(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.FLUIDS)) {
			IForgeRegistry<Fluid> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), fluid());
			registry.register(new ResourceLocation(getModId(), getIdentifier() + "_flowing"), flowing());
		}
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.FLUID_TYPES)) {
			IForgeRegistry<FluidType> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), getFluidType());
		}
	}
}
