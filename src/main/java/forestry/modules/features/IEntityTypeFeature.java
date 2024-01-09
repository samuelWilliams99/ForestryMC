package forestry.modules.features;

import javax.annotation.Nullable;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.DefaultAttributes;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegisterEvent;

public interface IEntityTypeFeature<E extends Entity> extends IModFeature {

	@Override
	default void create() {
		EntityType<E> entityType = getEntityTypeConstructor().build(getIdentifier());
		setEntityType(entityType);
	}

	@Override
	@SuppressWarnings("unchecked")
	default void register(RegisterEvent event) {
		if (event.getRegistryKey().equals(ForgeRegistries.Keys.ENTITY_TYPES) && hasEntityType()) {
			IForgeRegistry<EntityType<E>> registry = event.getForgeRegistry();
			registry.register(new ResourceLocation(getModId(), getIdentifier()), entityType());
		}
	}

	default EntityType<E> entityType() {
		EntityType<E> tileType = getEntityType();
		if (tileType == null) {
			throw new IllegalStateException("Called feature getter method before content creation.");
		}
		return tileType;
	}

	AttributeSupplier.Builder createAttributes();

	void setEntityType(EntityType<E> entityType);

	EntityType.Builder<E> getEntityTypeConstructor();

	boolean hasEntityType();

	@Nullable
	EntityType<E> getEntityType();
}
