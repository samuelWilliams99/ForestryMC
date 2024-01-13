package forestry.core.utils;

import net.minecraft.util.RandomSource;

import java.util.Random;

public final class TickHelper {
	private static final RandomSource rand = RandomSource.create();
	private int tickCount = rand.nextInt(2048);

	public void onTick() {
		tickCount++;
	}

	public boolean updateOnInterval(int tickInterval) {
		return tickCount % tickInterval == 0;
	}
}
