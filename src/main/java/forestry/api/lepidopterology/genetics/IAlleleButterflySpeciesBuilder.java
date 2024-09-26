/*******************************************************************************
 * Copyright 2011-2014 SirSengir
 *
 * This work (the API) is licensed under the "MIT" License, see LICENSE.txt for details.
 ******************************************************************************/
package forestry.api.lepidopterology.genetics;

import java.awt.Color;
import java.util.Collection;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import forestry.api.genetics.alleles.IAlleleSpeciesBuilder;

public interface IAlleleButterflySpeciesBuilder extends IAlleleSpeciesBuilder<IAlleleButterflySpeciesBuilder> {

	@Override
	IAlleleButterflySpecies build();

	IAlleleButterflySpeciesBuilder setRarity(float rarity);

	/**
	 * @param texturePath String texture path for this butterfly e.g. "forestry:butterfly/..."
	 */
	IAlleleButterflySpeciesBuilder setTexture(String texturePath);

	/**
	 * @param serumColour The color of this butterfly's serum.
	 */
	IAlleleButterflySpeciesBuilder setSerumColour(int serumColour);

	/**
	 * @param serumColour The color of this butterfly's serum.
	 */
	IAlleleButterflySpeciesBuilder setSerumColour(Color serumColour);

	IAlleleButterflySpeciesBuilder setFlightDistance(float flightDistance);

	IAlleleButterflySpeciesBuilder setNocturnal();

	IAlleleButterflySpeciesBuilder addSpawnBiomes(Collection<ResourceKey<Biome>> biomeTags);

	IAlleleButterflySpeciesBuilder addSpawnBiome(ResourceKey<Biome> biomeTag);
}
