package forestry.storage;

import javax.annotation.Nullable;

import forestry.core.utils.TagUtil;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Predicate;

public class BackpackFilter implements Predicate<ItemStack> {

	private final TagKey<Item> acceptKey;
	private final TagKey<Item> rejectKey;
	@Nullable
	private List<Item> cachedAccept;
	@Nullable
	private List<Item> cachedReject;

	public BackpackFilter(TagKey<Item> acceptKey, TagKey<Item> rejectKey) {
		this.acceptKey = acceptKey;
		this.rejectKey = rejectKey;
	}

	private List<Item> getAccept() {
		if (cachedAccept == null) {
			cachedAccept = getHolderSet(acceptKey);
		}
		return cachedAccept;
	}

	private List<Item> getReject() {
		if (cachedReject == null) {
			cachedReject = getHolderSet(rejectKey);
		}
	return cachedReject;
	}

	private static List<Item> getHolderSet(TagKey<Item> tagKey) {
		return ForgeRegistries.ITEMS.tags().getTag(tagKey).stream().toList();
	}

	@Override
	public boolean test(ItemStack itemStack) {
		// The backpack denies anything except what is allowed,
		// but from what is allowed you can say what will be rejected (like an override)
		// This allows broad wildcard "accept" types where you can still reject certain ones.
		Item item = itemStack.getItem();
		return getAccept().contains(item) && !getReject().contains(item);
	}
}
