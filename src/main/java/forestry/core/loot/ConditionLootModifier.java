package forestry.core.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import forestry.arboriculture.loot.GrafterLootModifier;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;

import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.common.loot.LootTableIdCondition;

import forestry.core.config.Constants;

/**
 * A global loot modifier used by forestry to inject the additional chest loot to the vanilla loot tables.
 */
public class ConditionLootModifier extends LootModifier {
	public final static Codec<ConditionLootModifier> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					ResourceLocation.CODEC.fieldOf("table").forGetter(clm -> clm.tableLocation),
					Codec.STRING.listOf().xmap(l -> l.toArray(new String[0]), a -> Arrays.stream(a)
							.toList()).fieldOf("extensions").forGetter(clm -> clm.extensions)
			).apply(instance, ConditionLootModifier::new)
	);

	@Override
	public Codec<ConditionLootModifier> codec() {
		return CODEC;
	}

	private final ResourceLocation tableLocation;
	private final String[] extensions;

	public ConditionLootModifier(ResourceLocation location, String... extensions) {
		super(new LootItemCondition[]{
				LootTableIdCondition.builder(location).build()
		});
		this.tableLocation = location;
		this.extensions = extensions;
	}

	private static LootItemCondition[] merge(LootItemCondition[] conditions, LootItemCondition condition) {
		LootItemCondition[] newArray = Arrays.copyOf(conditions, conditions.length + 1);
		newArray[conditions.length] = condition;
		return newArray;
	}

	private ConditionLootModifier(LootItemCondition[] conditions, ResourceLocation location, String... extensions) {
		super(merge(conditions, LootTableIdCondition.builder(location).build()));
		this.tableLocation = location;
		this.extensions = extensions;
	}

	/**
	 * Helper field to prevent an endless method loop caused by forge in {@link LootTable#getRandomItems(LootContext)}
	 * which calls this method again, since it keeps the {@link LootContext#getQueriedLootTableId()} value, which causes
	 * "getRandomItems" to calling this method again, because the conditions still met even that it is an other loot
	 * table.
	 */
	private boolean operates = false;

	@Nonnull
	@Override
	protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (operates) {
			return generatedLoot;
		}
		operates = true;
		for (String extension : extensions) {
			ResourceLocation location = new ResourceLocation(Constants.MOD_ID, tableLocation.getPath() + "/" + extension);
			LootTable table = context.getLootTable(location);
			if (table != LootTable.EMPTY) {
				generatedLoot.addAll(table.getRandomItems(context));
			}
		}
		operates = false;
		return generatedLoot;
	}
}
