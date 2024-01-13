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
package forestry.apiculture.particles;

import javax.annotation.Nonnull;
import java.util.Locale;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.BlockPos;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import forestry.core.registration.ParticleTypeRegistryObject;

import net.minecraft.core.particles.ParticleOptions.Deserializer;
import net.minecraftforge.registries.ForgeRegistries;

public class BeeParticleData implements ParticleOptions {

	public static final Deserializer<BeeParticleData> DESERIALIZER = new Deserializer<BeeParticleData>() {
		@Nonnull
		@Override
		public BeeParticleData fromCommand(@Nonnull ParticleType<BeeParticleData> type, @Nonnull StringReader reader) throws CommandSyntaxException {
			reader.expect(' ');
			long direction = reader.readLong();
			reader.expect(' ');
			int color = reader.readInt();
			return new BeeParticleData(type, direction, color);
		}

		@Override
		public BeeParticleData fromNetwork(@Nonnull ParticleType<BeeParticleData> type, FriendlyByteBuf buf) {
			return new BeeParticleData(type, buf.readLong(), buf.readInt());
		}
	};

	public static Codec<BeeParticleData> createCodec(ParticleType<BeeParticleData> type) {
		return RecordCodecBuilder.create(val -> val.group(Codec.LONG.fieldOf("direction").forGetter(data -> data.destination.asLong()), Codec.INT.fieldOf("color").forGetter(data -> data.color)).apply(val, (destination1, color1) -> new BeeParticleData(type, destination1, color1)));
	}

	public final ParticleType<BeeParticleData> type;
	public final BlockPos destination;
	public final int color;

	public BeeParticleData(ParticleType<BeeParticleData> type, long destination, int color) {
		this.type = type;
		this.destination = BlockPos.of(destination);
		this.color = color;
	}

	public BeeParticleData(ParticleTypeRegistryObject<BeeParticleData> type, BlockPos destination, int color) {
		this.type = type.getParticleType();
		this.destination = destination;
		this.color = color;
	}

	@Nonnull
	@Override
	public ParticleType<?> getType() {
		return type;
	}

	@Override
	public void writeToNetwork(@Nonnull FriendlyByteBuf buffer) {
		buffer.writeRegistryId(ForgeRegistries.PARTICLE_TYPES, type);
		buffer.writeLong(destination.asLong());
		buffer.writeInt(color);
	}

	@Nonnull
	@Override
	public String writeToString() {
		return String.format(Locale.ROOT, "%s %d %d %d %d", ForgeRegistries.PARTICLE_TYPES.getKey(getType()), destination.getX(), destination.getY(), destination.getZ(), color);
	}
}
